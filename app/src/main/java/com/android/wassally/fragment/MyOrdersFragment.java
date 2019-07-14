package com.android.wassally.fragment;


import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.wassally.R;
import com.android.wassally.activity.ClientHomeActivity;
import com.android.wassally.adapter.OrdersAdapter;
import com.android.wassally.helpers.NetworkUtils;
import com.android.wassally.helpers.PreferenceUtils;
import com.android.wassally.model.Order;
import com.android.wassally.networkUtils.UserClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyOrdersFragment extends Fragment {

    private CardView emptyCard;
    private CardView noInternetCard;

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
        noInternetCard = rootView.findViewById(R.id.no_internet_cardView);
        Button tryAgainBtn = rootView.findViewById(R.id.try_again_btn);


        progressBar = rootView.findViewById(R.id.progress);

        RecyclerView recyclerView =rootView.findViewById(R.id.rv_orders);
        adapter = new OrdersAdapter(getContext());
        recyclerView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        checkNetworkConnection();

        tryAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNetworkConnection();
            }
        });

        return rootView;
    }

    /**
     * before sending any network call it's better to check for network connection first
     * if there is no internet available display simple card view to inform user that there is no internet connection
     * with button to perform refresh or retry
     */

    private void checkNetworkConnection() {
        boolean isConnected = NetworkUtils.checkNetWorkConnectivity(getContext());
        if (isConnected){
            noInternetCard.setVisibility(View.INVISIBLE);
            getMyOrders();
        }else {
            noInternetCard.setVisibility(View.VISIBLE);
            final Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.main_coordinator_layout),"No Internet connection",Snackbar.LENGTH_LONG);
            snackbar.setAction("Dismiss", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();
        }
    }

    private void getMyOrders() {
        progressBar.setVisibility(View.VISIBLE);
        String token = "Token "+PreferenceUtils.getToken(getContext());

        //create retrofit instance
        Retrofit retrofit =NetworkUtils.createRetrofitInstance();
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
                noInternetCard.setVisibility(View.VISIBLE);

                Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.main_coordinator_layout),"Check network connection",Snackbar.LENGTH_LONG);
                snackbar.show();
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
