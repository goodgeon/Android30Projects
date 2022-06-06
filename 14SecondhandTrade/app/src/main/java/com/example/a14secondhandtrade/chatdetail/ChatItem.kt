package com.example.a14secondhandtrade.chatdetail

data class ChatItem(
    val senderId: String,
    val message: String
) {
    constructor(): this("","")
}
