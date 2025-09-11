package io.github.kodedevel.cmplugin.tasks.git

import io.github.kodedevel.cmplugin.CMPExtension

abstract class Pull : Git() {

    override fun config(extension: CMPExtension) {
        super.config(extension)

        args("pull", "origin", origin.get())
    }


    companion object {
        const val NAME = "pull"
    }
}