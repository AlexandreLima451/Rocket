package com.company.util

data class FileData (var order : Int, var path : String, var listOfFiles : List<String> = listOf())

data class ExtractedFileData(val filePath: String, val listOfFiles: List<FileData>)