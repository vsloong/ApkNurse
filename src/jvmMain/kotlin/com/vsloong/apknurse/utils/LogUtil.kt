package com.vsloong.apknurse.utils

fun logger(msg: String) {
    println("${loggerTimeMillis()} ${Thread.currentThread().name} --- $msg")
}