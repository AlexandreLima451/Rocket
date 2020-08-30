package com.company.view.custom

import com.company.configuration.RocketConfiguration
import java.awt.Container
import java.awt.Dimension
import java.awt.Font
import java.awt.Point
import java.io.File
import javax.swing.JButton
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter
import javax.swing.filechooser.FileSystemView

class ZipFilePicker {

    private lateinit var filePicker: JFileChooser
    private lateinit var filePickerButton: JButton

    fun addToView(container: Container): ZipFilePicker {
        filePickerButton = JButton("Selecionar arquivo")
                .apply {
                    font = Font("Arial", Font.PLAIN, 15)
                    size = Dimension(190, 30)
                    location = Point(320, 95)
                }.also {
                    container.add(it)
                }

        filePicker = JFileChooser(FileSystemView.getFileSystemView().homeDirectory)
                .apply {
                    font = Font("Arial", Font.PLAIN, 15)
                    size = Dimension(190, 30)
                    location = Point(320, 95)
                }
        val xmlfilter = FileNameExtensionFilter("arquivos zip (*.zip)", "zip")
        filePicker.fileFilter = xmlfilter
        filePickerButton.addActionListener {
            filePicker.showOpenDialog(container).also {
                if (it == JFileChooser.APPROVE_OPTION) {
                    val file: File = filePicker.selectedFile
                    filePickerButton.text = file.name
                    saveFilePathConfig(file.absolutePath)
                }
            }
        }

        return this
    }

    private fun saveFilePathConfig(path: String) {
        RocketConfiguration.instance().updateZipFilePath(path)
    }
}