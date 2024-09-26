package com.example.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.tictactoe.TicTacToeScreen
import com.example.tictactoe.data.AppDatabase
import com.example.tictactoe.data.ResultadoRepository
import com.example.tictactoe.ui.theme.TicTacToeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = AppDatabase.getDatabase(this)
        val repository = ResultadoRepository(database.resultadoDao())

        setContent {
            TicTacToeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TicTacToeScreen(repository) // Asegúrate de pasar el repository aquí
                }
            }
        }
    }
}