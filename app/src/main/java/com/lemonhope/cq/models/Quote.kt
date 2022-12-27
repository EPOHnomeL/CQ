package com.lemonhope.cq.models

data class Quote(
    val quote: String,
    val author: String,
    val topic: List<String>,
    val id: Int
) {
    override fun toString(): String {

        var temp: String = if (topic.size == 1) "Topic: " else "Topics: "
        for (t in topic) {
            temp += "$t "
        }
        return "$quote\n\n— $author"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Quote

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }

}