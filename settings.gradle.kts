pluginManagement {
    repositories {
        // Add ObjectBox FIRST
        maven("https://maven.objectbox.io")  // Must be first!

        google()
        mavenCentral()
        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "io.objectbox") {
                useModule("io.objectbox:objectbox-gradle-plugin:${requested.version}")
            }
        }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://maven.objectbox.io")  // For ObjectBox dependencies
    }
}


rootProject.name = "LogBridge"
include(":app")

