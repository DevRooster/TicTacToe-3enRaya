package com.example.tictactoe.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "resultados")
data class Resultado(
    @PrimaryKey(autoGenerate = true) val id_resultado: Int = 0,
    val nombre_partida: String,
    val nombre_jugador1: String,
    val nombre_jugador2: String,
    val ganador: String,
    val punto: Int,
    val estado: String
)