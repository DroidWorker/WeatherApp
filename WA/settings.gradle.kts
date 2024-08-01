pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "WeatherApp"
include(":app")
include(":core:network")
include(":feature:widget")
include(":feature:searchweather")
include(":feature:fivedaysweather")
include(":feature:currentweather")
include(":domain")
include(":core:di")
include(":core:data")
include(":core:localDB")
