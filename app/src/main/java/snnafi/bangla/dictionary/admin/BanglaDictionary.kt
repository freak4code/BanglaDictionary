package snnafi.bangla.dictionary.admin

import androidx.multidex.MultiDexApplication
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import snnafi.bangla.dictionary.admin.fly.BanglaDictionaryApi
import snnafi.bangla.dictionary.admin.util.Constant
import snnafi.bangla.dictionary.admin.util.Helper
import java.util.concurrent.TimeUnit


class BanglaDictionary : MultiDexApplication() {


    override fun onCreate() {
        super.onCreate()
        Helper.application = this
        FirebaseMessaging.getInstance().subscribeToTopic(Constant.NOTIFICATION)
            .addOnSuccessListener {
                //Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
            }

    }


    companion object {
        fun banglaDictionaryApi(): BanglaDictionaryApi {
            return fly().create(BanglaDictionaryApi::class.java)
        }

        fun fly(): Retrofit {
            val gson = GsonBuilder()
                .setLenient()
                .create()
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build()



            return Retrofit.Builder()
                .baseUrl("https://app.snnafi.com/BanglaDictionary/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(client)
                .build()
        }
    }
}