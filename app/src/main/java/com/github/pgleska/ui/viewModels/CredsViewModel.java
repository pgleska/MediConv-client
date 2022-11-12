package com.github.pgleska.ui.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CredsViewModel extends ViewModel {
    private MutableLiveData<String> privateKey;
    private MutableLiveData<String> token;

    public CredsViewModel() {
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
}
