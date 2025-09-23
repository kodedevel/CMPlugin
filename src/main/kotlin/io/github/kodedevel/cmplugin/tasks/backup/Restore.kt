package io.github.kodedevel.cmplugin.tasks.backup

import io.github.kodedevel.cmplugin.CMPExtension
import io.github.kodedevel.cmplugin.tasks.Configurable
import io.github.kodedevel.cmplugin.tasks.TaskRunnable
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.*
import java.io.File

abstract class Restore : Copy(), Configurable, TaskRunnable {

    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val backupDir: DirectoryProperty


    @get:InputFile
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val backupFile: RegularFileProperty

    @get:OutputDirectory
    abstract val workspaceDir: DirectoryProperty


    @TaskAction
    override fun run() {
        val targetDir = workspaceDir.asFile.get()
        val backupFile = backupDir.file("${workspaceDir.asFile.get().name}.zip").get().asFile

        if (!backupFile.exists()) {
            println("No backup")
            return
        }

        val result = targetDir.deleteRecursively() || !targetDir.exists()
        println("print $result")
        if (result) {
            workspaceDir.asFile.get().mkdirs()
        }
    }


    override fun config(extension: CMPExtension) {
        backupDir.set(project.layout.dir(extension.backupDir.map { File(it) }))

        workspaceDir.set(project.layout.dir(extension.workspaceDir.map { File(it) }))


        backupFile.set(backupDir.file("${workspaceDir.asFile.get().name}.zip"))

        from(project.zipTree(backupFile))
        into(workspaceDir)
    }
}