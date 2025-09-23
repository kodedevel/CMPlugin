package io.github.kodedevel.cmplugin.tasks.sitemap.builder

class SitemapIndex: BaseSitemap() {

    override val map: StringBuilder = StringBuilder(
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<sitemapindex xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n"
    )

    fun getSitemapIndex() = map.append("</sitemapindex>").toString()

    fun addSitemap(loc: String, modificationDate: Any?) {
        map.append(getElementWithSpace(2, "<sitemap>\n"))
        addLoc(loc)

        if (modificationDate != null){
            when(modificationDate){
                is String -> addLastMod(modificationDate)
                is Long -> addLastMod(modificationDate)
                else -> IllegalArgumentException("lastmod must be either Long or String")
            }
        }

        map.append(getElementWithSpace(2, "</sitemap>\n"))
    }
}