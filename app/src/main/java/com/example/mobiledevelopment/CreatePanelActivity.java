package com.example.mobiledevelopment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatePanelActivity extends AppCompatActivity {

    private final static String IMAGE_STUB = "https://fsmedia.imgix.net/4f/2a/c7/1f/6e01/41ff/8fa5/9774418130fc.jpeg?crop=edges&fit=crop&auto=format%2Ccompress&dpr=2&h=325&w=650";

    private TextInputLayout mTypeField;
    private TextInputLayout mPowerField;
    private TextInputLayout mAddress;
    private TextInputLayout mBatteryCapacityField;
    private ImageView mImageView;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_creation);
        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.new_panel));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initClassFields();
        initButton();
        initImageStub();
    }

    private void initClassFields() {
        mTypeField = findViewById(R.id.type_field);
        mPowerField = findViewById(R.id.power_field);
        mAddress = findViewById(R.id.address_field);
        mBatteryCapacityField = findViewById(R.id.battery_field);
        mImageView = findViewById(R.id.item_image);
        mProgressBar = findViewById(R.id.progress_bar);
    }

    private void initButton() {
        findViewById(R.id.btn_create_item).setOnClickListener(v -> {
            String type = Objects.requireNonNull(mTypeField.getEditText()).getText().toString();
            String power = Objects.requireNonNull(mPowerField.getEditText()).getText().toString();
            String address = Objects.requireNonNull(mAddress.getEditText()).getText().toString();
            String battery = Objects.requireNonNull(mBatteryCapacityField.getEditText()).getText().toString();
            addItem(type, power, address, battery);
        });
    }

    private void initImageStub() {
        Picasso.get().load(IMAGE_STUB).into(mImageView);
    }

    private void addItem(String type, String power, String address, String batteryCapacity) {
        PanelService service = ((ApplicationEx) Objects.requireNonNull(this.getApplication())).getRestService();
        Panel panel = new Panel(type, power, address, batteryCapacity);
        Call<Panel> call = service.createPanel(panel);
        mProgressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<Panel>() {

            @Override
            public void onResponse(Call<Panel> call, Response<Panel> response) {
                mProgressBar.setVisibility(View.INVISIBLE);
                if (response.isSuccessful()) {
                    openMainActivity();
                }
            }

            @Override
            public void onFailure(Call<Panel> call, Throwable throwable) {
                mProgressBar.setVisibility(View.INVISIBLE);
                Snackbar.make(findViewById(R.id.item_view), R.string.post_failed, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        openMainActivity();
        return true;
    }

    private void openMainActivity() {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
    }
}