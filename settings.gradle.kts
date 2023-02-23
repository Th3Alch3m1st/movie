dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}
rootProject.name = "MovieDB"
include (":app")
include (":appcore:core")
include (":appcore:themes")
include(":appcore:preference")
include(":appcore:testutils")

include(":features:latestmovies")
include(":features:searchmovies")
include(":features:moviedetails")

include(":features:settings")
