package com.github.pgleska.retrofit.interfaces;

import com.github.pgleska.dtos.CredentialsDTO;
import com.github.pgleska.dtos.PrivateKeyDTO;
import com.github.pgleska.dtos.ResponseDTO;
import com.github.pgleska.dtos.TokenDTO;
import com.github.pgleska.dtos.UserDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserInterface {

    @POST("/login")
    Call<TokenDTO> login(@Body CredentialsDTO credentialsDTO);

    @POST("/api/user/register")
    Call<ResponseDTO<UserDTO>> registerUser(@Body UserDTO userDTO);

    @POST("/api/user/search")
    Call<ResponseDTO<List<UserDTO>>> searchForUser(@Header("Authorization") String token,
                                                   @Query("seq") String sequence);

    @POST("/api/user/publicKey")
    Call<ResponseDTO<List<UserDTO>>> sendPublicKey(@Header("Authorization") String token,
                                                   @Body UserDTO userDTO);

    @GET("/api/user/privateKey")
    Call<ResponseDTO<List<PrivateKeyDTO>>> getPrivateKey(@Header("Authorization") String token);

    @POST("/api/user/privateKey")
    Call<ResponseDTO<PrivateKeyDTO>> sendPrivateKey(@Header("Authorization") String token,
                                                    @Body PrivateKeyDTO privateKeyDTO);
}
