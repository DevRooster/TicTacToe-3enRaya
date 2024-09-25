package com.example.tictactoe

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class TicTacToeViewModel: ViewModel() {
    private val _state = mutableStateOf(TicTicToeState())
    val state: State<TicTicToeState> = _state

    fun setButton(id: Int) {
        if(_state.value.victor == null) {
            if (_state.value.buttonValues[id].equals("-")) {
                val buttons = _state.value.buttonValues.copyOf()
                if (_state.value.isXTurn) {
                    buttons[id] = "X"
                } else {
                    buttons[id] = "O"
                }
                _state.value = _state.value.copy(
                    buttonValues = buttons,
                    isXTurn = !_state.value.isXTurn
                )
            }
        }
        isGameOver()
    }

    private fun isGameOver(): Boolean {
        if(rowHasWinner(1) || rowHasWinner(2) || rowHasWinner(3)){
            return true
        }else if (columnHasWinner(1) || columnHasWinner(2) || columnHasWinner(3)){
            return true
        }else if(firstDiagonalHasWinner() || secondDiagonalHasWinner()){
            return true
        }
        return false
    }

    private fun rowHasWinner(rowId: Int): Boolean{
        val third = (rowId * 3) - 1
        val second = third - 1
        val first = second - 1
        return compareSpaces(first,second,third)
    }

    private fun columnHasWinner(columnId: Int): Boolean{
        val first = columnId -1
        val second = first + 3
        val third = first + 6
        return compareSpaces(first,second,third)
    }

    private fun firstDiagonalHasWinner(): Boolean{
        return compareSpaces(0,4,8)
    }

    private fun secondDiagonalHasWinner(): Boolean{
        return compareSpaces(2,4,6)
    }


    private fun compareSpaces(first: Int, second: Int, third: Int): Boolean{
        val firstTwoMatch = _state.value.buttonValues[first] == _state.value.buttonValues[second]
        val secondTwoMatch = _state.value.buttonValues[second] == _state.value.buttonValues[third]
        val allThreeMatch = firstTwoMatch && secondTwoMatch
        return if(_state.value.buttonValues[first] == "-"){
            false
        }else if(allThreeMatch){
            _state.value = _state.value.copy(victor = _state.value.buttonValues[first])
            val buttonWinners = _state.value.buttonWinners.copyOf()
            buttonWinners[first] = true
            buttonWinners[second] = true
            buttonWinners[third] = true
            _state.value = _state.value.copy(buttonWinners = buttonWinners)
            true
        }else{
            false
        }
    }





    fun resetBoard(){
        _state.value = TicTicToeState()
    }
}