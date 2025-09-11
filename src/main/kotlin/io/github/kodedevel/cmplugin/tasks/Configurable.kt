package io.github.kodedevel.cmplugin.tasks

import io.github.kodedevel.cmplugin.CMPExtension

interface Configurable {

    fun config(extension: CMPExtension)
}