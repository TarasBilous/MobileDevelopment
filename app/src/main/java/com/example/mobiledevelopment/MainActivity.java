package com.example.mobiledevelopment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.recyclerview.widget.RecyclerView.VERTICAL;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout mMainLayout;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initClassFields();
        initSwipeRefresh();
        initInternetConnectionReceiver();
        getServerData();
    }

    private void initClassFields() {
        mMainLayout = findViewById(R.id.layout_main);
        mProgressBar = findViewById(R.id.progress_bar);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, VERTICAL));
        mRecyclerView.setAdapter(new RecyclerAdapter(new ArrayList<>()));
    }

    public void initSwipeRefresh() {
        SwipeRefreshLayout swipeRefresh = findViewById(R.id.swipe_refresh);
        swipeRefresh.setOnRefreshListener(() -> {
            getServerData();
            swipeRefresh.setRefreshing(false);
        });
    }

    private void initInternetConnectionReceiver() {
        IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        InternetConnectionReceiver receiver = new InternetConnectionReceiver(mMainLayout);
        this.registerReceiver(receiver, filter);
    }

    private void getServerData() {
        PanelService service = ((ApplicationEx) getApplication()).getRestService();
        Call<List<Panel>> call = service.getServerPanels();
        mProgressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<List<Panel>>() {

            @Override
            public void onResponse(Call<List<Panel>> call, Response<List<Panel>> response) {
                processResponse(response);
            }

            @Override
            public void onFailure(Call<List<Panel>> call, Throwable throwable) {
                mProgressBar.setVisibility(View.INVISIBLE);
                Snackbar.make(mMainLayout, R.string.fetching_failed, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void processResponse(Response<List<Panel>> response) {
        mProgressBar.setVisibility(View.INVISIBLE);
        RecyclerAdapter adapter = (RecyclerAdapter) mRecyclerView.getAdapter();
        if (adapter != null && response.isSuccessful()) {
            adapter.updatePanels(response.body());
        } else {
            String error = String.format(
                    getApplicationContext().getString(R.string.server_error),
                    response.code());
            Snackbar.make(mMainLayout, error, Snackbar.LENGTH_LONG).show();
        }
    }
}