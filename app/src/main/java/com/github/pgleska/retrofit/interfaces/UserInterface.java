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
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserInterface {

    @POST("/login")
    Call<TokenDTO> login(@Body CredentialsDTO credentialsDTO);

    @GET("/api/user")
    Call<ResponseDTO<UserDTO>> getUser(@Header("Authorization") String token);

    @PATCH("/api/user")
    Call<ResponseDTO<UserDTO>> updateUser(@Header("Authorization") String token,
                                          @Body UserDTO userDTO);

    @POST("/api/user/register")
    Call<ResponseDTO<UserDTO>> registerUser(@Body UserDTO userDTO);

    @GET("/api/user/search")
    Call<ResponseDTO<List<UserDTO>>> searchForUser(@Header("Authorization") String token,
                                                   @Query("seq") String sequence);

    @POST("/api/user/publicKey")
    Call<ResponseDTO<UserDTO>> sendPublicKey(@Header("Authorization") String token,
                                                   @Body UserDTO userDTO);

    @GET("/api/user/privateKey")
    Call<ResponseDTO<PrivateKeyDTO>> getPrivateKey(@Header("Authorization") String token);

    @POST("/api/user/privateKey")
    Call<ResponseDTO<PrivateKeyDTO>> sendPrivateKey(@Header("Authorization") String token,
                                                    @Body PrivateKeyDTO privateKeyDTO);
}
