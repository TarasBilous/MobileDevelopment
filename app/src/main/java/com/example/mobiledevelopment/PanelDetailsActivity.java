package com.example.mobiledevelopment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class PanelDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        loadPanelData();
    }

    private void loadPanelData() {
        Intent intent = getIntent();
        if (intent.hasExtra("panel_type") &&
                intent.hasExtra("power") &&
                intent.hasExtra("address") &&
                intent.hasExtra("battery_capacity") &&
                intent.hasExtra("image")) {
            String PanelType = intent.getStringExtra("panel_type");
            String address = intent.getStringExtra("address");
            String power = intent.getStringExtra("power");
            String batteryCapacity = intent.getStringExtra("battery_capacity");
            String image = intent.getStringExtra("image");

            showPanelDetails(PanelType, address, power, batteryCapacity, image);
        }
    }

    private void showPanelDetails(String PanelType, String address, String power,
                                  String batteryCapacity, String image) {
        TextView typeText = findViewById(R.id.type_details);
        TextView addressText = findViewById(R.id.address_details);
        TextView powerText = findViewById(R.id.power_details);
        TextView batteryCapacityText = findViewById(R.id.battery_details);
        ImageView imageView = findViewById(R.id.img_details);
        Picasso.get().load(image).into(imageView);
        typeText.append(PanelType);
        addressText.append(address);
        powerText.append(power);
        batteryCapacityText.append(batteryCapacity);
    }
}
