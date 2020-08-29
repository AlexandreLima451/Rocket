package com.company.util

import java.io.File

class FileManager {

    /**
     * method that moves a file to another directory
     */
    private fun moveTo(mSourcePath: String, mDestPath: String): Boolean {
        return File(mSourcePath).renameTo(File(mDestPath))
    }

    /**
     * method that creates a new directory
     */
    private fun createFolder(mPath: String, mNewFolderName: String): String {
        val completePath = "$mPath/$mNewFolderName"
        val isFolderCreated = File(completePath).mkdir()
        return if (isFolderCreated) {
            completePath
        } else {
            ""
        }
    }

    /**
     * main method that splits all files according the parameters
     */
    fun splitFiles(mSourcePath: String, mDestPath: String, mFileExtension: String?, mRemoveFromName: String?, mQuantityOfFilesByFolder: Int): String {
        val folder = File(mSourcePath)
        val files = folder.listFiles() ?: return MESSAGE_NOT_FOUND
        var count = 0
        var currentFolderName = DEFAULT_FOLDER_NAME
        var destPathFolder = createFolder(mDestPath, currentFolderName)
        for (file in files) {
            val destFilePath = destPathFolder + "/" + file.name.replace(mRemoveFromName!!, "")
            if (file.name.contains(mFileExtension!!)) {
                val wasFileMoved = moveTo(file.absolutePath, destFilePath)
                if (wasFileMoved) {
                    count++
                    if (count >= mQuantityOfFilesByFolder) {
                        currentFolderName = (currentFolderName.toInt() + 1).toString()
                        destPathFolder = createFolder(mDestPath, currentFolderName)
                        count = 0
                    }
                } else {
                    return MESSAGE_ERROR_MOVING_FILE
                }
            }
        }

        return MESSAGE_SUCCESS
    }

    companion object {
        private const val DEFAULT_FOLDER_NAME = "1"
        private const val MESSAGE_NOT_FOUND = "No files found"
        private const val MESSAGE_SUCCESS = "OK"
        private const val MESSAGE_ERROR_MOVING_FILE = "It is not able to move FILE to destination."
    }
}