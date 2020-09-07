package com.company.util

import java.util.*
import javax.activation.DataHandler
import javax.activation.FileDataSource
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

class EmailManager {

    fun execute(filepath: String, fileName: String, emailSubject: String) {
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
                        return PasswordAuthentication("lx.felipemakimen@gmail.com",
                                "Firefire1207")
                    }
                })

        try {

            val message = MimeMessage(session);
            message.setFrom(InternetAddress("lx.felipemakimen@gmail.com"));

            //Remetente
            val toUser = InternetAddress //Destinatário(s)
                    .parse("felipemakinen@hotmail.com, lx.felipemakimen@gmail.com");

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
            multipart.addBodyPart(messageBodyPart)

            // Send the complete message parts

            // Send the complete message parts
            message.setContent(multipart)
            Transport.send(message)

            System.out.println("Feito!!!")

        } catch (e: MessagingException) {
            throw RuntimeException(e)
        }
    }
}