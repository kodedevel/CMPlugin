package io.github.kodedevel.cmplugin.tasks.git

import io.github.kodedevel.cmplugin.CMPExtension


abstract class Add : Git() {

    override fun config(extension: CMPExtension) {
        super.config(extension)
        args("add", ".")
    }

    companion object {
        const val NAME = "add"
    }
}