package io.github.kodedevel.cmplugin.tasks.sitemap

import io.github.kodedevel.cmplugin.CMPExtension
import io.github.kodedevel.cmplugin.tasks.Configurable
import io.github.kodedevel.cmplugin.tasks.TaskRunnable
import io.github.kodedevel.cmplugin.tasks.sitemap.builder.Sitemap
import io.github.kodedevel.cmplugin.tasks.sitemap.builder.SitemapIndex
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

abstract class CreateSitemap : DefaultTask(), Configurable, TaskRunnable {

    @get:Input
    abstract val baseURL: Property<String>


    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val rootHTMLDirectory: DirectoryProperty

    @get:Input
    @get:Optional
    abstract val categories: Property<Array<String>?>

    @get:InputFile
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val homePageFile: RegularFileProperty


    @get:Input
    @get:Optional
    abstract val exclude: Property<Array<String>?>

    @get:Input
    @get:Optional
    abstract val priority: Property<Float?>


    @get:Input
    @get:Optional
    abstract val changeFreq: Property<String?>


    @Internal
    lateinit var sitemapFile: File

    @Internal
    lateinit var sitemapIndexFile: File


    @TaskAction
    override fun run() {
        if (categories.orNull == null) {
            createSitemap(CATEGORY_ALL)
        } else {
            createSitemapByCategories()
            createSitemapIndex()
        }
    }

    private fun createSitemap(category: String) {

        val sitemapBuilder = Sitemap.Builder()

        sitemapFile = rootHTMLDirectory.file("sitemap_$category.xml").get().asFile

        rootHTMLDirectory.asFileTree.forEach { file ->
            if (isHTML(file)) {

                val isCategoryMatches = isCategoryMatches(category, file)

                if ((isCategoryMatches || category == CATEGORY_ALL)) {

                    if (exclude.orNull == null || exclude.get().isEmpty()) {
                        sitemapBuilder.addURL(
                            loc = getURL(file),
                            lastmod = file.lastModified(),
                            changeFreq = changeFreq.orNull,
                            priority = priority.orNull
                        )
                    } else {

                        if (!isExcluded(file))
                            sitemapBuilder.addURL(
                                loc = getURL(file),
                                lastmod = file.lastModified(),
                                changeFreq = changeFreq.orNull,
                                priority = priority.orNull
                            )
                    }
                }
            }
        }

        val sitemap = sitemapBuilder.build()

        val writer = BufferedWriter(FileWriter(sitemapFile))
        writer.write(sitemap.map)
        writer.close()
    }

    private fun createSitemapByCategories() {
        for (category in categories.get()) {
            createSitemap(category)
        }

        createSitemapForHomePage()
    }


    private fun createSitemapForHomePage() {
        sitemapFile = rootHTMLDirectory.file("sitemap_home.xml").get().asFile

        val sitemapBuilder = Sitemap.Builder()
        //When category is set to all homepage will be added to single file automatically
        val file = homePageFile.asFile.get()
        sitemapBuilder.addURL(
            loc = getURL(file),
            file.lastModified(),
            priority.orNull,
            changeFreq.orNull
        )

        val sitemap = sitemapBuilder.build()

        val writer = BufferedWriter(FileWriter(sitemapFile))
        writer.write(sitemap.map)
        writer.close()
    }

    private fun createSitemapIndex() {

        val sitemapIndexBuilder = SitemapIndex.Builder()

        sitemapIndexFile = rootHTMLDirectory.file(SITEMAP_INDEX_NAME).get().asFile

        val root = rootHTMLDirectory.asFile.get()

        for (file in root.listFiles()) {
            if (isXML(file) && file.name != SITEMAP_INDEX_NAME) {
                sitemapIndexBuilder.addSitemap(getURL(file), file.lastModified())
            }
        }

        val sitemapIndex = sitemapIndexBuilder.build()

        val writer = BufferedWriter(FileWriter(sitemapIndexFile))
        writer.write(sitemapIndex.map)
        writer.close()
    }

    private fun getURL(file: File): String {
        val abstractMap = getRelativePath(file)
        val loc = baseURL.map { "$it/$abstractMap" }.get()

        return loc
    }

    private fun isXML(file: File): Boolean = file.extension == "xml"

    private fun getRelativePath(file: File): String = file.relativeTo(rootHTMLDirectory.asFile.get()).path

    private fun isHTML(file: File): Boolean = file.extension == "html" || file.extension == "htm"

    private fun isExcluded(file: File): Boolean {

        for (name in exclude.get()) {
            val regex = Regex("\\b$name\\b(\\.html|\\.htm)?")

            if (regex in file.name)
                return true
        }

        return false
    }

    private fun isIndexPage(file: File) = file.name == "index.html"

    private fun isCategoryMatches(category: String, file: File): Boolean {
        val relativeFile = file.relativeTo(rootHTMLDirectory.asFile.get())

        val regex = Regex("(/)?\\b($category)\\b(/)?")

        return regex in relativeFile.path
    }


    override fun config(extension: CMPExtension) {
        baseURL.set(extension.baseURL.map { it })

        rootHTMLDirectory.set(project.layout.dir(extension.projectDir.map { File(it) }))

        val sitemap = extension.sitemap

        homePageFile.set(rootHTMLDirectory.file(sitemap.homePage))

        categories.set(sitemap.categories.map { it })
        exclude.set(sitemap.exclude)
        priority.set(sitemap.priority)
        changeFreq.set(sitemap.changeFreq.map { it.v })
    }

    companion object {
        const val NAME = "createSitemap"
        private const val CATEGORY_ALL = "all"
        private const val SITEMAP_INDEX_NAME = "sitemap_index.xml"
    }
}