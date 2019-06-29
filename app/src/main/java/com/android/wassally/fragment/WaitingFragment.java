package com.android.wassally.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.wassally.R;
import com.android.wassally.adapter.PendingOrdersAdapter;
import com.android.wassally.model.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class WaitingFragment extends Fragment {


    public WaitingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView= inflater.inflate(R.layout.fragment_waiting, container, false);

        RecyclerView recyclerView =rootView.findViewById(R.id.rv_pp);
        PendingOrdersAdapter adapter = new PendingOrdersAdapter();
        recyclerView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);


        List<Order> orders = new ArrayList<>();

        //adapter.setOrdersData(orders);

        return rootView;

    }

}
