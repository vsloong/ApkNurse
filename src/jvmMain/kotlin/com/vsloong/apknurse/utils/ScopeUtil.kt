package com.vsloong.apknurse.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

/**
 * 创建一个IO Scope
 */
val ioScope = CoroutineScope(Dispatchers.IO)
