package com.github.pgleska.retrofit.interfaces;

import com.github.pgleska.dtos.MessageDTO;
import com.github.pgleska.dtos.ResponseDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MessageInterface {

    @GET("/conversations")
    Call<List<MessageDTO>> getConversations(@Header("Authorization") String token);

    @GET("/conversations")
    Call<List<MessageDTO>> getConversationsWithQueryParams(@Header("Authorization") String token,
                                                           @Query("phrase") String phrase);

    @POST("/api/message/send")
    Call<ResponseDTO<MessageDTO>> sendMessage(@Header("Authorization") String token,
                                              @Body MessageDTO messageDTO);

    @GET("/api/message/download/{id}")
    Call<ResponseDTO<List<MessageDTO>>> downloadMessages(@Header("Authorization") String token,
                                                         @Path("id") Integer id);
}
