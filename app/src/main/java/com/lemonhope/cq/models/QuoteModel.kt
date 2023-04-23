package com.lemonhope.cq.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class QuoteModel() : RealmObject{

    @PrimaryKey
    var _id: org.mongodb.kbson.ObjectId = ObjectId.invoke()
    var quote: String = ""
    var author: String = ""
    var topic: ArrayList<String> = arrayListOf<String>()
    }