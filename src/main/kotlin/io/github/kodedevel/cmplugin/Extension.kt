package io.github.kodedevel.cmplugin

import org.gradle.api.Action
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Nested
import javax.inject.Inject

abstract class CMPExtension @Inject constructor() {

    abstract val projectDir: Property<String>

    abstract val backupDir: Property<String>

    abstract val baseURL: Property<String>

    init {
        backupDir.convention("backup")
    }

    @get:Nested
    abstract val git: GitExtension

    fun git(action: Action<GitExtension>) {
        action.execute(git)
    }

    @get:Nested
    abstract val sitemap: SitemapExtension

    fun sitemap(action: Action<SitemapExtension>) {
        action.execute(sitemap)
    }
}


abstract class GitExtension @Inject constructor() {
    abstract val commitMessage: Property<String>
    abstract val username: Property<String>
    abstract val origin: Property<String>

    init {
        origin.convention("main")
        commitMessage.convention("Updated")
    }
}


abstract class SitemapExtension @Inject constructor() {
    abstract val categories: Property<Array<String>?>
    abstract val exclude: Property<Array<String>?>
    abstract val priority: Property<Float?>
    abstract val changeFreq: Property<ChangeFreqValues?>
    abstract val homePage: Property<String>

    init {
        categories.convention(null)
        exclude.convention(null)
        priority.convention(null)
        changeFreq.convention(null)
        homePage.convention("index.html")
    }
}

enum class ChangeFreqValues(val v: String) {
    HOURLY("hourly"), DAILY("daily"), WEEKLY("weekly"), MONTHLY("monthly"), YEARLY("yearly"), NEVER("never")
}