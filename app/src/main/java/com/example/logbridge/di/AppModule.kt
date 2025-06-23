package com.example.logbridge.di

import com.example.logbridge.data.local.LocalEntries
import com.example.logbridge.ui.screens.logDetails.util.LogDetailScreenModel
import com.example.logbridge.ui.screens.logPicker.util.LogPickerScreenModel
import com.example.logbridge.ui.screens.settings.util.SettingsScreenModel
import com.example.logbridge.utils.others.ObjectBoxBuilder
import io.objectbox.Box
import org.koin.dsl.module

val appModule = module{
    // Provide Box<UserVoiceEntity> from your ObjectBoxBuilder
    single<Box<LocalEntries>> { ObjectBoxBuilder.boxStore.boxFor(LocalEntries::class.java) }

    factory { LogPickerScreenModel(get()) }
    factory { LogDetailScreenModel(get()) }
    factory { SettingsScreenModel(get()) }

}