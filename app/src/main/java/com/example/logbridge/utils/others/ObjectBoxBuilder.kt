package com.example.logbridge.utils.others

import android.content.Context
import com.example.logbridge.data.local.MyObjectBox
import io.objectbox.BoxStore

object ObjectBoxBuilder {
    lateinit var boxStore: BoxStore
        private set

    fun init(context: Context) {
        boxStore = MyObjectBox.builder()
            .androidContext(context)
            .build()
    }
}

