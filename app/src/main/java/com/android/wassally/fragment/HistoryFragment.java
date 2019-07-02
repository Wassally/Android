package com.android.wassally.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.wassally.R;
import com.android.wassally.activity.ClientHomeActivity;
import com.android.wassally.adapter.OrdersAdapter;
import com.android.wassally.model.Addresses.Address;
import com.android.wassally.model.Addresses.Location;
import com.android.wassally.model.Addresses.PackageAddress;
import com.android.wassally.model.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {


    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((ClientHomeActivity)getActivity()).setActionBarTitle("History");


        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.rv_orders);
        OrdersAdapter adapter = new OrdersAdapter(getContext());
        recyclerView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        Location toLocation = new Location(11.25, 125.21);
        Address toAddress = new Address(toLocation, "Rehab Mall 2, Cairo Governorate, Egypt", "adgadgdg");
        PackageAddress packageAddress = new PackageAddress(toAddress, toAddress);


        List<Order> orders = new ArrayList<>();

        adapter.setOrdersData(orders);

        return rootView;
    }

}
