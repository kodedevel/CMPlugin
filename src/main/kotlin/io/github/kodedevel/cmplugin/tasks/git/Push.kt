package io.github.kodedevel.cmplugin.tasks.git

import io.github.kodedevel.cmplugin.CMPExtension
import java.io.File

abstract class Push : Git() {
    override fun config(extension: CMPExtension) {
        super.config(extension)

        description = "Pushing contents to github"

        doFirst {

            var accessToken = project.providers.environmentVariable(extension.git.accessTokenSystemVarName).orNull

            if (accessToken == null){
                println("No environment variable for access token u can enter your access token here:")
                accessToken = readLine()
            }

            val repo = File(repoDirPath.get()).name

            val remote =
                "https://${username.get()}:$accessToken@github.com/${username.get()}/$repo"

            args("push", "-u", remote, "HEAD")
        }

    }

    companion object {
        const val NAME = "push"
    }

}