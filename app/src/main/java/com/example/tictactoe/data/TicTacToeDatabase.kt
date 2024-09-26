package com.example.tictactoe.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Resultado::class], version = 1, exportSchema = false)
abstract class TicTacToeDatabase : RoomDatabase() {
    abstract fun resultadoDao(): ResultadoDao
}