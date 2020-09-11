package com.company.util

object CryptoManager {

    fun Cripto(content: String): String {
        //Criptografa a String passada por parâmetro
        var text = content
        var contador: Int
        val tamanho: Int
        var ASCIIcode: Int
        var cryptoText = ""
        tamanho = text.length
        contador = 0
        while (contador < tamanho) {
            ASCIIcode = text[contador].toInt() + 130
            cryptoText += ASCIIcode.toChar()
            contador++
        }
        return cryptoText
    }

    fun Decripto(content: String): String {
        //Descriptografa a String passada por parâmetro
        var text = content
        var contador: Int
        val tamanho: Int
        var codigoASCII: Int
        var senhaCriptografada = ""
        tamanho = text.length
        contador = 0
        while (contador < tamanho) {
            codigoASCII = text[contador].toInt() - 130
            senhaCriptografada = senhaCriptografada + codigoASCII.toChar()
            contador++
        }
        return senhaCriptografada
    }
}