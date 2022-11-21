package com.github.pgleska.retrofit;

import com.github.pgleska.retrofit.interfaces.MessageInterface;
import com.github.pgleska.retrofit.interfaces.UserInterface;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    private RetrofitClient() {}

    private static Retrofit getClient(String baseUrl) {
        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static MessageInterface getConversationInterface(String url) {
        return getClient(url).create(MessageInterface.class);
    }

    public static UserInterface getUserInterface(String url) {
        return getClient(url).create(UserInterface.class);
    }
}
