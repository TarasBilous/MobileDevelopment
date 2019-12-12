package com.example.mobiledevelopment.fragments;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ProgressBar;
import android.content.IntentFilter;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.view.View;

import com.example.mobiledevelopment.ApplicationEx;
import com.example.mobiledevelopment.InternetConnectionReceiver;
import com.example.mobiledevelopment.Panel;
import com.example.mobiledevelopment.PanelService;
import com.example.mobiledevelopment.R;
import com.example.mobiledevelopment.RecyclerAdapter;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.recyclerview.widget.RecyclerView.VERTICAL;

public class PanelsListFragment extends Fragment {

    private RelativeLayout mMainLayout;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_panels_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initClassFields(view);
        initSwipeRefresh(view);
        initInternetConnectionReceiver();
        getServerData();
    }

    private void initClassFields(View view) {
        mMainLayout = view.findViewById(R.id.layout_main);
        mProgressBar = view.findViewById(R.id.progress_bar);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getActivity()), VERTICAL));
        mRecyclerView.setAdapter(new RecyclerAdapter(mMainLayout.getContext(), new ArrayList<>()));
    }

    private void initSwipeRefresh(View view) {
        SwipeRefreshLayout SwipeRefresh = view.findViewById(R.id.swipe_refresh);
        SwipeRefresh.setOnRefreshListener(() -> {
            getServerData();
            SwipeRefresh.setRefreshing(false);
        });
    }

    private void initInternetConnectionReceiver() {
        IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        InternetConnectionReceiver receiver = new InternetConnectionReceiver(mMainLayout);
        Objects.requireNonNull(getActivity()).registerReceiver(receiver, filter);
    }

    private void getServerData() {
        PanelService service = ((ApplicationEx) Objects.requireNonNull(getActivity())
                .getApplication()).getRestService();
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
                    Objects.requireNonNull(getActivity())
                            .getApplicationContext()
                            .getString(R.string.server_error),
                    response.code());
            Snackbar.make(mMainLayout, error, Snackbar.LENGTH_LONG).show();
        }
    }
}
