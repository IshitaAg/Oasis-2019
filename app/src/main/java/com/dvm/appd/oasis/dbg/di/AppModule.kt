package com.dvm.appd.oasis.dbg.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.dvm.appd.oasis.dbg.auth.data.repo.AuthRepository
import com.dvm.appd.oasis.dbg.auth.data.retrofit.AuthService
import com.dvm.appd.oasis.dbg.elas.model.repo.ElasRepository
import com.dvm.appd.oasis.dbg.elas.model.retrofit.ElasService
import com.dvm.appd.oasis.dbg.elas.model.room.ElasDao
import com.dvm.appd.oasis.dbg.shared.AppDatabase
import com.dvm.appd.oasis.dbg.shared.BaseInterceptor
import com.dvm.appd.oasis.dbg.shared.MoneyTracker
import com.dvm.appd.oasis.dbg.shared.NetworkChecker
import com.dvm.appd.oasis.dbg.wallet.data.repo.WalletRepository
import com.dvm.appd.oasis.dbg.wallet.data.retrofit.WalletService
import com.dvm.appd.oasis.dbg.wallet.data.room.WalletDao
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {

    @Provides
    @Singleton
    fun providesMoneyTracker(authRepository: AuthRepository):MoneyTracker{
        return MoneyTracker(authRepository)
    }

    @Provides
    @Singleton
    fun providesWalletRepository(walletService: WalletService, walletDao: WalletDao, authRepository: AuthRepository, moneyTracker: MoneyTracker, networkChecker: NetworkChecker): WalletRepository {
        return WalletRepository(walletService,walletDao,authRepository,moneyTracker, networkChecker)
    }

    @Provides
    @Singleton
    fun providesNetworkChecker(application: Application):NetworkChecker{
        return NetworkChecker(application)
    }
    @Provides
    @Singleton
    fun providesWalletDao(appDatabase: AppDatabase): WalletDao {
        return appDatabase.walletDao()
    }

    @Provides
    @Singleton
    fun providesWalletService(retrofit: Retrofit): WalletService {
        return retrofit.create(WalletService::class.java)
    }

    @Provides
    @Singleton
    fun providesAuthRepository(authService: AuthService, sharedPreferences: SharedPreferences):AuthRepository{
        return AuthRepository(authService,sharedPreferences)
    }

    @Provides
    @Singleton
    fun providesAuthService(retrofit: Retrofit):AuthService{
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun providesSharedPreferences(application: Application): SharedPreferences {
        return application.getSharedPreferences("bosm.sp", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun providesApplicaton(): Application {
        return application
    }

    @Provides
    @Singleton
    fun providesAppDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "bosm.db").fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://testapp.bits-dvm.org:9090/")
            .client(OkHttpClient().newBuilder().addInterceptor(BaseInterceptor()).build())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideElasRepository(elasDao: ElasDao, elasService: ElasService, authRepository: AuthRepository) : ElasRepository {
        return ElasRepository(elasDao, elasService, authRepository)
    }

    @Provides
    @Singleton
    fun provideElasDao(appDatabase: AppDatabase) : ElasDao {
        return appDatabase.elasDao()
    }

    @Provides
    @Singleton
    fun providesElasService(retrofit: Retrofit): ElasService {
        return retrofit.create(ElasService::class.java)
    }
}