package com.company.view

import com.company.util.LoginManager
import com.company.view.custom.JLabelShadow
import java.awt.Color
import java.awt.Dimension
import java.awt.Font
import java.awt.GridLayout
import javax.swing.*
import javax.swing.border.EmptyBorder


class LoginScreen : JFrame() {

    private lateinit var emailTextField: JTextField
    private lateinit var passwordTextField: JTextField
    private lateinit var loginButton: JButton

    init {
        title = "Rocket"
        setBounds(300, 90, 300, 600)
        setLocationRelativeTo(null)
        contentPane = JLabel(ImageIcon(javaClass.getResource("/image/rocket_logo.png")).apply {
            setLocationRelativeTo(null)
        })
        contentPane.layout = GridLayout(15, 1)
        (contentPane as JLabel).setBorder(BorderFactory.createEmptyBorder(0,20,0,20));
        contentPane.add(JLabel())

        contentPane.add(JLabel())
        addTitle()
        contentPane.add(JLabel())
        addEmailTextField()
        addPasswordTextField()
        contentPane.add(JLabel())
        contentPane.add(JLabel())
        addLoginButton()

        isVisible = true
    }

    private fun addEmailTextField() {
        JLabelShadow("Informe o seu e-mail:").apply {
            foreground = Color.WHITE
            horizontalAlignment = JLabel.CENTER
            border = EmptyBorder(50, 20, 50, 20)
        }.also {
            contentPane.add(it)
        }

        emailTextField = JTextField().apply {
            font = Font("Arial", Font.PLAIN, 15)
            size = Dimension(190, 20)
        }.also {
            contentPane.add(it)
        }
    }

    private fun addPasswordTextField() {
        JLabelShadow("Informe a senha:").apply {
            foreground = Color.WHITE
            horizontalAlignment = JLabel.CENTER
            border = EmptyBorder(50, 20, 50, 20)
        }.also {
            contentPane.add(it)
        }

        passwordTextField = JPasswordField().apply {
            font = Font("Arial", Font.PLAIN, 15)
            size = Dimension(190, 20)
        }.also {
            contentPane.add(it)
        }
    }

    private fun addTitle() {
        val title = JLabelShadow("Rocket").apply {
            font = Font("Arial", Font.PLAIN, 40)
            setSize(300, 30)
            horizontalAlignment = JLabel.CENTER
            foreground = Color.WHITE
        }

        title.border = EmptyBorder(40, 20, 50, 20)
        contentPane.add(title)
    }

    private fun addLoginButton() {
        loginButton = JButton("Entrar").apply {
            font = Font("Arial", Font.PLAIN, 14)
            horizontalAlignment = JLabel.CENTER
            foreground = Color.BLUE
            addActionListener {
                loginButton.text = "Entrando..."
                val result = LoginManager.instance?.auth(
                        emailTextField.text,
                        passwordTextField.text
                )

                if (result == true) {
                    isVisible = false
                    dispose()
                    MyFrame()
                } else {
                    val options = arrayOf<Any>("OK")
                    JOptionPane.showOptionDialog(contentPane,
                            "Credenciais incorretas.", "Ops",
                            JOptionPane.PLAIN_MESSAGE,
                            JOptionPane.QUESTION_MESSAGE,
                            ImageIcon("/image/rocket_logo.png"),
                            options,
                            options[0])
                    emailTextField.text = ""
                    passwordTextField.text = ""
                    loginButton.text = "Entrar"
                }
            }
        }.also { contentPane.add(it) }
    }


}