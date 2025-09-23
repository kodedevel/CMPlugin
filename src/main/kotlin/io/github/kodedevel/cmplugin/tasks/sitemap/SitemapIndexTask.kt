package io.github.kodedevel.cmplugin.tasks.sitemap

import io.github.kodedevel.cmplugin.CMPExtension
import io.github.kodedevel.cmplugin.tasks.sitemap.builder.SitemapIndex
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

abstract class SitemapIndexTask: BaseSitemapTask() {

    @get:Input
    abstract val categories: Property<Array<String>?>

    @Internal
    lateinit var sitemapIndexFile: File

    @TaskAction
    override fun run() {
        if (categories.orNull != null && categories.get().isNotEmpty()) {
            create()
        }else {
            println("It's probably you have single sitemap and u don't need sitemapindex u can index it directly to GSC.")
        }
    }

    override fun create() {

        val sitemapIndex = SitemapIndex()

        sitemapIndexFile = workspaceDir.file(SITEMAP_INDEX_FILE_NAME).get().asFile

        val root = workspaceDir.asFile.get()

        for (file in root.listFiles()) {
            if (isXML(file) && file.name != SITEMAP_INDEX_FILE_NAME) {
                val lastModified: Any = getLastModifiedDate(file)
                sitemapIndex.addSitemap(getURL(file, workspaceDir.asFile.get(), baseURL.get()), lastModified)
            }
        }

        val writer = BufferedWriter(FileWriter(sitemapIndexFile))
        writer.write(sitemapIndex.getSitemapIndex())
        writer.close()
    }


    override fun config(extension: CMPExtension) {
        super.config(extension)
        categories.set(extension.sitemap.categories)
    }

    companion object{
        const val NAME = "createSitemapIndex"
        private const val SITEMAP_INDEX_FILE_NAME = "sitemap_index.xml"
    }
}