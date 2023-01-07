package com.github.pgleska.ui.register;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PinFragment extends Fragment {
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
        ButtonRegister.setEnabled(false);
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

        pinET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 4) {
                    ButtonRegister.setEnabled(true);
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(root.getWindowToken(), 0);
                }
            }
        });
    }

    private void registerFlow() {
        try {
            Map<String, String> keys = CryptographyUtils.generatePair();
            byte[] salt = CryptographyUtils.generateSalt();
            SecretKey pinCodeKey = CryptographyUtils.createPinCodeKey(pinET.getText().toString(), salt);
            String publicKey = keys.get("public");
            String privateKey = keys.get("private");
            sendPublicKey(publicKey);
            sendPrivateKey(privateKey, pinCodeKey, salt);
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

    private void sendPrivateKey(String privateKey, SecretKey pinCodeKey, byte[] salt) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        String encrypted = CryptographyUtils.encrypt("AES", privateKey, pinCodeKey);
        PrivateKeyDTO privateKeyDTO = new PrivateKeyDTO();
        privateKeyDTO.setPrivateKey(encrypted);
        privateKeyDTO.setSalt(Base64.getEncoder().encodeToString(salt));
        Call<ResponseDTO<PrivateKeyDTO>> call = userInterface.sendPrivateKey(viewModel.getToken(), privateKeyDTO);
        call.enqueue(new Callback<ResponseDTO<PrivateKeyDTO>>() {
            @Override
            public void onResponse(Call<ResponseDTO<PrivateKeyDTO>> call, Response<ResponseDTO<PrivateKeyDTO>> response) {
                if(response.isSuccessful()) {
                    try {
                        String encryptedPrivateKey = response.body().getPayload().getPrivateKey();
                        String salt = response.body().getPayload().getSalt();
                        SecretKey pinCodeKey = CryptographyUtils.createPinCodeKey(
                                pinET.getText().toString(), Base64.getDecoder().decode(salt));
                        String decryptedPrivateKey = CryptographyUtils.decrypt("AES", encryptedPrivateKey, pinCodeKey);
                        PrivateKey privateKey = CryptographyUtils.restorePrivateKey(decryptedPrivateKey);
                        viewModel.setPrivateKey(privateKey);
                        pinET.setText("");
                        Navigation.findNavController(root).navigate(R.id.action_nav_pin_to_nav_conversations);
                    } catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException |
                            IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseDTO<PrivateKeyDTO>> call, Throwable t) {

            }
        });
    }

    private void loginFlow() {
        Call<ResponseDTO<PrivateKeyDTO>> call = userInterface.getPrivateKey(viewModel.getToken());
        call.enqueue(new Callback<ResponseDTO<PrivateKeyDTO>>() {
            @Override
            public void onResponse(Call<ResponseDTO<PrivateKeyDTO>> call, Response<ResponseDTO<PrivateKeyDTO>> response) {
                if(response.isSuccessful()) {
                    try {
                        String encryptedPrivateKey = response.body().getPayload().getPrivateKey();
                        String salt = response.body().getPayload().getSalt();
                        SecretKey pinCodeKey = CryptographyUtils.createPinCodeKey(
                                pinET.getText().toString(), Base64.getDecoder().decode(salt));
                        String decryptedPrivateKey = CryptographyUtils.decrypt("AES", encryptedPrivateKey, pinCodeKey);
                        PrivateKey privateKey = CryptographyUtils.restorePrivateKey(decryptedPrivateKey);
                        viewModel.setPrivateKey(privateKey);
                        pinET.setText("");
                        Navigation.findNavController(root).navigate(R.id.action_nav_pin_to_nav_conversations);
                    } catch (InvalidKeySpecException | NoSuchAlgorithmException | NoSuchPaddingException |
                            InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
                        e.printStackTrace();
                        pinET.setText("");
                        ButtonRegister.setEnabled(false);
                        Toast.makeText(getContext(), "Incorrect PIN", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseDTO<PrivateKeyDTO>> call, Throwable t) {

            }
        });
    }

}