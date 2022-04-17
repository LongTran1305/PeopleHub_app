package com.project4.peoplehub_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.lifecycle.ProcessCameraProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
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
    private String avatarUrl;
    final static String URL = "http://cc220e9dfab7.sn.mynetname.net:8080/files/downloadFile/";
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
        if (data != null) {
            new MaterialAlertDialogBuilder(QrActivity.this, R.style.AlertDialogTheme)
                    .setTitle("Send Add friend request")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sendAddFriend(result.getContents());
//                            sendAddFriend("hoanghuunghia3");
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).show();

        }
    }

    private void getUserInfo() {
        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        ImageView imageView = (ImageView) binding.ivAvatar;


        Call<UserResponse> call = ApiClient.getApi().getUserInfo("Bearer " + accessToken);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                Log.i("msg", response.toString());
                if (response.isSuccessful()) {
                    UserResponse userResponse = response.body();
                    String userName = userResponse.getData().getUsername();
                    avatarUrl = userResponse.getData().getAvatar();
                    binding.tvUserName.setText(userName);
                    Glide.with(getApplicationContext())
                            .load(URL+avatarUrl)
                            .circleCrop()
                            .into(imageView);

                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {

            }
        });
    }

    private void sendAddFriend(String result) {
        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        Call<AddFriendResponse> call = ApiClient.getApi().sendAddFriend("Bearer " + accessToken, result);
        call.enqueue(new Callback<AddFriendResponse>() {
            @Override
            public void onResponse(Call<AddFriendResponse> call, Response<AddFriendResponse> response) {
                if (response.code() == 200) {
                    new MaterialAlertDialogBuilder(QrActivity.this, R.style.AlertDialogTheme)
                            .setTitle("Send Request Success")
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).show();
                }
                if (response.code() == 400) {
                    new MaterialAlertDialogBuilder(QrActivity.this, R.style.AlertDialogTheme)
                            .setTitle("You already send request to this user")
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            }).show();
                }
            }

            @Override
            public void onFailure(Call<AddFriendResponse> call, Throwable t) {
                new MaterialAlertDialogBuilder(QrActivity.this, R.style.AlertDialogTheme)
                        .setTitle("Send Request Failed")
                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();
            }
        });
    }
}