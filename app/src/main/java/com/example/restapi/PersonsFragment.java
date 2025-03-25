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
import com.example.restapi.adapters.PersonAdapter;
import com.example.restapi.models.Person;
import com.example.restapi.tasks.PersonAsyncTask;
import com.example.restapi.viewmodels.SharedViewModel;
import java.util.List;

public class PersonsFragment extends Fragment {
    private RecyclerView recyclerView;
    private PersonAdapter adapter;
    private SharedViewModel viewModel;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_persons, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            viewModel.clearPersonList();
            adapter.notifyDataSetChanged();
            new PersonAsyncTask(adapter, viewModel.getPersonList()).execute();
            swipeRefreshLayout.setRefreshing(false);
        });

        recyclerView = view.findViewById(R.id.personsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new PersonAdapter(viewModel.getPersonList());
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = requireActivity().findViewById(R.id.startRandom);
        fab.setOnClickListener(v -> {
            new PersonAsyncTask(adapter, viewModel.getPersonList()).execute();
        });

        return view;
    }
} 