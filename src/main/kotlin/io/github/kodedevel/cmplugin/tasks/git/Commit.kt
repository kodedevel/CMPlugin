package io.github.kodedevel.cmplugin.tasks.git

import io.github.kodedevel.cmplugin.CMPExtension

abstract class Commit : Git() {

    override fun config(extension: CMPExtension) {
        super.config(extension)
        description = "Committing changes to git"

        doFirst {
            println("Enter commit message (or press enter if u want it to be default)")
            val commitMessage = readLine()
            args("commit", "-m", if (commitMessage == null || commitMessage.isEmpty()) DEFAULT_COMMIT_MESSAGE else commitMessage)
        }
    }

    companion object {
        const val NAME = "commit"
        private const val DEFAULT_COMMIT_MESSAGE = "Updated"
    }
}