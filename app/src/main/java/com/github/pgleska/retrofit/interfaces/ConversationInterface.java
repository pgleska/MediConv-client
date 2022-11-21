package com.github.pgleska.retrofit.interfaces;

import com.github.pgleska.dtos.ConversationDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ConversationInterface {

    @GET("/conversations")
    Call<List<ConversationDTO>> getConversations(@Header("Authorization") String token);

    @GET("/conversations")
    Call<List<ConversationDTO>> getConversationsWithQueryParams(@Header("Authorization") String token,
                                                                @Query("phrase") String phrase);
}
