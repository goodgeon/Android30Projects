package com.example.a14secondhandtrade.chatlist

data class ChatListItem(
    val buyerId: String,
    val sellerId: String,
    val itemTitle: String,
    val key: String
) {
    constructor(): this("", "", "", "")
}
