package io.github.kodedevel.cmplugin.tasks.sitemap

import io.github.kodedevel.cmplugin.CMPExtension
import io.github.kodedevel.cmplugin.tasks.Configurable
import io.github.kodedevel.cmplugin.tasks.TaskRunnable
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class CleanSitemaps : DefaultTask(), Configurable, TaskRunnable {

    @get:InputDirectory
    abstract val rootHTMLDir: DirectoryProperty


    @TaskAction
    override fun run() {
        val root = rootHTMLDir.asFile.get()

        val regex = Regex("map_.*.xml")
        for (file in root.listFiles()) {
            if (regex in file.name)
                file.delete()
        }
    }

    override fun config(extension: CMPExtension) {
        rootHTMLDir.set(project.layout.dir(extension.workspaceDir.map { it -> File(it) }))
    }


    companion object {
        const val NAME = "cleanSitemaps"
    }
}