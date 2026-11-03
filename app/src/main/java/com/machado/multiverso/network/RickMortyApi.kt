package com.machado.multiverso.network

import com.machado.multiverso.data.PersonagemDto
import com.machado.multiverso.data.RespostaPersonagens
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickMortyApi {
    // Parâmetros nulos são ignorados pelo Retrofit (não vão na URL).
    @GET("character")
    suspend fun personagens(
        @Query("page") pagina: Int,
        @Query("name") nome: String?,
        @Query("status") status: String?
    ): RespostaPersonagens

    @GET("character/{id}")
    suspend fun personagem(@Path("id") id: Int): PersonagemDto
}

object RetrofitInstance {
    private val json = Json { ignoreUnknownKeys = true }

    val api: RickMortyApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://rickandmortyapi.com/api/")
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(RickMortyApi::class.java)
    }
}
