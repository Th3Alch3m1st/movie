# Coding Challenge

## About the application
On launch, Application will try to fetch the latest movies and the result will be shown as pagination. If requests fail due to internet connection or any other api related problem app will show an error UI along with a retry button. If api calls fail during pagination, app will show an error footer along with a retry button.

Users can search new movies by typing on search view and the result will be shown as pagination. Initial search view is loaded with the query word “action”. If the request fails during pagination, app will show a footer error screen along with a retry button.

If no result is found during a search query, app will show an empty screen.

Tapping on the movie card on both the latest movie and search screen app will open a detailed movie screen.

App also have a caching mechanism implemented with retrofit.

Users can select two themes light and dark, by default the app goes with system settings. If dark mode enabled the app will be dark mode otherwise light mode.


Users can navigate settings to change themes from the toolbar option menu.

## Improvement area:
- Could have written it Jetpack compose, unfortunately I haven’t  learn UI test for jetpack compose and still learning Jetpack compose
- Could have Use room database for offline caching instead I used retrofit caching(Due to time constraints)
- Themoviedb Api key could have store more securely with secrets gradle plugins
- Could have write more Unit/UI test to cover more scenario
- Could have write  kotlin dsl plugin to avoid redundant plugin import in module

## Some Trade-off:
Like most of the applications I also have trade off decisions in this application.
- I put the ISettingPreference use case into the preference module, I did it because in some cases apps may need SettingPreference info like theme mode in other modules. If I put ISettingPreference in the settings module, In that case I have to add settings feature module to that module. It better to have implement a utils module rather than feature module
- All test utils are put into testutils module and in gradle they were added as implementation rather than testImplementation. And later added it as testImplementation and androidTestImplementation in the feature module so test util could be shared in every feature module and it looks like a circular dependency. But it’s in the testing layer.


## Demo
<a href="url"><img src="https://github.com/Th3Alch3m1st/codingchallengestocard/blob/main/screenshots/app_demo.gif" height="480" width="230" />
  
## Screenshots
<a href="url"><img src="https://github.com/Th3Alch3m1st/codingchallengestocard/blob/main/screenshots/screen_latest_movies.jpeg" height="480" width="230" />
<a href="url"><img src="https://github.com/Th3Alch3m1st/codingchallengestocard/blob/main/screenshots/screen_latest_movies_dark.jpeg" height="480" width="230" />
<a href="url"><img src="https://github.com/Th3Alch3m1st/codingchallengestocard/blob/main/screenshots/screen_movie_search.jpeg" height="480" width="230" />
<a href="url"><img src="https://github.com/Th3Alch3m1st/codingchallengestocard/blob/main/screenshots/screen_movie_details.jpeg" height="480" width="230" />
<a href="url"><img src="https://github.com/Th3Alch3m1st/codingchallengestocard/blob/main/screenshots/screen_settings.jpeg" height="480" width="230" />

## Architecture
MVVM  with clean Architecture and moduler approch
  
## Third-party libraries
- Architecture Components: Lifecycle, ViewModel, Navigation, Safe Args, Paging 3
- UI component: Material
- Data Binding
- Coroutine, Flow
- Dependency injector: Hilt
- Networking: Retrofit, Moshi
- Glide: Image loading
- Unit testing: JUnit4, AssertJ, MockitoKotlin, Espresso Arch core testing (InstantTaskExecutorRule), Kotlinx coroutines test
- UI testing: Espresso
- Full list: https://github.com/Th3Alch3m1st/codingchallengestocard/blob/main/Dependencies.kt

## Project Structure
  <a href="url"><img src="https://github.com/Th3Alch3m1st/codingchallengestocard/blob/main/screenshots/app_architechture.png" height="466" width="485" />
  <a href="url"><img src="https://github.com/Th3Alch3m1st/codingchallengestocard/blob/main/screenshots/project_structure.png" height="466" width="320" />
    
## Application Data flow
  <a href="url"><img src="https://github.com/Th3Alch3m1st/codingchallengestocard/blob/main/screenshots/flow.png" height="241" width="818" />  
    
## Approach Explanation
- ViewModel separate UI and Data layer. ViewModel allows data to survive configuration changes and improve testabilities.
- Jetpack DataBinding to bind the layouts views and it's null safe.
- Use Kotlin DSL for gradle management - it helps better gradle management in multi module projects. And increase readability, provide code navigation and auto suggestions
- Write code maintaining SOLID principle
- Used mapper class to convert network response into UI model
- Wrote Unit test and UI test to ensure app stability and performance
- Wrote some infix function to increase unit test readability
- Add documentation in UI test to explain test scenario and write short comment for unit test
    
    
## Build tools
- Android Studio Electric Eel | 2022.1.1
- Gradle 7.4.0
