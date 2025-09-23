package io.github.kodedevel.cmplugin.tasks.sitemap.builder



class Sitemap: BaseSitemap() {

    override val map: StringBuilder = StringBuilder(
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n"
    )

    fun getSitemap() = map.append("</urlset>").toString()

    fun addURL(loc: String, lastmod: Any?, priority: Float?, changeFreq: String?) {
        map.append(getElementWithSpace(2, "<url>\n"))
        addLoc(loc)

        if (lastmod != null) {
            when(lastmod){
                is Long -> addLastMod(lastmod)
                is String -> addLastMod(lastmod)
                else -> IllegalArgumentException("lastmod must be either Long or String")
            }
        }

        if (priority != null) {
            if (priority > 1f || priority < 0f) throw IllegalArgumentException("Priority must be between 0 and 1")
            addPriority(priority)
        }

        if (changeFreq != null)
            addChangeFreq(changeFreq)

        map.append(getElementWithSpace(2, "</url>\n"))
    }

    internal fun addPriority(priority: Float) {
        val element = getElementWithSpace(4, "<priority>$priority</priority>\n")
        map.append(element)
    }

    internal fun addChangeFreq(changeFreq: String) {
        val element = getElementWithSpace(4, "<changefreq>$changeFreq</changefreq>\n")
        map.append(element)
    }
}