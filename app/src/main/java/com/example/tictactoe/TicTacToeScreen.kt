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
    var puntajeJugador1 by remember { mutableStateOf(0) }
    var puntajeJugador2 by remember { mutableStateOf(0) }
    var tablero by remember { mutableStateOf(Array(3) { Array(3) { "" } }) } // Tablero de 3x3
    var turno by remember { mutableStateOf(true) } // true para jugador 1 (X), false para jugador 2 (O)
    val coroutineScope = rememberCoroutineScope()

    // Variable para mostrar el jugador actual
    val jugadorActual = if (turno) "X" else "O"

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

        // Mostrar de quién es el turno
        Text(
            text = if (ganador.isEmpty()) "Turno del jugador: $jugadorActual" else "Ganador: $ganador",
            style = TextStyle(fontSize = MaterialTheme.typography.titleLarge.fontSize)
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
                                    // Colocar X o O y cambiar el turno
                                    tablero[row][col] = if (turno) "X" else "O"
                                    turno = !turno
                                    ganador = checkWinner(tablero, nombreJugador1, nombreJugador2).also { resultado ->
                                        // Actualizar puntajes según el resultado
                                        if (resultado == nombreJugador1) {
                                            puntajeJugador1++
                                        } else if (resultado == nombreJugador2) {
                                            puntajeJugador2++
                                        }
                                    }
                                }
                            }
                            .background(Color.LightGray, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = tablero[row][col], style = TextStyle(fontSize = MaterialTheme.typography.headlineMedium.fontSize))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Tabla de puntajes
        if (ganador.isNotEmpty()) {
            Text("Ganador: $ganador", style = TextStyle(fontSize = MaterialTheme.typography.headlineSmall.fontSize))
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Tabla de Puntajes", style = TextStyle(fontSize = MaterialTheme.typography.titleLarge.fontSize))
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(nombreJugador1, modifier = Modifier.weight(1f))
            Text("$puntajeJugador1", modifier = Modifier.weight(1f), style = TextStyle(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold))
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(nombreJugador2, modifier = Modifier.weight(1f))
            Text("$puntajeJugador2", modifier = Modifier.weight(1f), style = TextStyle(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para reiniciar el juego
        Button(
            onClick = {
                // Reiniciar el estado del juego
                tablero = Array(3) { Array(3) { "" } }
                ganador = ""
                turno = true // Empezar con el jugador 1
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reiniciar Juego")
        }

        // Inserción de resultado en la base de datos al finalizar el juego
        Button(
            onClick = {
                // Inserción de resultado en la base de datos
                coroutineScope.launch {
                    val resultado = Resultado(
                        nombre_partida = nombrePartida,
                        nombre_jugador1 = nombreJugador1,
                        nombre_jugador2 = nombreJugador2,
                        ganador = ganador,
                        punto = if (ganador == nombreJugador1) puntajeJugador1 else puntajeJugador2,
                        estado = "Finalizado"
                    )
                    repository.insertResultado(resultado)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Finalizar Juego")
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