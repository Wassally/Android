package com.android.wassally.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.wassally.Constants;
import com.android.wassally.R;
import com.android.wassally.activity.ClientHomeActivity;
import com.android.wassally.activity.PackageSummaryActivity;
import com.android.wassally.adapter.OrdersAdapter;
import com.android.wassally.model.Addresses.Address;
import com.android.wassally.model.Addresses.Location;
import com.android.wassally.model.Addresses.PackageAddress;
import com.android.wassally.model.Order;
import com.android.wassally.networkUtils.UserClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyOrdersFragment extends Fragment {

    private CardView emptyCard;
    private OrdersAdapter adapter;
    private ProgressBar progressBar;


    public MyOrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((ClientHomeActivity)getActivity()).setActionBarTitle("Home");
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_orders, container, false);

        emptyCard = rootView.findViewById(R.id.empty_cardView);

        progressBar = rootView.findViewById(R.id.progress);

        RecyclerView recyclerView =rootView.findViewById(R.id.rv_orders);
        adapter = new OrdersAdapter(getContext());
        recyclerView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        getMyOrders();


        return rootView;
    }

    private void getMyOrders() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String token = "Token "+preferences.getString(Constants.AUTH_TOKEN,"");

        //create retrofit instance
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit =builder.build();
        UserClient client = retrofit.create(UserClient.class);
        Call<List<Order>> ordersCall = client.getMyOrders(token);
        ordersCall.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(@NonNull Call<List<Order>> call, @NonNull Response<List<Order>> response) {
                progressBar.setVisibility(View.INVISIBLE);
                if (response.isSuccessful() && response.body()!=null){
                    adapter.setOrdersData(response.body());
                    if(adapter.getItemCount()==0){
                        emptyCard.setVisibility(View.VISIBLE);
                    }else {
                        emptyCard.setVisibility(View.INVISIBLE);
                    }
                }else{
                    Toast.makeText(getContext(), "error getting packages", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), "some thing went wrong please try again!", Toast.LENGTH_SHORT).show();
                Log.e("mytag","some thing went wrong while getting orders"+t);

            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.my_orders_menu,menu);
    }*/
}
