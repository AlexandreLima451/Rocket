package com.company.util

import com.company.configuration.RocketConfiguration
import java.util.*
import javax.activation.DataHandler
import javax.activation.FileDataSource
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

class EmailManager {

    fun execute(logger: Logger, filepath: String, fileName: String, emailSubject: String) {
        logger.log("\nPreparando e enviando o e-mail: $emailSubject")
        val configuration = RocketConfiguration.instance()

        val props = Properties().apply {
            put("mail.smtp.host", "smtp.gmail.com");
            put("mail.smtp.socketFactory.port", "465");
            put("mail.smtp.socketFactory.class",
                    "javax.net.ssl.SSLSocketFactory");
            put("mail.smtp.auth", "true");
            put("mail.smtp.port", "465");
        }

        val session = Session.getDefaultInstance(props,
                object : Authenticator() {
                    override fun getPasswordAuthentication(): PasswordAuthentication {
                        return PasswordAuthentication(configuration.emailAddressSender,
                                CryptoManager.Decripto(configuration.passwordEmailSender))
                    }
                })

        try {

            val message = MimeMessage(session);
            message.setFrom(InternetAddress(configuration.emailAddressSender));

            //Remetente
            val toUser = InternetAddress //Destinatário(s)
                    .parse(configuration.emailAddressRecipient);

            message.setRecipients(Message.RecipientType.TO, toUser);
            message.setSubject(emailSubject)
            message.setText("");
            /**Método para enviar a mensagem criada*/

            val source = FileDataSource(filepath)
            val multipart: Multipart = MimeMultipart()
            val messageBodyPart: BodyPart = MimeBodyPart()
            messageBodyPart.setText("")
            multipart.addBodyPart(messageBodyPart)
            messageBodyPart.setDataHandler(DataHandler(source))
            messageBodyPart.setFileName(fileName)

            // Send the complete message parts

            // Send the complete message parts
            message.setContent(multipart)
            Transport.send(message)

            print("Feito!!! " + fileName)
            logger.log("E-mail $emailSubject - Enviado. \n")

        } catch (e: MessagingException) {
            throw RuntimeException(e)
        }
    }

    fun sendReport(report: String) {
        val configuration = RocketConfiguration.instance()

        val props = Properties().apply {
            put("mail.smtp.host", "smtp.gmail.com");
            put("mail.smtp.socketFactory.port", "465");
            put("mail.smtp.socketFactory.class",
                    "javax.net.ssl.SSLSocketFactory");
            put("mail.smtp.auth", "true");
            put("mail.smtp.port", "465");
        }

        val session = Session.getDefaultInstance(props,
                object : Authenticator() {
                    override fun getPasswordAuthentication(): PasswordAuthentication {
                        return PasswordAuthentication(configuration.emailAddressSender,
                                CryptoManager.Decripto(configuration.passwordEmailSender))
                    }
                })

        try {

            val message = MimeMessage(session);
            message.setFrom(InternetAddress(configuration.emailAddressSender));

            //Remetente
            val toUser = InternetAddress //Destinatário(s)
                    .parse(configuration.emailAddressSender);

            message.setRecipients(Message.RecipientType.TO, toUser);
            message.setSubject("Relatório Rocket - ${configuration.emailSubject}")
            message.setText(report);
            /**Método para enviar a mensagem criada*/

            val messageBodyPart: BodyPart = MimeBodyPart()
            messageBodyPart.setText(report)
            Transport.send(message)
        } catch (e: MessagingException) {
            throw RuntimeException(e)
        }
    }
}