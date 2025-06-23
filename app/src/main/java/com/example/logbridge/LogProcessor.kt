package com.example.logbridge

object LogProcessor {
    init {
        System.loadLibrary("logprocessor")
    }

    external fun processLog(input: String): String
}
