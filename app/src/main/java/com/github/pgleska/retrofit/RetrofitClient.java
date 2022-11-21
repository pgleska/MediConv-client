package com.github.pgleska.retrofit;

import com.github.pgleska.retrofit.interfaces.ConversationInterface;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    private RetrofitClient() {}

    private static Retrofit getClient(String baseUrl) {
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl + "/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static ConversationInterface getConversationInterface(String url) {
        return getClient(url).create(ConversationInterface.class);
    }
}
