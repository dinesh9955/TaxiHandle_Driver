package com.rydz.driver.di;

import androidx.lifecycle.ViewModelProvider;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.rydz.driver.apiConstants.ApiCallInterface;
import com.rydz.driver.apiConstants.Urls;
import com.rydz.driver.apiConstants.ViewModelFactory;
import com.rydz.driver.viewModel.login.Repository;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.inject.Singleton;
import java.util.concurrent.TimeUnit;

/**
 * Created by ${Saquib} on 03-05-2018.
 */

@Module
public class UtilsModule {
    //create a single Gson provider
    @Provides
    @Singleton
   public Gson provideGson() {
        GsonBuilder builder =
                new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return builder.setLenient().create();
    }


    //Create a Retrofit with okhttpClient
    @Provides
    @Singleton
   public Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {

        return new Retrofit.Builder()
                .baseUrl(Urls.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }


    //ApiCall Interface for create retrofit client
    @Provides
    @Singleton
   public ApiCallInterface getApiCallInterface(Retrofit retrofit) {
        return retrofit.create(ApiCallInterface.class);
    }

    @Provides
    @Singleton
  public   OkHttpClient getRequestHeader() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            Request request = original.newBuilder()
                    .build();
            return chain.proceed(request);
        })
                .connectTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(100, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS);

        return httpClient.build();
    }

    @Provides
    @Singleton
   public Repository getRepository(ApiCallInterface apiCallInterface) {
        return new Repository(apiCallInterface);
    }

    @Provides
    @Singleton
  public ViewModelProvider.Factory getViewModelFactory(Repository myRepository) {
        return new ViewModelFactory(myRepository);
    }
}
