package io.github.kodedevel.cmplugin


import io.github.kodedevel.cmplugin.tasks.backup.CreateBackup
import io.github.kodedevel.cmplugin.tasks.backup.Restore
import io.github.kodedevel.cmplugin.tasks.sitemap.CleanSitemaps
import io.github.kodedevel.cmplugin.tasks.sitemap.CreateSitemap
import io.github.kodedevel.cmplugin.tasks.git.Add
import io.github.kodedevel.cmplugin.tasks.git.AddRemote
import io.github.kodedevel.cmplugin.tasks.git.Clone
import io.github.kodedevel.cmplugin.tasks.git.Commit
import io.github.kodedevel.cmplugin.tasks.git.Init
import io.github.kodedevel.cmplugin.tasks.git.Pull
import io.github.kodedevel.cmplugin.tasks.git.Push
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Delete
import org.gradle.kotlin.dsl.*

class CMPlugin : Plugin<Project> {

    override fun apply(project: Project) {

        val extension = project.extensions.create<CMPExtension>("cmpConfig")

        backupAndRestore(project, extension)

        gitTasks(project, extension)

        sitemapTasks(project, extension)

    }

    private fun sitemapTasks(project: Project, extension: CMPExtension) {
        project.tasks.register<CreateSitemap>(CreateSitemap.NAME) {
            group = GROUP_NAME_CMPLUGIN
            config(extension)
        }

        project.tasks.register<CleanSitemaps>(CleanSitemaps.NAME) {
            group = GROUP_NAME_CMPLUGIN
            config(extension)
        }
    }

    private fun gitTasks(project: Project, extension: CMPExtension) {

        project.tasks.register<Init>(Init.NAME){
            group = GROUP_NAME_GIT
            config(extension)
        }

        project.tasks.register<Add>(Add.NAME) {
            group = GROUP_NAME_GIT
            config(extension)
        }

        project.tasks.register<Commit>(Commit.NAME) {
            group = GROUP_NAME_GIT
            mustRunAfter(Add.NAME)
            config(extension)
        }

        project.tasks.register<Push>(Push.NAME) {
            group = GROUP_NAME_GIT
            mustRunAfter(Commit.NAME)
            config(extension)
        }

        project.tasks.register<Clone>(Clone.NAME) {
            group = GROUP_NAME_GIT
            config(extension)
        }

        project.tasks.register<AddRemote>(AddRemote.NAME) {
            group = GROUP_NAME_GIT
            config(extension)
            mustRunAfter(Clone.NAME)
        }

        project.tasks.register<Pull>(Pull.NAME) {
            group = GROUP_NAME_GIT
            config(extension)
            mustRunAfter(AddRemote.NAME)
        }

        project.tasks.register("automatePush") {
            group = GROUP_NAME_GIT
            dependsOn(Add.NAME, Commit.NAME, Push.NAME)
        }

        project.tasks.register("automateClone") {
            group = GROUP_NAME_GIT
            dependsOn(Clone.NAME, AddRemote.NAME, Pull.NAME)
        }
    }

    private fun backupAndRestore(project: Project, extension: CMPExtension) {

        val deleteBackup by project.tasks.registering(Delete::class) {
            group = GROUP_NAME_BACKUP_AND_RESTORE
            setDelete(project.layout.projectDirectory.dir(extension.backupDir))
            doLast {
                println("Delete backup")
            }
        }

        project.tasks.register<CreateBackup>("createBackup") {
            group = GROUP_NAME_BACKUP_AND_RESTORE
            dependsOn(deleteBackup)
            config(extension)
            doLast { println("Create backup") }
        }

        project.tasks.register<Restore>("restore") {
            group = GROUP_NAME_BACKUP_AND_RESTORE
            config(extension)
        }
    }
}