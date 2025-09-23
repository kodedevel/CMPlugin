# Static Web Content Manager Plugin for Gradle

A Simple gradle plugin to manage your static web pages.

## Features

### Backup and Restore
- You can create a local backup of your project any time to have your up-to-dated contents.

### Create Sitemap
- have full sitemap of your html files just by one command.
- delete them all generated sitemap files by one command.

### Separated project and configuration files
- Your project will be your web contents directory which shall not be your root directory this will make your workspace separated from your root project. (You don't need to push files behind your workspace to GitHub).

### Support Git
- clone your static contents repository add remote and then pull to sync your project just by one command!
- add your changed files, commit and publish just by one command!
- supports some git tasks such as init, add, remote add, commit, pull, push separately.
- the plugin can create your project directory and init git.

**NOTE:** Your root project shall not be your workspace, Your workspace is the directory that you define in root project and the plugin can handle it as root of your website!

## Add and Config

### Add Plugin

- Add plugin to build.gradle of your root project as below:

```gradle
    plugins{
        id("io.github.kodedevel.cmplugin") version "1.1"
    }
```

### Config Plugin

#### Define workspace directory path

- this is were your project will go 

```Gradle
cmpConfig{
    workspaceDir = "path-to-your-workspace-directory"
}
```

#### Define backup directory path

```gradle
cmpConfig{
    backupDir = "path-to-your-backup-directory"
}
```

#### Define baseUrl

- Define baseUrl of your website to create sitemaps.

```gradle
cmpConfig{
    baseUrl = "your-website-address"
}
```

#### Define username for git

```Gradle
    cmpConfig{
        
        ...
        
        git{
            username = "Your-Git-Username"
        }
        
        ...
    }
```

#### Define origin for github

```Gradle
    cmpConfig{
        
        ...
        
        git{
            origin = "origin-of-your-branch" //e.g main
        }
        
        ...
        
    }
```

#### Get git access token environment variable

- If you have defined git access token as system variable u can call it by defining its name in configuration file as below:

```Gradle
    cmpConfig{
        
        ...
        
        git{
            accessTokenSystemVarName = "your-system-access-token-variable-name"
        }
        ...
    }
```

#### Define categories for sitemap files

- You can have several sitemap files in category
- Your category name must be one of your url parts, for example if your url is https://example.com/a/b/c/file.html your category name must be a or b or c.


```Gradle
    cmpConfig{

        ...
        
        sitemap{
            categories= arrayOf("first-category", "second-category", /* And so on... */)
        }
        
        ...
    }
```

#### Prevent html pages from being included in sitemap files.

- You can exclude pages from being added to sitemap files.

```gradle
cmpConfig{
        
        ...
        
        sitemap{
            exclude = arrayOf("fileA.html", "fileB.html", "fileC.html", /*and so on ... */)
        }
        ...
}
```

### Executable Commands

- below is some important commands to use in gradle:

#### Backup and restore
**createBackup** create a local backup from the project

**cleanBackup** clean local backup

#### Git

- **initGit**
create a git project and its corresponding workspace directory

- **automatePush**
automatically does necessary tasks to push project to the github repository

- **automateClone**
automatically does necessary tasks to clone and sync project from repository

#### Sitemap

- **autoCreateSitemaps**
automatically creates sitemap file(s) and sitemapindex for the project

- **cleanSitemaps**
clean all generated sitemap files.