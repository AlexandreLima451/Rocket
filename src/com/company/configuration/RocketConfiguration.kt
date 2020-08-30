package com.company.configuration

import com.google.gson.GsonBuilder
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
    var zipFilePath: String = ""
    var shouldSendEmail: Boolean = false
    var shouldSendReport: Boolean = false
    var shouldDeleteFilesAfterProcess = false
    var emailAddressSender: String = ""
    var passwordEmailSender: String = ""
    var emailAddressRecipient: String = ""
    var quantityFilesPerPackage: Int = 50
    var emailSubject: String = ""

    fun updateEmailSubject(subject: String): RocketConfiguration {
        this.emailSubject = subject
        updateConfigFile()
        return this
    }

    fun updateTextToRemoveFileName(text: String): RocketConfiguration {
        this.textToRemoveFilename = text
        updateConfigFile()
        return this
    }

    fun updateZipFilePath(filepath: String): RocketConfiguration {
        this.zipFilePath = filepath
        updateConfigFile()
        return this
    }

    fun updateShouldSendEmail(sendEmail: Boolean): RocketConfiguration {
        this.shouldSendEmail = sendEmail
        updateConfigFile()
        return this
    }

    fun updateShouldSendReport(sendReport: Boolean): RocketConfiguration {
        this.shouldSendReport = sendReport
        updateConfigFile()
        return this
    }

    fun updateShouldDeleteFilesAfterProcess(shouldDelete: Boolean): RocketConfiguration {
        this.shouldDeleteFilesAfterProcess = shouldDelete
        updateConfigFile()
        return this
    }

    fun updateEmailAddressSender(emailAddress: String): RocketConfiguration {
        this.emailAddressSender = emailAddress
        updateConfigFile()
        return this
    }

    fun updatePasswordEmailSender(password: String): RocketConfiguration {
        this.passwordEmailSender = password
        updateConfigFile()
        return this
    }

    fun updateEmailAddressRecipient(emailAddress: String): RocketConfiguration {
        this.emailAddressRecipient = emailAddress
        updateConfigFile()
        return this
    }

    fun updateQuantityFilesPerPackage(quantity: Int): RocketConfiguration {
        this.quantityFilesPerPackage = quantity
        updateConfigFile()
        return this
    }

    private fun updateConfigFile() {
        try {
            FileWriter(CONFIGURATION_FILE).use { file ->
                val jsonString = GsonBuilder()
                        .setPrettyPrinting()
                        .create()
                        .toJson(this, RocketConfiguration::class.java).toString()
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
                return GsonBuilder()
                        .setPrettyPrinting()
                        .create()
                        .fromJson<RocketConfiguration>(
                                jsonString.toJSONString(),
                                RocketConfiguration::class.java
                        )
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

