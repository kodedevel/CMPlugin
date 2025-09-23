plugins{
    `kotlin-dsl`
    id("com.gradle.plugin-publish") version "2.0.0"
}

group = "io.github.kodedevel"
version = "1.1"

repositories{
    gradlePluginPortal()
    mavenCentral()
}

gradlePlugin {
    website.set("https://github.com/kodedevel/CMPlugin")
    vcsUrl.set("https://github.com/kodedevel/CMPlugin.git")
    plugins {
        register("CMPlugin"){
            id = "io.github.kodedevel.cmplugin"
            displayName = "CMPlugin"
            description = "manage static web contents such as automation creating sitemaps, creating backups execute git commands and etc."
            tags = listOf("Web", "HTML", "Git", "Github","Sitemap","Static Website", "Static Content Manager Plugin")
            implementationClass = "io.github.kodedevel.cmplugin.CMPlugin"
        }
    }
}