package com.github.pgleska.ui.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.pgleska.dtos.UserDTO;

public class UniversalViewModel extends ViewModel {
    private MutableLiveData<String> privateKey;
    private MutableLiveData<String> token;
    private MutableLiveData<UserDTO> user;
    private MutableLiveData<UserDTO> otherUser;

    public UniversalViewModel() {
        this.privateKey = new MutableLiveData<>();
        this.token = new MutableLiveData<>();
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey.setValue(privateKey);
    }

    public String getPrivateKey() {
        return privateKey.getValue();
    }

    public void setToken(String token) {
        this.token.setValue(token);
    }

    public String getToken() {
        return token.getValue();
    }

    public UserDTO getUser() {
        return user.getValue();
    }

    public void setUser(UserDTO user) {
        this.user.setValue(user);
    }

    public UserDTO getOtherUser() {
        return otherUser.getValue();
    }

    public void setOtherUser(UserDTO user) {
        this.otherUser.setValue(user);
    }
}
