package com.example.tictactoe.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ResultadoDao {
    @Insert
    suspend fun insert(resultado: Resultado)

    @Query("SELECT * FROM resultados ORDER BY id_resultado DESC")
    suspend fun getAllResultados(): List<Resultado>
}