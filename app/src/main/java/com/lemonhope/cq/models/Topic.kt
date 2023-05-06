package com.lemonhope.cq.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Topic() : RealmObject{

    @PrimaryKey
    var _id: org.mongodb.kbson.ObjectId = ObjectId.invoke()
    var topic: String = ""
    var color: String = ""
}