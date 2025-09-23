package io.github.kodedevel.cmplugin.tasks.git

import io.github.kodedevel.cmplugin.CMPExtension
import io.github.kodedevel.cmplugin.tasks.Configurable
import io.github.kodedevel.cmplugin.tasks.TaskRunnable
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import java.io.ByteArrayOutputStream
import java.io.File

abstract class Git : Exec(), Configurable, TaskRunnable {

    @get:Optional
    @get:Input
    internal abstract val repoDir: Property<String>

    @get:Input
    internal abstract val username: Property<String?>

    @get:Input
    internal abstract val origin: Property<String>

    @TaskAction
    override fun run() {
        printResult()
    }

    internal fun printResult() {
        standardOutput = ByteArrayOutputStream()
        print((standardOutput as ByteArrayOutputStream).toString())
    }

    @Internal
    fun getRepoURL(): String {
        val reponame = File(repoDir.get())
        return "$GIT_URL/${username.get()}/${reponame.name}.git"
    }

    override fun config(extension: CMPExtension) {
        executable = "git"
        repoDir.set(extension.workspaceDir)
        username.set(extension.git.username)
        origin.set(extension.git.origin)

        setWorkingDir(repoDir.map { File(it) })
        isIgnoreExitValue = true
    }

    companion object {
        private const val GIT_URL = "https://github.com"
    }
}