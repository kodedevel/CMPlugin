package io.github.kodedevel.cmplugin.tasks.sitemap.builder

import java.text.SimpleDateFormat
import java.util.*

class SitemapIndex private constructor(private val _map: String) {

    val map: String get() = _map


    class Builder {
        val map = StringBuilder(
            "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                    "<sitemapindex xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n"
        )


        fun build(): SitemapIndex {
            map.append("</sitemapindex>")
            return SitemapIndex(map.toString())
        }

        fun addSitemap(loc: String, modificationDate: Long): Builder {
            map.append(getElementWithSpace(2, "<sitemap>\n"))
            addLoc(loc)
            addLastMod(modificationDate)
            map.append(getElementWithSpace(2, "</sitemap>\n"))

            return this
        }

        private fun addLoc(loc: String) {
            val element = getElementWithSpace(4, "<loc>$loc</loc>\n")
            map.append(element)
        }

        private fun addLastMod(modificationDate: Long) {
            val date = Date(modificationDate)
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val element = getElementWithSpace(4, "<lastmod>${formatter.format(date)}</lastmod>\n")
            map.append(element)
        }

        private fun getElementWithSpace(numberOfSpace: Int, element: String): String {
            return " ".repeat(numberOfSpace).plus(element)
        }
    }
}