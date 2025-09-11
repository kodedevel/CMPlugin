package io.github.kodedevel.cmplugin.tasks.git

import io.github.kodedevel.cmplugin.CMPExtension

abstract class AddRemote : Git() {

    override fun config(extension: CMPExtension) {
        super.config(extension)
        args("remote", "add", origin.get(), getRepoURL())
    }

    companion object {
        const val NAME = "addRemote"
    }
}