package io.github.kodedevel.cmplugin.tasks.sitemap

import io.github.kodedevel.cmplugin.CMPExtension
import io.github.kodedevel.cmplugin.tasks.sitemap.builder.Sitemap
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

abstract class SitemapTask : BaseSitemapTask() {

    @get:InputFile
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val homePageFile: RegularFileProperty

    @get:Input
    @get:Optional
    abstract val exclude: Property<Array<String>?>

    @get:Input
    @get:Optional
    abstract val categories: Property<Array<String>?>

    @get:Input
    @get:Optional
    abstract val priority: Property<Float?>

    @get:Input
    @get:Optional
    abstract val changeFreq: Property<String?>

    @Internal
    lateinit var sitemapFile: File

    @TaskAction
    override fun run() {

        if (categories.orNull == null) {
            create(CATEGORY_ALL)
        } else {
            create()
        }
    }

    fun create(category: String) {
        val sitemap = Sitemap()

        sitemapFile = workspaceDir.file("sitemap_$category.xml").get().asFile

        workspaceDir.asFileTree.forEach { file ->

            if (isHTML(file)) {

                val lastModified: Any = getLastModifiedDate(file)

                val isCategoryMatches = isCategoryMatches(category, file)

                if ((isCategoryMatches || category == CATEGORY_ALL)) {

                    if (exclude.orNull == null || exclude.get().isEmpty()) {
                        sitemap.addURL(
                            loc = getURL(file, workspaceDir.asFile.get(), baseURL.get()),
                            lastmod = lastModified,
                            changeFreq = changeFreq.orNull,
                            priority = priority.orNull
                        )

                    } else {

                        if (!isExcluded(file))
                            sitemap.addURL(
                                loc = getURL(file, workspaceDir.asFile.get(), baseURL.get()),
                                lastmod = lastModified,
                                changeFreq = changeFreq.orNull,
                                priority = priority.orNull
                            )
                    }
                }
            }
        }

        val writer = BufferedWriter(FileWriter(sitemapFile))
        writer.write(sitemap.getSitemap())
        writer.close()
    }

    override fun create() {
        for (category in categories.get()) {
            create(category)
        }
        createForHomePage()
    }

    private fun createForHomePage() {
        sitemapFile = workspaceDir.file(SITEMAP_HOME_FILE_NAME).get().asFile

        val sitemap = Sitemap()

        //When category is set to all homepage will be added to single file automatically
        val file = homePageFile.asFile.get()
        val lastModified = getLastModifiedDate(file)
        sitemap.addURL(
            loc = getURL(file, workspaceDir.asFile.get(), baseURL.get()),
            lastModified,
            priority.orNull,
            changeFreq.orNull
        )

        val writer = BufferedWriter(FileWriter(sitemapFile))
        writer.write(sitemap.getSitemap())
        writer.close()
    }

    private fun isExcluded(file: File): Boolean {
        val exclude by exclude
        for (name in exclude!!) {
            val regex = Regex("\\b$name\\b(\\.html|\\.htm)?")

            if (regex in file.name)
                return true
        }

        return false
    }

    private fun isCategoryMatches(category: String, file: File): Boolean {
        val relativeFile = file.relativeTo(workspaceDir.asFile.get())

        val regex = Regex("(/)?\\b($category)\\b(/)?")

        return regex in relativeFile.path
    }

    override fun config(extension: CMPExtension) {
        super.config(extension)

        val sitemap = extension.sitemap

        homePageFile.set(workspaceDir.file(sitemap.homePage))

        categories.set(sitemap.categories.map { it })
        exclude.set(sitemap.exclude)
        priority.set(sitemap.priority)
        changeFreq.set(sitemap.changeFreq.map { it.v })
    }

    companion object {
        const val NAME = "createSitemaps"
        private const val SITEMAP_HOME_FILE_NAME = "sitemap_home.xml"
        private const val CATEGORY_ALL = "all"

    }

}



