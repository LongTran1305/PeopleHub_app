package com.project4.peoplehub_app;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.fragment.app.DialogFragment;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.project4.peoplehub_app.databinding.ActivityQrBinding;

public class QrActivity extends AppCompatActivity {
    AlertDialog.Builder builder;
    private ActivityQrBinding binding;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private static int WIDTH = 350;
    private static int HEIGHT = 350;
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
                intentIntegrator.addExtra("SCAN_WIDTH",WIDTH);
                intentIntegrator.addExtra("SCAN_HEIGHT",HEIGHT);
                intentIntegrator.initiateScan();
            }
        });

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
                            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                            intent.putExtra(SearchManager.QUERY, result.getContents());
                            startActivity(intent);
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

}