package io.github.kodedevel.cmplugin.tasks.sitemap.builder

import java.text.SimpleDateFormat
import java.util.Date

abstract class BaseSitemap: Addable {

    internal abstract val map: StringBuilder

    override fun addLastMod(modificationDate: String) {
        val components = modificationDate.split(Regex("\\s|\""))

        val dateExpression = Regex("\\d{4}-\\d{2}-\\d{2}")
        val timeExpression = Regex("\\d{2}:\\d{2}:\\d{2}")
        val timeZoneExpression = Regex("[+-]\\d{4}")

        val date = StringBuilder()

        for (s in components){
            if (dateExpression.containsMatchIn(s))
                date.append(s)
            else if (timeExpression.containsMatchIn(s))
                date.append("T").append(s)
            else if (timeZoneExpression.containsMatchIn(s))
                date.append(s)
            else continue
        }

        val element = getElementWithSpace(4, "<lastmod>$date</lastmod>\n")

        map.append(element)
    }

    override fun addLastMod(modificationDate: Long) {
        val date = Date(modificationDate)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val element = getElementWithSpace(4, "<lastmod>${dateFormat.format(date)}</lastmod>\n")
        map.append(element)
    }

    override fun addLoc(loc: String) {
        val element = getElementWithSpace(4, "<loc>$loc</loc>\n")
        map.append(element)
    }

    internal fun getElementWithSpace(numberOfSpaces: Int, element: String): String {
        return " ".repeat(numberOfSpaces).plus(element)
    }
}