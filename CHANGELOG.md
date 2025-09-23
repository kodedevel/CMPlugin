# Small fixes and added features

## Features
### Added support

#### Support time in sitemap files
- You can have time in generated sitemap files.

#### Date and time from git commits for sitemaps
- If the project is a git project it gets the last modification time from last commit for sitemaps.

#### Get gitAccessToken from env
- You can define your git access token as your system environment variable then set it as your environment variable in build.gradle file.

### Small changes:
#### Changed the name of projectDir to workSpaceDir

- Now the projectDir is worksapceDir to better understand the root of your static contents name.

## Fixes
### Small fixes:
- After the plugin is added u are not forced to set a name for workspace directory, it has a default name which is root-contents-no-name but surely u will need a name for your workspace directory :)
- Initially you don't need to set a username for your git configuration while u will need to set it during your work with git tasks later.
