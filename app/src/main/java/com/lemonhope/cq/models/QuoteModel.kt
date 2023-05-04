package com.lemonhope.cq.models

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Ignore
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class QuoteModel() : RealmObject{

    @PrimaryKey
    var _id: org.mongodb.kbson.ObjectId = ObjectId.invoke()
    var index: Int = 0
    var quote: String = ""
    var author: String = ""
    var topics: RealmList<String> = realmListOf()
    var favourite: Boolean = false

    override fun toString(): String {
        return "$quote\n\n— $author"
    }

}