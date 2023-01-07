package com.github.pgleska.ui.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.github.pgleska.dtos.PrivateKeyDTO;
import com.github.pgleska.dtos.UserDTO;

import java.security.PrivateKey;

import javax.crypto.SecretKey;

public class UniversalViewModel extends ViewModel {
    private MutableLiveData<PrivateKey> privateKey;
    private MutableLiveData<String> token;
    private MutableLiveData<UserDTO> user;
    private MutableLiveData<UserDTO> otherUser;
    private MutableLiveData<MODE> mode;
    private MutableLiveData<SecretKey> sk;

    public enum MODE {
        LOGIN,
        REGISTER
    }

    public UniversalViewModel() {
        this.privateKey = new MutableLiveData<>();
        this.token = new MutableLiveData<>();
        this.user = new MutableLiveData<>();
        this.otherUser = new MutableLiveData<>();
        this.mode = new MutableLiveData<>();
        this.sk = new MutableLiveData<>();
    }

    public void clear() {
        this.privateKey = new MutableLiveData<>();
        this.token = new MutableLiveData<>();
        this.user = new MutableLiveData<>();
        this.otherUser = new MutableLiveData<>();
        this.mode = new MutableLiveData<>();
        this.sk = new MutableLiveData<>();
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey.setValue(privateKey);
    }

    public PrivateKey getPrivateKey() {
        return privateKey.getValue();
    }

    public void setToken(String token) {
        this.token.setValue(token);
    }

    public String getToken() {
        return "Bearer " + token.getValue();
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

    public void setRegisterMode() {
        mode.setValue(MODE.REGISTER);
    }

    public void setLoginMode() {
        mode.setValue(MODE.LOGIN);
    }

    public MODE getMode() {
        return this.mode.getValue();
    }

    public void setSK(SecretKey sk) {
        this.sk.setValue(sk);
    }

    public SecretKey getSK() {
        return this.sk.getValue();
    }
}
