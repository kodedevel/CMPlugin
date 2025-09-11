package io.github.kodedevel.cmplugin.tasks.sitemap.builder

import java.text.SimpleDateFormat
import java.util.*

class Sitemap private constructor(private val _map: String) {

    val map: String get() = _map


    class Builder {
        private val map = StringBuilder(
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n"
        )

        fun addURL(loc: String, lastmod: Long? = null, priority: Float? = null, changeFreq: String? = null): Builder {

            map.append(getElementWithSpace(2, "<url>\n"))
            addLoc(loc)

            if (lastmod != null) addLastMod(lastmod)

            if (priority != null) {
                if (priority > 1f || priority < 0f) throw IllegalArgumentException("Priority must be between 0 and 1")
                addPriority(priority)
            }

            if (changeFreq != null)
                addChangeFreq(changeFreq)

            map.append(getElementWithSpace(2, "</url>\n"))

            return this
        }


        private fun addLastMod(modificationDate: Long) {
            val date = Date(modificationDate)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            val element = getElementWithSpace(4, "<lastmod>${dateFormat.format(date)}</lastmod>\n")
            map.append(element)
        }

        private fun addPriority(priority: Float) {
            val element = getElementWithSpace(4, "<priority>$priority</priority>\n")
            map.append(element)
        }

        private fun addLoc(loc: String) {
            val element = getElementWithSpace(4, "<loc>$loc</loc>\n")
            map.append(element)
        }

        private fun addChangeFreq(changeFreq: String) {
            val element = getElementWithSpace(4, "<changefreq>$changeFreq</changefreq>\n")
            map.append(element)
        }

        private fun getElementWithSpace(numberOfSpaces: Int, element: String): String {
            return " ".repeat(numberOfSpaces).plus(element)
        }

        fun build(): Sitemap {
            map.append("</urlset>")
            return Sitemap(map.toString())
        }
    }
}