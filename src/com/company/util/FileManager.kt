package com.company.util

import com.company.configuration.RocketConfiguration
import java.io.*
import java.time.LocalDateTime
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

/**
 * Class that is responsible to manage files
 */
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
    private fun deleteFolder(mFolderPath: String): String {
        val folder = File(mFolderPath)

        if (!folder.isDirectory) {
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
     * @return the absolute path of zip file
     */
    private fun zipFolder(mSourcePath: String, mDestPath: String, mZipFileName: String): String {

        val buffer = ByteArray(1024)
        val sourceDirectory = File(mSourcePath)
        val zipFileNameWithoutExtension = mZipFileName.replace(".zip", "")

        if (!sourceDirectory.isDirectory) {
            return "The source path $mSourcePath is not a directory!"
        }

        val absolutePathOfZip = "$mDestPath/$zipFileNameWithoutExtension.zip"

        val fileOut = FileOutputStream(absolutePathOfZip)
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

        } catch (ioException: IOException) {
            ioException.printStackTrace()
        } finally {
            try {
                zipOut.close()
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
        }
        return absolutePathOfZip
    }

    /**
     * method that extracts all files inside of a zip
     * @param mSourcePath is the folder that will be unzip
     * @param mDestPath is the final folder where the files will be found after unzip
     * @return a list of files that were extracted
     */
    fun unzipFolder(mSourcePath: String, mDestPath: String): List<FileData> {
        val buffer = 1024

        try {
            val fileInputStream = FileInputStream(mSourcePath)
            val zipInputStream = ZipInputStream(BufferedInputStream(fileInputStream))
            var zipEntry: ZipEntry?

            var fileOutputStream: FileOutputStream
            var dest: BufferedOutputStream
            var fileId = 0
            val listOfFileData = ArrayList<FileData>()

            zipEntry = zipInputStream.nextEntry

            while (zipEntry != null && zipEntry.name.contains(".xml").not()) {
                zipEntry = zipInputStream.nextEntry
            }

            if (zipEntry != null
                    && zipEntry.name.contains(".xml")
                    && zipEntry.name.contains("MAC").not()) {
                var absoluteDestPath = mDestPath + File.separator + zipEntry.name
                fileOutputStream = FileOutputStream(absoluteDestPath)
                dest = BufferedOutputStream(fileOutputStream, buffer)

                while (zipEntry?.name != null) {

                    if (zipEntry.name != null) {
                        var count: Int
                        val data = ByteArray(buffer)

                        count = zipInputStream.read(data, 0, buffer)
                        while (count > 0) {
                            dest.write(data, 0, count)
                            count -= buffer
                        }

                        dest.flush()

                        fileId++
                        listOfFileData.add(FileData(fileId, absoluteDestPath))

                        zipEntry = zipInputStream.nextEntry

                        if (zipEntry != null && zipEntry.name.contains("MAC").not()) {
                            absoluteDestPath = mDestPath + File.separator + zipEntry.name
                            fileOutputStream = FileOutputStream(absoluteDestPath)
                            dest = BufferedOutputStream(fileOutputStream, buffer)
                        }
                    } else {
                        break
                    }
                }
                dest.close()
                zipInputStream.close()
            }

            return listOfFileData
        } catch (exc: IOException) {
            exc.printStackTrace()
            return ArrayList()
        }
    }

    fun unzipFolder2(mSourcePath: String, mDestPath: String): ExtractedFileData {
        val listOfFileData = ArrayList<FileData>()
        val zipFile = net.lingala.zip4j.ZipFile(mSourcePath)
        zipFile.extractAll(mDestPath)
        val items = zipFile.fileHeaders.filter { it.fileName.contains("MAC").not() && it.fileName.contains(".xml") }

        for (i in items.indices) {
            listOfFileData.add(
                    FileData(
                            i,
                            mDestPath + File.separator + items[i].fileName
                    )
            )
        }
        return ExtractedFileData(mDestPath + File.separator, listOfFileData)
    }

    /**
     * method that list all files of a folder
     * @param mSourcePath is the folder that will scanned
     * @return a list of files
     */
    private fun listFilesByFolder(mSourcePath: String): List<String> {
        val listOfFiles = ArrayList<String>()

        return try {
            val files = File(mSourcePath)

            files.listFiles()?.forEach {
                listOfFiles.add(it.name)
            }

            listOfFiles
        } catch (exc: IOException) {
            exc.printStackTrace()
            ArrayList()
        }
    }


    /**
     * the method that splits all files according the parameters
     * @param mSourcePath is the source's path of the files that will be split.
     * @param mDestPath is the destiny's path that all folders .
     * @param mFileExtension is the extension of the files that will be split
     * @param mIsZipFolder indicates if folder will be zipped or not
     * @return a list of files;
     */
    fun splitFiles(mSourcePath: String,
                   mDestPath: String,
                   logger: Logger,
                   mFileExtension: String = ".xml",
                   mIsZipFolder: Boolean = true): List<FileData> {

        val configuration = RocketConfiguration.instance()
        val folder = File(mSourcePath.trim())
        val files = folder.listFiles() ?: return ArrayList()
        var count = 0
        val rootFolderName = "rocket " + LocalDateTime.now()
        var currentFolderName = DEFAULT_FOLDER_NAME
        val listOfFileData = ArrayList<FileData>()

        val destinyFolder = createFolder(mPath = mDestPath, mNewFolderName = rootFolderName)

        var destPathFolder = createFolder(destinyFolder.trim(), currentFolderName)
        logger.log("Separando os arquivos XML - ${configuration.quantityFilesPerPackage} em cada pasta")
        for (file in files) {
            val fileName = file.name.replace(configuration.textToRemoveFilename, "")
            val destFilePath = destPathFolder + File.separator + fileName
            if (file.name.contains(mFileExtension)) {
                val wasFileMoved = moveTo(file.absolutePath, destFilePath)
                if (wasFileMoved) {
                    count++
                    if (count >= configuration.quantityFilesPerPackage) {

                        currentFolderName = (currentFolderName.toInt() + 1).toString()
                        destPathFolder = createFolder(destinyFolder.trim(), currentFolderName)
                        count = 0
                    }
                } else {
                    return ArrayList()
                }
            }
        }

        var fileId = 0

        if (mIsZipFolder) {
            logger.log("Compactando os pacotes para enviar por e-mail")
            File(destinyFolder).listFiles()?.forEach {
                if (it.isDirectory) {
                    val listFilesByFolder = listFilesByFolder(it.absolutePath)
                    val absolutePathOfZipFile = zipFolder(mSourcePath = it.absolutePath, mDestPath = destinyFolder, mZipFileName = it.name)
                    deleteFolder(mFolderPath = it.absolutePath)
                    fileId++
                    logger.log("Pacote $fileId compactado. Salvo no diretório: $absolutePathOfZipFile")
                    listOfFileData.add(FileData(fileId, absolutePathOfZipFile, listFilesByFolder))

                }
            }
        }

        return listOfFileData
    }

    companion object {
        private const val DEFAULT_FOLDER_NAME = "1"
        private const val MESSAGE_SUCCESS = "OK"
    }
}