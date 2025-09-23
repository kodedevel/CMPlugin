package io.github.kodedevel.cmplugin.tasks.sitemap

import io.github.kodedevel.cmplugin.CMPExtension
import io.github.kodedevel.cmplugin.tasks.Configurable
import io.github.kodedevel.cmplugin.tasks.TaskRunnable
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.process.ExecOperations
import java.io.ByteArrayOutputStream
import java.io.File
import javax.inject.Inject

abstract class BaseSitemapTask: DefaultTask(), Configurable, TaskRunnable {

    @get:Input
    abstract val baseURL: Property<String>

    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val workspaceDir: DirectoryProperty

    @get: Inject
    abstract val cliOperations: ExecOperations

    @get:Internal
    private val cliOperationsResult: ByteArrayOutputStream = ByteArrayOutputStream()


    internal abstract fun create()

    internal fun getLastModifiedDate(file: File) =
        if (isGitProject(workspaceDir.asFile.get())) {
            getLastModifiedFromGitCommit(file)
        }else{
            file.lastModified()
        }

    private fun getLastModifiedFromGitCommit(file: File): String {
        cliOperationsResult.reset()
        cliOperations.exec {
            standardOutput = cliOperationsResult
            workingDir = file.parentFile
            executable = "git"
            args(
                "log", "--format=\"%ci\"", "-1", file.name
            )
        }

        return cliOperationsResult.toString()
    }


    override fun config(extension: CMPExtension) {
        baseURL.set(extension.baseURL.map { it })
        workspaceDir.set(project.layout.dir(extension.workspaceDir.map { File(it) }))
    }
}