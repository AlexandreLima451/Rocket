package com.company.util

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDateTime
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream


class FileManager {

    /**
     * method that moves a file to another directory
     * @param mSourcePath is the source's path + name of the file which will be moved.
     * @param mDestPath is the destiny's path + new name.
     * @return true if file was moved successfully
     */
    private fun moveTo(mSourcePath: String, mDestPath: String): Boolean {
        return File(mSourcePath).renameTo(File(mDestPath))
    }

    /**
     * method that creates a new directory
     * @param mPath is the final folder where the folder will be created
     * @param mNewFolderName is the name of the new folder
     * @return the absolute path of the new folder if it was created
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
     * method that deletes the folder and all files inside of it
     * @param mFolderPath is the folder that will be deleted
     * @return the final result of the process
     */
    private fun deleteFolder(mFolderPath : String) : String{
        val folder = File(mFolderPath)

        if(!folder.isDirectory){
            return "The source path $mFolderPath is not a directory!"
        }

        folder.listFiles()?.forEach { file ->
            file.delete()
        }

        folder.delete()

        return MESSAGE_SUCCESS
    }

    /**
     * method that compacts the folder to a .zip extension
     * @param mSourcePath is the folder that will be compacted
     * @param mDestPath is the final folder where the .zip folder will be found
     * @param mZipFileName is the name of .zip file
     * @return the final result of the process
     */
    private fun compactFolder(mSourcePath: String, mDestPath: String, mZipFileName : String) : String{

        val buffer = ByteArray(1024)
        val sourceDirectory = File(mSourcePath)
        val zipFileNameWithoutExtension = mZipFileName.replace(".zip", "")

        if (!sourceDirectory.isDirectory){
            return "The source path $mSourcePath is not a directory!"
        }

        val fileOut = FileOutputStream("$mDestPath/$zipFileNameWithoutExtension.zip")
        val zipOut = ZipOutputStream(fileOut)

        try {

            sourceDirectory.listFiles()?.forEach { file ->
                zipOut.putNextEntry(ZipEntry(file.name))
                val inputFile = FileInputStream(mSourcePath + File.separator + file.name)
                try {

                    var len: Int
                    while (inputFile.read(buffer).also { len = it } > 0) {
                        zipOut.write(buffer, 0, len)
                    }
                } catch (exInput: IOException) {
                    exInput.printStackTrace()
                } finally {
                    inputFile.close()
                }
            }


            zipOut.closeEntry()

        }catch (ioException : IOException){
            ioException.printStackTrace()
        }finally {
            try {
                zipOut.close()
            }catch (ex : IOException){
                ex.printStackTrace()
            }
        }
        return MESSAGE_SUCCESS
    }

    /**
     * the method that splits all files according the parameters
     * @param mSourcePath is the source's path of the files that will be split.
     * @param mDestPath is the destiny's path that all folders .
     * @param mFileExtension is the extension of the files that will be split
     * @param mRemoveFromName is the text that will be removed from original files' name
     * @param mQuantityOfFilesByFolder is the number of files by folder
     * @param mIsZipFolder indicates if folder will be zipped or not
     * @return the result of process
     */
    fun splitFiles(mSourcePath: String,
                   mDestPath: String,
                   mFileExtension: String?,
                   mRemoveFromName: String?,
                   mQuantityOfFilesByFolder: Int,
                   mIsZipFolder : Boolean): String {
        val folder = File(mSourcePath.trim())
        val files = folder.listFiles() ?: return MESSAGE_NOT_FOUND
        var count = 0
        val rootFolderName = "rocket " + LocalDateTime.now()
        var currentFolderName = DEFAULT_FOLDER_NAME

        val destinyFolder = createFolder(mPath = mDestPath, mNewFolderName = rootFolderName)

        var destPathFolder = createFolder(destinyFolder.trim(), currentFolderName)
        for (file in files) {
            val fileName = file.name.replace(mRemoveFromName!!, "")
            val destFilePath = destPathFolder + File.separator + fileName
            if (file.name.contains(mFileExtension!!)) {
                val wasFileMoved = moveTo(file.absolutePath, destFilePath)
                if (wasFileMoved) {
                    count++
                    if (count >= mQuantityOfFilesByFolder) {

                        currentFolderName = (currentFolderName.toInt() + 1).toString()
                        destPathFolder = createFolder(destinyFolder.trim(), currentFolderName)
                        count = 0
                    }
                } else {
                    return MESSAGE_ERROR_MOVING_FILE
                }
            }
        }

        if(mIsZipFolder){
            File(destinyFolder).listFiles()?.forEach {
                if(it.isDirectory){
                    compactFolder(mSourcePath = it.absolutePath, mDestPath = destinyFolder, mZipFileName = it.name)
                    deleteFolder(mFolderPath = it.absolutePath)
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