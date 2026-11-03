package com.machado.multiverso.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun inserir(usuario: Usuario): Long

    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    suspend fun buscarPorEmail(email: String): Usuario?

    @Query("SELECT * FROM usuarios WHERE id = :id LIMIT 1")
    fun observarPorId(id: Int): Flow<Usuario?>
}

@Dao
interface FavoritoDao {
    @Query("SELECT * FROM favoritos WHERE usuarioId = :usuarioId ORDER BY personagemId")
    fun observar(usuarioId: Int): Flow<List<Favorito>>

    @Query("SELECT * FROM favoritos WHERE usuarioId = :usuarioId AND personagemId = :personagemId LIMIT 1")
    suspend fun buscar(usuarioId: Int, personagemId: Int): Favorito?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(favorito: Favorito)

    @Query("DELETE FROM favoritos WHERE usuarioId = :usuarioId AND personagemId = :personagemId")
    suspend fun remover(usuarioId: Int, personagemId: Int)
}
