package com.example.tictactoe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TicTacToeScreen(
    modifier: Modifier = Modifier,
    viewModel: TicTacToeViewModel = TicTacToeViewModel()
){
    val state = viewModel.state.value
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val turn = if(state.isXTurn) "Turno de X" else "Turno de O"
        val turnMessage = "Tic Tac Toe\nEs el $turn"
        val winner = state.victor
        val winnerMessage = "Tic Tac Toe\n$winner Gana"
        Text(
            text = if(winner != null) winnerMessage else turnMessage,
            textAlign = TextAlign.Center,
            modifier = modifier.padding(16.dp),
            fontSize = 40.sp,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.headlineMedium
        )

        BuildRow(rowId = 1, viewModel = viewModel)
        BuildRow(rowId = 2, viewModel = viewModel)
        BuildRow(rowId = 3, viewModel = viewModel)

        Button(
            onClick = {viewModel.resetBoard()},
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            )
        ){
            Text(text = "Reset Game", fontSize = 32.sp)
        }

    }
}

@Composable
fun BuildRow(
    rowId: Int,
    modifier: Modifier = Modifier,
    viewModel: TicTacToeViewModel
){
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        val third = (rowId * 3) - 1
        val second = third - 1
        val first = second - 1
        val buttonColors = viewModel.state.value.buttonWinners
        val buttonValues = viewModel.state.value.buttonValues
        TicTacToeButton(buttonValues[first],buttonColors[first]) { viewModel.setButton(first)}
        TicTacToeButton(buttonValues[second],buttonColors[second]) { viewModel.setButton(second)}
        TicTacToeButton(buttonValues[third],buttonColors[third]) { viewModel.setButton(third)}
    }
}


@Composable
fun TicTacToeButton(
    button: String,
    shouldChangeColor: Boolean,
    onClick: () -> Unit,
){
    val color = if(shouldChangeColor) MaterialTheme.colorScheme.tertiary
                    else MaterialTheme.colorScheme.primary
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = Color.White
        )
    ){
        Text(text = button, fontSize = 50.sp)
    }
}














