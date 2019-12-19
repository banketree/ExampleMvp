package com.example.component_demo1.ui.mvvm.bean


import com.google.gson.annotations.SerializedName

data class Fiction(
    var bid: String = "",
    var bookname: String = "",
    var introduction: String = "",
    @SerializedName("book_info")
    var bookInfo: String = "",
    var chapterid: String = "",
    var topic: String = "",
    @SerializedName("topic_first")
    var topicFirst: String = "",
    @SerializedName("date_updated")
    var dateUpdated: Int = 0,
    var author: String = "",
    @SerializedName("author_name")
    var authorName: String = "",
    @SerializedName("top_class")
    var topClass: String = "",
    var state: String = "",
    var readCount: String = "",
    var praiseCount: String = "",
    @SerializedName("stat_name")
    var statName: String = "",
    @SerializedName("class_name")
    var className: String = "",
    var size: String = "",
    @SerializedName("book_cover")
    var bookCover: String = "",
    @SerializedName("chapterid_first")
    var chapteridFirst: String = "",
    var chargeMode: String = "",
    var digest: String = "",
    var price: String = "",
    var tag: List<String> = listOf(),
    @SerializedName("is_new")
    var isNew: Int = 0,
    var discountNum: Int = 0,
    @SerializedName("quick_price")
    var quickPrice: Int = 0,
    var formats: String = "",
    @SerializedName("audiobook_playCount")
    var audiobookPlayCount: String = "",
    var chapterNum: String = "",
    var isShortStory: Boolean = false,
    var userid: String = "",
    @SerializedName("search_heat")
    var searchHeat: String = "",
    @SerializedName("num_click")
    var numClick: String = "",
    @SerializedName("recommend_num")
    var recommendNum: String = "",
    @SerializedName("first_cate_id")
    var firstCateId: String = "",
    @SerializedName("first_cate_name")
    var firstCateName: String = "",
    var reason: String = ""
)