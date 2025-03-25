package com.example.restapi;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.restapi.adapters.WantedAdapter;
import com.example.restapi.models.WantedPerson;
import com.example.restapi.tasks.WantedAsyncTask;
import com.example.restapi.viewmodels.SharedViewModel;
import java.util.List;

public class WantedFragment extends Fragment {
    private static final String TAG = "WantedFragment";
    private RecyclerView recyclerView;
    private WantedAdapter adapter;
    private SharedViewModel viewModel;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Creating WantedFragment");
        View view = inflater.inflate(R.layout.fragment_wanted, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            viewModel.clearWantedList();
            adapter.notifyDataSetChanged();
            new WantedAsyncTask(adapter, viewModel.getWantedList()).execute();
            swipeRefreshLayout.setRefreshing(false);
        });

        recyclerView = view.findViewById(R.id.wantedRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new WantedAdapter(viewModel.getWantedList());
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = requireActivity().findViewById(R.id.startRandom);
        fab.setOnClickListener(v -> {
            Log.d(TAG, "FAB clicked: Starting WantedAsyncTask");
            new WantedAsyncTask(adapter, viewModel.getWantedList()).execute();
        });

        Log.d(TAG, "onCreateView: WantedFragment setup complete");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: WantedFragment resumed");
    }
} 