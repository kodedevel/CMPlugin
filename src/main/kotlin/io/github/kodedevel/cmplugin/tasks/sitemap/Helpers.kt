package io.github.kodedevel.cmplugin.tasks.sitemap

import java.io.File

internal fun isHTML(file: File): Boolean = file.extension == "html" || file.extension == "htm"

internal fun isXML(file: File): Boolean = file.extension == "xml"

internal fun isGitProject(root: File): Boolean{

    val rootFiles = root.listFiles()

    for (file in rootFiles)
        if (file.name == ".git")
            return true


    return false
}



internal fun getURL(file: File, workspaceDir: File, baseURL: String): String {
    val abstractMap = file.relativeTo(workspaceDir)
    val loc = "$baseURL/$abstractMap"
    return loc
}
