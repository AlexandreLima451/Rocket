package com.company.util

import com.google.gson.JsonParser
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class LoginManager protected constructor() {
    private val firebaseKey = "AIzaSyDkT2L2l982uj84F5aYonddArwUBH2CC00"

    @Throws(Exception::class)
    fun auth(username: String, password: String): Boolean? {
        var urlRequest: HttpURLConnection? = null
        var token: String? = null
        try {
            val url = URL("$BASE_URL$OPERATION_AUTH?key=$firebaseKey")
            urlRequest = url.openConnection() as HttpURLConnection
            urlRequest.setDoOutput(true)
            urlRequest.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
            val os: OutputStream = urlRequest.getOutputStream()
            val osw = OutputStreamWriter(os, "UTF-8")
            osw.write("{\"email\":\"$username\",\"password\":\"$password\",\"returnSecureToken\":true}")
            osw.flush()
            osw.close()
            urlRequest.connect()
            val jp = JsonParser() //from gson
            val root = jp.parse(InputStreamReader(urlRequest.getContent() as InputStream)) //Convert the input stream to a json element
            val rootobj = root.asJsonObject //May be an array, may be an object.
            token = rootobj["idToken"].asString
        } catch (e: Exception) {
            return null
        } finally {
            urlRequest?.disconnect()
        }
        if (token != null) {
            val userInfo = getAccountInfo(token)
            if (userInfo == username) {
                return true
            }
        }
        return false
    }

    @Throws(Exception::class)
    fun getAccountInfo(token: String): String? {
        var urlRequest: HttpURLConnection? = null
        var email: String? = null
        try {
            val url = URL("$BASE_URL$OPERATION_ACCOUNT_INFO?key=$firebaseKey")
            urlRequest = url.openConnection() as HttpURLConnection
            urlRequest.setDoOutput(true)
            urlRequest.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
            val os: OutputStream = urlRequest.getOutputStream()
            val osw = OutputStreamWriter(os, "UTF-8")
            osw.write("{\"idToken\":\"$token\"}")
            osw.flush()
            osw.close()
            urlRequest.connect()
            val jp = JsonParser() //from gson
            val root = jp.parse(InputStreamReader(urlRequest.getContent() as InputStream)) //Convert the input stream to a json element
            val rootobj = root.asJsonObject //May be an array, may be an object.
            email = rootobj["users"].asJsonArray[0].asJsonObject["email"].asString
        } catch (e: Exception) {
            return null
        } finally {
            urlRequest?.disconnect()
        }
        return email
    }

    companion object {
        private const val BASE_URL = "https://www.googleapis.com/identitytoolkit/v3/relyingparty/"
        private const val OPERATION_AUTH = "verifyPassword"
        private const val OPERATION_REFRESH_TOKEN = "token"
        private const val OPERATION_ACCOUNT_INFO = "getAccountInfo"
        var instance: LoginManager? = null
            get() {
                if (field == null) {
                    field = LoginManager()
                }
                return field
            }
            private set
    }

}