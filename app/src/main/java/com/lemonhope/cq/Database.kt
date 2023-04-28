package com.lemonhope.cq

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.lemonhope.cq.models.Quote
import com.lemonhope.cq.models.QuoteModel
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import android.content.res.Resources
import android.content.res.loader.ResourcesLoader
import android.content.res.loader.ResourcesProvider
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat

class Database {
    companion object {
        @Volatile
        private var instance: Realm? = null

        fun getInstance(resources: Resources): Realm {
            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        val config =
                            RealmConfiguration.Builder(setOf(QuoteModel::class))
                                .compactOnLaunch()
                                .initialData{
                                    // Show something as it is the first startup
                                    // Delete raw resource
                                    val gson = Gson()
                                    val text = resources.openRawResource(R.raw.quotes2).bufferedReader().use { it.readText() }
                                    val quoteType = object : TypeToken<List<Quote>>() {}.type
                                    val jsonQuotes = gson.fromJson(text, quoteType) as List<Quote>
                                    for(quote in jsonQuotes){
                                        val temp = QuoteModel()
                                        temp.quote = quote.quote
                                        temp.author = quote.author
                                        for (topic in quote.topics){
                                            temp.topics.add(topic)
                                        }
                                        this.copyToRealm(temp)
                                    }
                                }
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
    }
}