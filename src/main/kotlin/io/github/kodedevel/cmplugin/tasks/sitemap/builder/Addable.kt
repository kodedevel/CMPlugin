package io.github.kodedevel.cmplugin.tasks.sitemap.builder

interface Addable {

    fun addLoc(loc: String)

    fun addLastMod(modificationDate: String)

    fun addLastMod(modificationDate: Long)
}