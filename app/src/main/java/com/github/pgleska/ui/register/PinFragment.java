package com.github.pgleska.ui.register;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.github.pgleska.R;
import com.github.pgleska.cryptography.CryptographyUtils;
import com.github.pgleska.databinding.FragmentPinBinding;
import com.github.pgleska.dtos.PrivateKeyDTO;
import com.github.pgleska.dtos.ResponseDTO;
import com.github.pgleska.dtos.UserDTO;
import com.github.pgleska.retrofit.RetrofitClient;
import com.github.pgleska.retrofit.interfaces.UserInterface;
import com.github.pgleska.ui.viewModels.UniversalViewModel;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PinFragment extends Fragment {
    //generating private and public keys
    private static final String TAG = PinFragment.class.getName();
    private FragmentPinBinding binding;
    private View root;
    private Button ButtonRegister;
    private EditText pinET;

    private UniversalViewModel viewModel;
    private UserInterface userInterface;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentPinBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        initComponents();
        initListeners();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initComponents() {
        viewModel = new ViewModelProvider(getActivity()).get(UniversalViewModel.class);
        pinET = binding.etPin;
        ButtonRegister = binding.btnConfirm;
        userInterface = RetrofitClient.getUserInterface(getString(R.string.server_address));
    }

    private void initListeners() {
        ButtonRegister.setOnClickListener(v -> {
            if(viewModel.getMode().equals(UniversalViewModel.MODE.REGISTER)) {
                registerFlow();
            } else {
                loginFlow();
            }
        });
    }

    private void registerFlow() {
        try {
            Map<String, String> keys = CryptographyUtils.generatePair();
            SecretKey pinCodeKey = CryptographyUtils.createPinCodeKey(pinET.getText().toString());
            viewModel.setSK(pinCodeKey);
            String publicKey = keys.get("public");
            String privateKey = keys.get("private");
            sendPublicKey(publicKey);
            sendPrivateKey(privateKey, pinCodeKey);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    private void sendPublicKey(String publicKey) {
        UserDTO userDTO = new UserDTO();
        userDTO.setPublicKey(publicKey);
        Call<ResponseDTO<UserDTO>> call = userInterface.sendPublicKey(viewModel.getToken(), userDTO);
        call.enqueue(new Callback<ResponseDTO<UserDTO>>() {
            @Override
            public void onResponse(Call<ResponseDTO<UserDTO>> call, Response<ResponseDTO<UserDTO>> response) {
                if(!response.isSuccessful()) {
                    Log.e(TAG, String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<ResponseDTO<UserDTO>> call, Throwable t) {

            }
        });
    }

    private void sendPrivateKey(String privateKey, SecretKey pinCodeKey) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
//        String encrypted = CryptographyUtils.encrypt("AES", privateKey, pinCodeKey);
//        PrivateKeyDTO privateKeyDTO = new PrivateKeyDTO();
//        privateKeyDTO.setPrivateKey(encrypted);
//        Call<ResponseDTO<PrivateKeyDTO>> call = userInterface.sendPrivateKey(viewModel.getToken(), privateKeyDTO);
//        call.enqueue(new Callback<ResponseDTO<PrivateKeyDTO>>() {
//            @Override
//            public void onResponse(Call<ResponseDTO<PrivateKeyDTO>> call, Response<ResponseDTO<PrivateKeyDTO>> response) {
//                if(response.isSuccessful()) {
////                    viewModel.setPrivateKey(response.body().getPayload().getPrivateKey());
////                    Navigation.findNavController(root).navigate(R.id.action_nav_pin_to_nav_login);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseDTO<PrivateKeyDTO>> call, Throwable t) {
//
//            }
//        });
    }

    private void loginFlow() {

    }
}