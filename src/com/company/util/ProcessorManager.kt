package com.company.util

import com.company.Main
import com.company.configuration.RocketConfiguration
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.security.CodeSource


class ProcessorManager {

    fun startProcess(logger: Logger) {
        val configuration = RocketConfiguration.instance()
        val fileManager = FileManager()
        val emailManager = EmailManager()
        val reportManager = ReportManager()
        Single.just(true)
                .map {
                    reportManager.headerDetails(logger)
                }
                .map {
                    val codeSource: CodeSource = Main::class.java.protectionDomain.codeSource
                    val jarFile = File(codeSource.location.toURI().path)
                    val jarDir = jarFile.parentFile.path
                    logger.log("Descompactando o arquivo selecionado")
                    fileManager.unzipFolder2(configuration.zipFilePath, jarDir)
                }
                .map {
                    val codeSource: CodeSource = Main::class.java.protectionDomain.codeSource
                    val jarFile = File(codeSource.location.toURI().path)
                    val jarDir = jarFile.parentFile.path
                    val dir = File("$jarDir/extracted")
                    dir.mkdir()
                    fileManager.splitFiles(it.filePath, dir.absolutePath, logger)
                }
                .flattenAsObservable{ items -> items }
                .map {
                    reportManager.filesDetails(it)
                    emailManager.execute(logger, it.path, it.order.toString() + ".zip", configuration.emailSubject + " - ${it.order}")
                }
                .doOnComplete {
                    reportManager.footerDetails(logger)
                    reportManager.printReport()
                    logger.closeLog()
                }
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.newThread())
                .subscribe({}, {
                    logger.log("Erro: " + it.stackTrace.toString())
                })


        // unzip o arquivo
        // separar os arquivos por pasta e renomear
        // zipar cada pasta
        // manda os emails
        // prepara e envia o relatorio
    }
}

data class EmailAttachment(
        val order: Int,
        val absolutePath: String
)