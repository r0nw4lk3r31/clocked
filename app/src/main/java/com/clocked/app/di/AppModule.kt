package com.clocked.app.di

import android.content.Context
import androidx.room.Room
import com.clocked.app.data.db.ClockedDatabase
import com.clocked.app.data.db.ShiftDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ClockedDatabase =
        Room.databaseBuilder(context, ClockedDatabase::class.java, "clocked.db")
            .build()

    @Provides
    fun provideShiftDao(db: ClockedDatabase): ShiftDao = db.shiftDao()
}
