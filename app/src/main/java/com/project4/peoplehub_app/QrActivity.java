package com.project4.peoplehub_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.lifecycle.ProcessCameraProvider;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.project4.peoplehub_app.databinding.ActivityQrBinding;
import com.project4.peoplehub_app.pojos.AddFriendResponse;
import com.project4.peoplehub_app.pojos.UserResponse;
import com.project4.peoplehub_app.service.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QrActivity extends AppCompatActivity {
    AlertDialog.Builder builder;
    private ActivityQrBinding binding;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private static int WIDTH = 350;
    private static int HEIGHT = 350;
    private String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQrBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        builder = new AlertDialog.Builder(this);
        binding.qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator intentIntegrator = new IntentIntegrator(QrActivity.this);
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                intentIntegrator.setBeepEnabled(true);
                intentIntegrator.addExtra("SCAN_WIDTH", WIDTH);
                intentIntegrator.addExtra("SCAN_HEIGHT", HEIGHT);
                intentIntegrator.initiateScan();
            }
        });
        getUserInfo();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            builder.setMessage("Would you like to add this user")
                    .setCancelable(true)
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
//                            intent.putExtra(SearchManager.QUERY, result.getContents());
//                            startActivity(intent);
                            sendAddFriend(result.getContents());
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private void getUserInfo() {
        Intent intent = getIntent();
        String accessToken = intent.getStringExtra("accessToken");
        Call<UserResponse> call = ApiClient.getApi().getUserInfo("Bearer " + accessToken);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                Log.i("msg", response.toString());
                if (response.isSuccessful()) {
                    UserResponse userResponse = response.body();
                    String userName = userResponse.getData().getUsername();
                    binding.tvUserName.setText(userName);
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {

            }
        });
    }

    private void sendAddFriend(String result) {
        Intent intent = getIntent();
        String accessToken = intent.getStringExtra("accessToken");
        Call<AddFriendResponse> call = ApiClient.getApi().sendAddFriend("Bearer " + accessToken, result);
        call.enqueue(new Callback<AddFriendResponse>() {
            @Override
            public void onResponse(Call<AddFriendResponse> call, Response<AddFriendResponse> response) {
                Log.i("msg", "Send addFriend success");
            }

            @Override
            public void onFailure(Call<AddFriendResponse> call, Throwable t) {
                Log.i("msg", "Send addFriend failed");
            }
        });
    }
}