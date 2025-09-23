package io.github.kodedevel.cmplugin.tasks.backup

import io.github.kodedevel.cmplugin.CMPExtension
import io.github.kodedevel.cmplugin.tasks.Configurable
import io.github.kodedevel.cmplugin.tasks.TaskRunnable
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.*
import org.gradle.api.tasks.bundling.Zip
import java.io.File

abstract class CreateBackup : Zip(), Configurable, TaskRunnable {

    @get:InputDirectory
    @get: PathSensitive(PathSensitivity.RELATIVE)
    val projectDir: DirectoryProperty = project.objects.directoryProperty()

    @get:OutputDirectory
    abstract val backupDir: DirectoryProperty

    @TaskAction
    override fun run() {
        if (!backupDir.asFile.get().exists())
            backupDir.asFile.get().mkdirs()
    }

    override fun config(extension: CMPExtension) {
        projectDir.set(project.layout.dir(extension.workspaceDir.map { File(it) }))
        backupDir.set(project.layout.dir(extension.backupDir.map { File(it) }))
        from(projectDir)
        destinationDirectory.set(backupDir.get())
        archiveFileName.set("${projectDir.get().asFile.name}.zip")
    }

}