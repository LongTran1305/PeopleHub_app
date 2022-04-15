package com.project4.peoplehub_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.project4.peoplehub_app.databinding.ActivityLoginBinding;
import com.project4.peoplehub_app.pojos.TokenLogin;
import com.project4.peoplehub_app.model.UserLogin;
import com.project4.peoplehub_app.service.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private TokenLogin tokenLogin ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("msg","long_touch");
                    validateData();
            }
        });
    }

    private void validateData(){
        String email = binding.edtEmail.getEditText().getText().toString();
        String password = binding.edtPassword.getEditText().getText().toString();

        doLogin(email,password);
    }

    private void doLogin(String email, String password) {

        UserLogin userLogin = new UserLogin(email,password);
        userLogin.setUsername(email);
        userLogin.setPassword(password);

        Call<TokenLogin> call = ApiClient.getApi().login(userLogin);
        call.enqueue(new Callback<TokenLogin>() {
            @Override
            public void onResponse(Call<TokenLogin> call, Response<TokenLogin> response) {
                binding.btnLogin.setEnabled(true);
                if(response.isSuccessful()){
                    TokenLogin tokenLogin = response.body();
                    tokenLogin.setAccessToken(response.body().getAccessToken());
                    tokenLogin.setRefreshToken(response.body().getRefreshToken());
                    sendToQrActivity(email,password,tokenLogin.getAccessToken());
                }
            }

            @Override
            public void onFailure(Call<TokenLogin> call, Throwable t) {
                binding.btnLogin.setEnabled(true);
            }
        });

    }
    private void sendToQrActivity(String email,String password,String accessToken){
        Intent intent = new Intent(getApplicationContext(),QrActivity.class);
        intent.putExtra("email",email);
        intent.putExtra("password",password);
        intent.putExtra("accessToken",accessToken);
        startActivity(intent);
        finishAffinity();
    }

}