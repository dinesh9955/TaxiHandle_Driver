package com.rydz.driver.model

import com.rydz.driver.model.chat.ChatUser

data class Msg(
        var __v: Int = 0, // 0
        var _id: String = "", // 5dbfd8d554296b19a17d34ac
        var bookingId: String = "", // 5dbfd07f72a4fb1877690da0
        var date: Long = 0, // 1572853973252
        var message: String = "", // zcbfhxhfdt
        var messageBy: Int = 0, // 0
        var messageType: Int = 0, // 0
        var opponentReadStatus: Int = 0, // 0
        var status: Int = 0, // 0
        var userReadStatus: Int = 0, // 0
        var user:ChatUser=ChatUser()
)