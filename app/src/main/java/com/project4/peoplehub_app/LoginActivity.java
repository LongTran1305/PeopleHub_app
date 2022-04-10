package com.project4.peoplehub_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.project4.peoplehub_app.databinding.ActivityLoginBinding;
import com.project4.peoplehub_app.model.User;
import com.project4.peoplehub_app.service.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private List<User> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

//        sendAddFriendRequest();
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLogin();
            }
        });
    }

    private void clickLogin() {

    }

    private void sendAddFriendRequest(){
        ApiService.apiService.sendAddFriendRequest("abcnajsdjasdasdhsa").enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                mList = response.body();
                Log.i("user",mList.toString());
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Failed to call API", Toast.LENGTH_SHORT).show();
            }
        });
    }
}