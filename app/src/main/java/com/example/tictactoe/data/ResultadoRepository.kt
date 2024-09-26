package com.example.tictactoe.data


class ResultadoRepository(private val dao: ResultadoDao) {

    suspend fun insertResultado(resultado: Resultado) {
        dao.insert(resultado)
    }

    suspend fun getAllResultados(): List<Resultado> {
        return dao.getAllResultados()
    }
}