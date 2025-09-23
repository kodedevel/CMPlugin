package io.github.kodedevel.cmplugin.tasks.git

import io.github.kodedevel.cmplugin.CMPExtension

abstract class Clone : Git() {

    override fun config(extension: CMPExtension) {
        super.config(extension)
        workingDir = project.rootDir
        if (username.orNull != null)
        args("clone", getRepoURL())
    }


    companion object {
        const val NAME = "clone"
    }
}