plugins {
	id("com.android.library")
	id("kotlin-android")
	id("kotlin-parcelize")
	id("org.jetbrains.kotlin.android")
	id("dagger.hilt.android.plugin")
	id("kotlin-kapt")
}

android {
	namespace = BuildConfig.core
	compileSdk = BuildConfig.compileSdkVersion

	defaultConfig {
		minSdk = BuildConfig.minSdkVersion
		targetSdk = BuildConfig.targetSdkVersion

		consumerProguardFiles("consumer-rules.pro")
	}

	buildFeatures {
		dataBinding = true
	}

	buildTypes {
		getByName("debug"){
			buildConfigField("String", "AUTH_TOKEN", "\"ddf19a1b3587568c5c436f7f53196f0a\"")
			buildConfigField("String", "BASE_URL", "\"https://api.themoviedb.org/3/\"")
			buildConfigField("String", "IMAGE_URL", "\"https://image.tmdb.org/t/p\"")
		}
		getByName("release") {
			buildConfigField("String", "AUTH_TOKEN", "\"ddf19a1b3587568c5c436f7f53196f0a\"")
			buildConfigField("String", "BASE_URL", "\"https://api.themoviedb.org/3/\"")
			buildConfigField("String", "IMAGE_URL", "\"https://image.tmdb.org/t/p\"")
			isMinifyEnabled = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}
	kotlinOptions {
		jvmTarget = "1.8"
	}
}

dependencies {
	implementation(project(Module.styles))

	implementation(KotlinDependencies.coreKtx)
	implementation(AndroidXSupportDependencies.appCompat)
	implementation(MaterialDesignDependencies.materialDesign)

	implementation(AndroidXSupportDependencies.pagingRuntime)

	implementation(AndroidXSupportDependencies.navigationFragmentKtx)

	HiltDependency()
	NetworkDependency()

	GlideDependency()

	implementation(Libraries.sdp)
	implementation(Libraries.ssp)
}