package io.github.kodedevel.cmplugin.tasks.git

import io.github.kodedevel.cmplugin.CMPExtension
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input

abstract class Commit : Git() {

    @get:Input
    abstract val commitMessage: Property<String>

    override fun config(extension: CMPExtension) {
        super.config(extension)
        commitMessage.set(extension.git.commitMessage)
        args("commit", "-m", commitMessage.get())
    }

    companion object {
        const val NAME = "commit"
    }
}