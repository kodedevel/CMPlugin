package io.github.kodedevel.cmplugin.tasks.git

import io.github.kodedevel.cmplugin.CMPExtension
import java.io.File

abstract class Push : Git() {

    override fun config(extension: CMPExtension) {
        super.config(extension)

        doFirst {
            println("Enter git access token: ")
            val accessToken = readLine()

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