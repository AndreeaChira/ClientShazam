package ro.fmit.ac;

import java.nio.file.attribute.UserPrincipalLookupService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static Retrofit getRetrofi(){

        HttpLoggingInterceptor httpLoggingInterceptor=new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient=new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();

        Retrofit retrofit=new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("")
                .client(okHttpClient)
                .build();

        return retrofit;
    }

    public static RetrofitService getservice(){
        RetrofitService retrofitService=getRetrofi().create(RetrofitService.class);
        return retrofitService;
    }
}
