package com.company.view

import com.company.configuration.RocketConfiguration
import com.company.view.custom.ZipFilePicker
import java.awt.Container
import java.awt.Dimension
import java.awt.Font
import java.awt.Point
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.*


private const val START_X_FIRST_COLUMN = 50
internal class MyFrame : JFrame(), ActionListener {
    private var zipFilePicker: ZipFilePicker
    private var startProcessButton: JButton
    private var sentEmailTextField: JTextField
    private val rocketConfiguration = RocketConfiguration.instance()

    // Components of the Form
    private val c: Container
    private val title: JLabel

    init {
        setTitle("Rocket")
        setBounds(300, 90, 560, 600)
        defaultCloseOperation = EXIT_ON_CLOSE
        isResizable = false
        c = contentPane
        c.layout = null

        // TITLE
        title = JLabel("Rocket")
        title.font = Font("Arial", Font.PLAIN, 40)
        title.setSize(300, 30)
        title.setLocation(220, 30)
        c.add(title)

        // Endereço do arquivo
        JLabel("Endereço do arquivo .zip:").apply {
            font = Font("Arial", Font.PLAIN, 14)
            size = Dimension(300, 20)
            location = Point(START_X_FIRST_COLUMN, 100)
        }.also {
            c.add(it)
        }

        zipFilePicker = ZipFilePicker().addToView(c)


        JLabel("Remover do nome dos arquivos:").apply {
            font = Font("Arial", Font.PLAIN, 14)
            size = Dimension(300, 20)
            location = Point(START_X_FIRST_COLUMN, 130)
        }.also {
            c.add(it)
        }

        val removeTextFromFilename = JTextField().apply {
            font = Font("Arial", Font.PLAIN, 15)
            size = Dimension(190, 20)
            location = Point(320, 130)
            text = rocketConfiguration.textToRemoveFilename
        }.also {
            c.add(it)
        }

        JLabel("E-mail destinatário:").apply {
            font = Font("Arial", Font.PLAIN, 14)
            size = Dimension(300, 20)
            location = Point(START_X_FIRST_COLUMN, 160)
        }.also {
            c.add(it)
        }

        sentEmailTextField = JTextField().apply {
            font = Font("Arial", Font.PLAIN, 15)
            size = Dimension(190, 20)
            location = Point(320, 160)
            text = rocketConfiguration.emailAddressRecipient
        }.also {
            c.add(it)
        }

        JLabel("Assunto do e-mail").apply {
            font = Font("Arial", Font.PLAIN, 14)
            size = Dimension(300, 20)
            location = Point(START_X_FIRST_COLUMN, 190)
        }.also {
            c.add(it)
        }

        val emailSubjectTextField = JTextField().apply {
            font = Font("Arial", Font.PLAIN, 15)
            size = Dimension(190, 20)
            location = Point(320, 190)
            text = rocketConfiguration.emailSubject
        }.also {
            c.add(it)
        }

        JLabel("Quantidade de arquivos por pacote").apply {
            font = Font("Arial", Font.PLAIN, 14)
            size = Dimension(300, 20)
            location = Point(START_X_FIRST_COLUMN, 220)
        }.also {
            c.add(it)
        }

        val quantityFilesTextField = JSpinner(SpinnerNumberModel(50, 1, 500, 1))
                .apply {
                    font = Font("Arial", Font.PLAIN, 15)
                    size = Dimension(190, 20)
                    location = Point(320, 220)
                    value = rocketConfiguration.quantityFilesPerPackage
                }.also {
                    c.add(it)
                }

        // Deletar os pacotes após finalizar
        val checkDeleteFiles = JCheckBox("Deletar os pacotes após finalizar")
                .apply {
                    font = Font("Arial", Font.PLAIN, 15)
                    setSize(250, 20)
                    setLocation(40, 270)
                }.also {
                    c.add(it)
                }.addItemListener {
                    RocketConfiguration
                            .instance()
                            .updateShouldDeleteFilesAfterProcess(it.stateChange == 1)
                }

        // Deletar os pacotes após finalizar
        val isSendEmail = JCheckBox("Enviar o email após o término")
                .apply {
                    font = Font("Arial", Font.PLAIN, 15)
                    setSize(250, 20)
                    setLocation(40, 300)
                }.also {
                    c.add(it)
                }
                .addItemListener {
                    rocketConfiguration
                            .updateShouldDeleteFilesAfterProcess(it.stateChange == 1)
                    if (it.stateChange == 1 && sentEmailTextField.text.isEmpty()) {
                        JOptionPane.showMessageDialog(c, "Verifique o endereço de e-mail no campo E-mail destinatário\nOs arquivos serão enviados para o e-mail informado.")
                    }
                }

        val isSendReport = JCheckBox("Enviar relatório após o término")
                .apply {
                    font = Font("Arial", Font.PLAIN, 15)
                    setSize(250, 20)
                    setLocation(40, 330)
                }.also {
                    c.add(it)
                }
                .addItemListener {
//                    RocketConfiguration
//                            .instance()
//                            .updateShouldDeleteFilesAfterProcess(it.stateChange == 1)
                }

        startProcessButton = JButton("Iniciar")
                .apply {
                    font = Font("Arial", Font.PLAIN, 14)
                    size = Dimension(200, 35)
                    location = Point(180, 400)
                } .also {
                    c.add(it)
                    it.addActionListener {
                        RocketConfiguration
                                .instance()
                                .updateEmailSubject(emailSubjectTextField.text)
                                .updateEmailAddressRecipient(sentEmailTextField.text)
                                .updateTextToRemoveFileName(removeTextFromFilename.text)
                                .updateQuantityFilesPerPackage(quantityFilesTextField.value as Int)

                    }
                }

        isVisible = true
    }

    override fun actionPerformed(e: ActionEvent?) {

    }
}

// Driver Code
internal object Registration {
    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val f = MyFrame()
    }
}