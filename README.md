<h1 align="center">
  Static Web Content Manager Plugin for Gradle
</h1>

A Simple gradle plugin to manage your static web pages.

**Features**
----

**Backup and Restore**
- You can create a local backup of your project any time to have your up-to-date contents to make sure u won't lose your data.

**Create Sitemap**
- You will have full sitemap of your html files just by one command.
- You can delete them all by one command too :)

**Separated project and configuration files**
- Your project will be your web contents directory which shall not be your root directory this will make your workspace separated from your root project. (You don't need to push files behind your workspace to GitHub).

**Support Some Git Features**
- by automation feature you can clone your static contents repository add remote and then pull to sync your project just by one command!
- by automation feature you can add your changed files, commit and publish just by one command!
- supports some git tasks such as init, add, remote add, commit, pull, push separately.
- by init the project will create your project directory and inits git.

**NOTE:** Your root project shall not be your workspace, Your workspace is the directory that you define in root project and the plugin can handle it!

**Add Plugin**
----

You can add plugin in build.gradle file of your root project as below:

```gradle
    plugins{
        id("io.github.kodedevel.cmplugin") version "1.0"
    }
```

**Configuration**
----
After adding plugin to your project you can config it in your build.gradle.kts of your root project directory

**Base Configuration:**

- You could config base configurations as below:
- The configuration of below is essential and u should name your project workspace directory same as your url home page.

```Gradle
cmpConfig{
    projectDir = "path-to-your-workspace-directory"
    baseURL = "url-of-your-website"
}
```

**GIT Configuration**

```Gradle
    cmpConfig{
        
        ...
        
        git{
            username = "Your-Git-Username"
            origin = "origin-of-your-branch" //e.g main
            commitMessage = "Your-commit-message-to-affect-push"
        }
        
    }
```

**SitemapConfiguration**

- You can config your sitemap requirements whether u need a single sitemap file or multiple sitemap files and define your categories for multiple sitemap files related to your urls.


```Gradle
    cmpConfig{

        ...
        
        sitemap{
            categories= arrayOf("first-category", "second-category", /* And so on... */)
            exclude = arrayOf("fileA.html", "fileB.html", "fileC.html", /*and so on ... */)
        }
    }
```

**Note** Your category name must be one of your url parts. for example if your url is https://example.com/a/b/c/file.html your category name must be a or b or c.

**Note** exclude option is added to exclude some html pages which u don't want to add. for example google console's html verification code could be added as exclude to prevent from add it to the sitemap generated file.

**Note** exclude option must likely is good for none category scenarios
