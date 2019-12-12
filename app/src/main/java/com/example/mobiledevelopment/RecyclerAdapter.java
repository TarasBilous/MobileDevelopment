package com.example.mobiledevelopment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.PanelViewHolder> {

    private List<Panel> mPanels;
    private Context mContext;

    public RecyclerAdapter(Context context, List<Panel> panels) {
        this.mPanels = panels;
        this.mContext = context;
    }

    @Override
    public int getItemCount() {
        return mPanels.size();
    }

    @NonNull
    @Override
    public PanelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_panel,
                parent, false);
        return new PanelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PanelViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        holder.mListLayout.setOnClickListener(view -> openPanelDetails(position));
        holder.mPanelType.setText(mPanels.get(position).panelType);
        holder.mPower.setText(String.format(context.getString(R.string.power), String.valueOf(mPanels.get(position).power)));
        holder.mAddress.setText(String.format(context.getString(R.string.address), mPanels.get(position).address));
        holder.mBatteryCapacity.setText(String.valueOf(mPanels.get(position).batteryCapacity));
        Picasso.get().load(mPanels.get(position).img).into(holder.mImage);
    }

    private void openPanelDetails(int position){
        Intent intent = new Intent(mContext, PanelDetailsActivity.class);
        intent.putExtra("panel_type", mPanels.get(position).panelType);
        intent.putExtra("power", String.valueOf(mPanels.get(position).power));
        intent.putExtra("address", mPanels.get(position).address);
        intent.putExtra("battery_capacity", String.valueOf(mPanels.get(position).batteryCapacity));
        intent.putExtra("image", mPanels.get(position).img);
        mContext.startActivity(intent);
    }

    public void updatePanels(List<Panel> panels) {
        this.mPanels = panels;
        notifyDataSetChanged();
    }

    public static class PanelViewHolder extends RecyclerView.ViewHolder {

        private TextView mPanelType;
        private TextView mPower;
        private TextView mAddress;
        private TextView mBatteryCapacity;
        private ImageView mImage;
        private RelativeLayout mListLayout;

        PanelViewHolder(View v) {
            super(v);
            mPanelType = v.findViewById(R.id.panel_type);
            mPower = v.findViewById(R.id.power);
            mAddress = v.findViewById(R.id.address);
            mBatteryCapacity = v.findViewById(R.id.battery_capacity);
            mImage = v.findViewById(R.id.image_panel);
            mListLayout = v.findViewById(R.id.list_layout);
        }
    }
}
