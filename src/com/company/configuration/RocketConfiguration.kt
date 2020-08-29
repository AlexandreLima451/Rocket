package com.company.configuration

import com.google.gson.Gson
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.text.ParseException

private const val CONFIGURATION_FILE = "config.json"
class RocketConfiguration {

    var textToRemoveFilename: String = ""
    var shouldSendEmail: Boolean = false
    var shouldSendReport: Boolean = false
    var shouldDeleteFilesAfterProcess = false
    var emailAddressSender: String = ""
    var passwordEmailSender: String = ""
    var emailAddressRecipient: String = ""
    var quantityFilesPerPackage: Int = 20

    fun updateTextToRemoveFileName(text: String) {
        this.textToRemoveFilename = text
        updateConfigFile()
    }

    fun updateShouldSendEmail(sendEmail: Boolean) {
        this.shouldSendEmail = sendEmail
        updateConfigFile()
    }

    fun updateShouldSendReport(sendReport: Boolean) {
        this.shouldSendReport = sendReport
        updateConfigFile()
    }

    fun updateShouldDeleteFilesAfterProcess(shouldDelete: Boolean) {
        this.shouldDeleteFilesAfterProcess = shouldDelete
        updateConfigFile()
    }

    fun updateEmailAddressSender(emailAddress: String) {
        this.emailAddressSender = emailAddress
        updateConfigFile()
    }

    fun updatePasswordEmailSender(password: String) {
        this.passwordEmailSender = password
        updateConfigFile()
    }

    fun updateEmailAddressRecipient(emailAddress: String) {
        this.emailAddressRecipient = emailAddress
        updateConfigFile()
    }

    fun updateQuantityFilesPerPackage(quantity: Int) {
        this.quantityFilesPerPackage = quantity
        updateConfigFile()
    }

    private fun updateConfigFile() {
        try {
            FileWriter(CONFIGURATION_FILE).use { file ->
                val jsonString = Gson().toJson(this, RocketConfiguration::class.java).toString()
                file.write(jsonString)
                file.flush()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    companion object {
        fun instance(): RocketConfiguration {
            try {
                val jsonParser = JSONParser()
                val reader = FileReader(CONFIGURATION_FILE)
                val jsonString: JSONObject = jsonParser.parse(reader) as JSONObject
                return Gson().fromJson<RocketConfiguration>(jsonString.toJSONString(), RocketConfiguration::class.java)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return RocketConfiguration()
        }
    }
}

