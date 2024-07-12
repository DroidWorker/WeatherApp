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
include(":app:feature")
include(":network")
include(":myapplication")
include(":feature")
include(":feature:currentweather")
include(":feature:fivadaysweather")
include(":feature:searchweather")
include(":feature:widgets")
include(":di")
include(":data")
include(":domain")
