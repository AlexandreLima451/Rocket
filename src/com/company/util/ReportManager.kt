package com.company.util

import com.company.extensions.formatToTimeDefault
import io.reactivex.Observer
import io.reactivex.subjects.PublishSubject
import java.lang.StringBuilder
import java.util.*

class ReportManager {

    private val report = StringBuilder()
    private val middle = StringBuilder()
    private val footer = StringBuilder()
    private var totalEmails = 0
    private var totalPackages = 0
    private var totalFiles = 0

    fun headerDetails(logger: Logger) {
        val information = """ 
                Processamento de arquivos iniciado as ${Date().formatToTimeDefault()}
                --------------------------------------------------------------------------------
                
            """.trimIndent()
        logger.log(information)
        report.append(information)
        report.append("\n")
    }

    fun footerDetails(logger: Logger) {
        val information = """ 
                Processamento de arquivos finalizado as ${Date().formatToTimeDefault()}
                
                | Total de arquivos XML enviados: ${totalFiles}
                | Total de pacotes enviados: ${totalPackages}
                | Total de e-mails enviados: ${totalEmails}
                --------------------------------------------------------------------------------

                
            """.trimIndent()
        logger.log("Processamento finalizado às ${Date().formatToTimeDefault()}")
        footer.append(information)
        footer.append("\n")
    }

    fun filesDetails(fileData: FileData) {
        val dataInfo = StringBuilder()

        dataInfo.append("-> Pacote ${fileData.order}.zip gerado com os seguintes arquivos:")
        dataInfo.append("\n")
        fileData.listOfFiles.forEach {
            dataInfo.append(it)
            dataInfo.append("\n")
        }
        dataInfo.append("\n")
        dataInfo.append("\n")

        totalFiles += fileData.listOfFiles.size
        totalEmails++
        totalPackages++
        middle.append(dataInfo.toString())
    }

    fun printReport() {
        report.append(footer)
        report.append(middle)
        EmailManager().sendReport(report.toString())
        print(report.toString())
    }
}
