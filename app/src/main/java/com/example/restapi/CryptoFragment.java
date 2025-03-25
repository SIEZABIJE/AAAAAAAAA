package com.example.restapi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.restapi.adapters.CryptoAdapter;
import com.example.restapi.models.Crypto;
import com.example.restapi.tasks.CryptoAsyncTask;
import com.example.restapi.viewmodels.SharedViewModel;
import java.util.List;

public class CryptoFragment extends Fragment {
    private RecyclerView recyclerView;
    private CryptoAdapter adapter;
    private SharedViewModel viewModel;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crypto, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            viewModel.clearCryptoList();
            adapter.notifyDataSetChanged();
            new CryptoAsyncTask(adapter, viewModel.getCryptoList()).execute();
            swipeRefreshLayout.setRefreshing(false);
        });

        recyclerView = view.findViewById(R.id.cryptoRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new CryptoAdapter(viewModel.getCryptoList());
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = requireActivity().findViewById(R.id.startRandom);
        fab.setOnClickListener(v -> {
            new CryptoAsyncTask(adapter, viewModel.getCryptoList()).execute();
        });

        return view;
    }
} 