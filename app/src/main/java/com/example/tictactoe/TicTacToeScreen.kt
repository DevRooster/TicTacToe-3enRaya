package com.example.tictactoe

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.TextStyle
import kotlinx.coroutines.launch
import com.example.tictactoe.data.Resultado
import com.example.tictactoe.data.ResultadoRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicTacToeScreen(repository: ResultadoRepository) {
    // Variables de estado
    var nombrePartida by remember { mutableStateOf("Tic Tac Toe") }
    var nombreJugador1 by remember { mutableStateOf("") }
    var nombreJugador2 by remember { mutableStateOf("") }
    var ganador by remember { mutableStateOf("") }
    var punto by remember { mutableStateOf(0) }
    var estado by remember { mutableStateOf("") }
    var turno by remember { mutableStateOf(true) } // true para jugador 1, false para jugador 2
    var tablero by remember { mutableStateOf(Array(3) { Array(3) { "" } }) } // Tablero de 3x3
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Barra superior
        TopAppBar(
            title = { Text("Tic Tac Toe") }
        )

        // Campos de texto para los nombres de los jugadores
        OutlinedTextField(
            value = nombreJugador1,
            onValueChange = { nombreJugador1 = it },
            label = { Text("Jugador 1") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = nombreJugador2,
            onValueChange = { nombreJugador2 = it },
            label = { Text("Jugador 2") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tablero de Tic Tac Toe
        for (row in 0..2) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                for (col in 0..2) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .padding(4.dp)
                            .clickable {
                                if (tablero[row][col].isEmpty() && ganador.isEmpty()) {
                                    tablero[row][col] = if (turno) "X" else "O"
                                    turno = !turno
                                    ganador = checkWinner(tablero, nombreJugador1, nombreJugador2)
                                }
                            }
                            .background(Color.LightGray, RoundedCornerShape(8.dp)), // Modificado
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = tablero[row][col], style = TextStyle(fontSize = MaterialTheme.typography.headlineMedium.fontSize)) // Modificado
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Inserción de resultado en la base de datos
                coroutineScope.launch {
                    val resultado = Resultado(
                        nombre_partida = nombrePartida,
                        nombre_jugador1 = nombreJugador1,
                        nombre_jugador2 = nombreJugador2,
                        ganador = ganador,
                        punto = punto,
                        estado = estado
                    )
                    repository.insertResultado(resultado)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Finalizar Juego")
        }

        // Mostrar ganador
        if (ganador.isNotEmpty()) {
            Text(text = "Ganador: $ganador", style = TextStyle(fontSize = MaterialTheme.typography.headlineSmall.fontSize)) // Modificado
        }
    }
}

// Función para verificar el ganador
private fun checkWinner(tablero: Array<Array<String>>, jugador1: String, jugador2: String): String {
    // Comprobar filas, columnas y diagonales
    val combinacionesGanadoras = listOf(
        // Filas
        listOf(Pair(0, 0), Pair(0, 1), Pair(0, 2)),
        listOf(Pair(1, 0), Pair(1, 1), Pair(1, 2)),
        listOf(Pair(2, 0), Pair(2, 1), Pair(2, 2)),
        // Columnas
        listOf(Pair(0, 0), Pair(1, 0), Pair(2, 0)),
        listOf(Pair(0, 1), Pair(1, 1), Pair(2, 1)),
        listOf(Pair(0, 2), Pair(1, 2), Pair(2, 2)),
        // Diagonales
        listOf(Pair(0, 0), Pair(1, 1), Pair(2, 2)),
        listOf(Pair(0, 2), Pair(1, 1), Pair(2, 0))
    )

    for (combinacion in combinacionesGanadoras) {
        val (a, b, c) = combinacion
        if (tablero[a.first][a.second] == "X" && tablero[b.first][b.second] == "X" && tablero[c.first][c.second] == "X") {
            return jugador1 // Jugador 1 gana
        } else if (tablero[a.first][a.second] == "O" && tablero[b.first][b.second] == "O" && tablero[c.first][c.second] == "O") {
            return jugador2 // Jugador 2 gana
        }
    }

    // Comprobar si el tablero está lleno (empate)
    return if (tablero.all { row -> row.all { cell -> cell.isNotEmpty() } }) {
        "Empate"
    } else {
        "" // Sin ganador aún
    }
}