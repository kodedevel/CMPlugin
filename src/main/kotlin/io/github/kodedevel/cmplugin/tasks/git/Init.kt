package io.github.kodedevel.cmplugin.tasks.git

import io.github.kodedevel.cmplugin.CMPExtension
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class Init : Git() {

    @TaskAction
    override fun run() {
        super.run()
        val repoDir = File(repoDir.get())
        if (!repoDir.exists())
            repoDir.mkdir()
    }

    override fun config(extension: CMPExtension) {
        super.config(extension)
        args("init", "-b", "main")
    }

    companion object{
        const val NAME = "initGit"
    }
}