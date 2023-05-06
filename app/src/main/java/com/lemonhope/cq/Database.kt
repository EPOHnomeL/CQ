package com.lemonhope.cq

import android.content.res.Resources
import android.graphics.Color
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lemonhope.cq.models.Quote
import com.lemonhope.cq.models.QuoteModel
import com.lemonhope.cq.models.Topic
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import kotlin.random.Random

class Database {
    companion object {
        @Volatile
        private var instance: Realm? = null
        private lateinit var resources: Resources

        fun setResources(resources: Resources) {
            this.resources = resources
        }

        fun getInstance(): Realm {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        val config =
                            RealmConfiguration.Builder(setOf(QuoteModel::class, Topic::class))
                                .compactOnLaunch()
                                .initialData {
                                    // Show something as it is the first startup
                                    // Delete raw resource
                                    Log.i("loading", "LOADING JSON TO DB")
                                    val gson = Gson()
                                    val rawQuotes =
                                        resources.openRawResource(R.raw.quotes2).bufferedReader()
                                            .use { it.readText() }
                                    val rawTopics =
                                        resources.openRawResource(R.raw.topics).bufferedReader()
                                            .use { it.readText() }
                                    val quoteType = object : TypeToken<List<Quote>>() {}.type
                                    val topicType = object : TypeToken<List<Topic>>() {}.type
                                    val jsonQuotes = gson.fromJson(rawQuotes, quoteType) as List<Quote>
                                    for ((i, quote) in jsonQuotes.withIndex()) {
                                        val temp = QuoteModel()
                                        temp.quote = quote.quote
                                        temp.author = quote.author
                                        temp.favourite = false
                                        temp.index = i
                                        for (topic in quote.topics) {
                                            temp.topics.add(topic)
                                        }
                                        this.copyToRealm(temp)
                                    }
                                    val jsonTopic = gson.fromJson(rawTopics, topicType) as List<Topic>
                                    jsonTopic.forEach{
                                        this.copyToRealm(it)
                                    }
                                }
                                .schemaVersion(2)
                                .build()
                        instance = Realm.open(config)
                    }
                }
            }
            return instance!!
        }

        fun closeDatabase() {
            instance!!.close()
        }

        fun topicToColor(topic: String): Int {
            val topicObj = getInstance().query<Topic>( "topic = $0", topic).first().find()
            val topicV = getInstance().copyFromRealm(topicObj!!)

            return Color.parseColor(topicV.color)
        }
    }
}