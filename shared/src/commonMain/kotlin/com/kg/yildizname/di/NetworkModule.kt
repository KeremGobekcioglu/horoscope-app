package com.kg.yildizname.di

import com.kg.yildizname.core.data.remote.FirestoreReadingSource
import com.kg.yildizname.core.data.remote.HoroscopeApiSource
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true }) }
            install(HttpTimeout) { requestTimeoutMillis = 10_000 }
        }
    }
    single { HoroscopeApiSource(get()) }
    single { FirestoreReadingSource(Firebase.firestore) }
}
