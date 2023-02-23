package com.mobimeo.movies.di


import com.mobimeo.movies.data.remote.LatestMoviesRemoteSource
import com.mobimeo.movies.fakeremotesource.FakeLatestMoviesRemoteSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

/**
 * Created By Rafiqul Hasan
 */
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [LatestMoviesRemoteSourceModule::class]
)
abstract class FakeLatestMoviesRemoteSourceModule {
    @Singleton
    @Binds
    abstract fun provideFakeLatestMoviesRemoteSource(impl: FakeLatestMoviesRemoteSourceImpl): LatestMoviesRemoteSource
}