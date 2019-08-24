package com.android.wassally.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.wassally.Constants;
import com.android.wassally.R;
import com.android.wassally.activity.ClientHomeActivity;
import com.android.wassally.activity.PackageSummaryActivity;
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
public class MyOrdersFragment extends Fragment implements OrdersAdapter.ListItemClickListener {

    private CardView emptyCard;
    private CardView noInternetCard;
    private LinearLayout stateEmptyLayout;
    private TextView stateEmptyTextView;
    private RecyclerView recyclerView;

    private OrdersAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;


    public MyOrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((ClientHomeActivity) getActivity()).setActionBarTitle("My orders");
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_orders, container, false);

        emptyCard = rootView.findViewById(R.id.empty_cardView);
        noInternetCard = rootView.findViewById(R.id.no_internet_cardView);
        stateEmptyLayout = rootView.findViewById(R.id.state_empty);
        stateEmptyTextView = rootView.findViewById(R.id.tv_state_empty);

        Button tryAgainBtn = rootView.findViewById(R.id.try_again_btn);
        swipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh);
        recyclerView = rootView.findViewById(R.id.rv_orders);

        adapter = new OrdersAdapter(getContext(), MyOrdersFragment.this);
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

        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMyOrders("");
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
        if (isConnected) {
            getMyOrders("");
        } else {
            whatToDisplay(Constants.DISPLAY_STATE_NO_INTERNET);

        }
    }

    private void getMyOrders(final String deliveryState) {
        swipeRefreshLayout.setRefreshing(true);
        String token = "Token " + PreferenceUtils.getToken(getContext());

        //create retrofit instance
        Retrofit retrofit = NetworkUtils.createRetrofitInstance();
        UserClient client = retrofit.create(UserClient.class);
        Call<List<Order>> ordersCall = client.getMyOrders(token, deliveryState);
        ordersCall.enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(@NonNull Call<List<Order>> call, @NonNull Response<List<Order>> response) {
                adapter.setOrdersData(null);

                //response is successful
                if (response.isSuccessful() && response.body() != null) {
                    // list of orders is not empty
                    if (response.body().size() > 0) {
                        adapter.setOrdersData(response.body());
                        whatToDisplay(Constants.DISPLAY_STATE_ORDERS_OK);
                    } else {
                        //list of orders is empty
                        if (deliveryState.equals("")) {
                            whatToDisplay(Constants.DISPLAY_STATE_ALL_ORDERS_EMPTY);
                        } else {
                            whatToDisplay(Constants.DISPLAY_STATE_FILTER_ORDERS_EMPTY);
                            switch (deliveryState) {
                                case "waiting":
                                    stateEmptyTextView.setText(getString(R.string.no_waiting_orders));
                                    break;
                                case "pending":
                                    stateEmptyTextView.setText(getString(R.string.no_pending_orders));
                                    break;
                                case "on_way":
                                    stateEmptyTextView.setText(getString(R.string.no_on_way_orders));
                            }
                        }
                    }
                } else {

                    Toast.makeText(getContext(), "error getting packages", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Log.e("mytag", "error" + t);

                Toast.makeText(getContext(), "something went wrong, please try again", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onListItemClick(Order clickedOrder) {
        Intent intent = new Intent(getContext(), PackageSummaryActivity.class);
        intent.putExtra(Constants.ORDER_EXTRA, clickedOrder);
        intent.putExtra("display_details_only", 5);
        startActivity(intent);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.my_orders_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.waiting:
                getMyOrders("waiting");
                break;
            case R.id.pending:
                getMyOrders("pending");
                break;
            case R.id.on_way:
                getMyOrders("on_way");
            default:
                getMyOrders("");

        }
        return true;
    }

    private void whatToDisplay(int displayState) {
        swipeRefreshLayout.setRefreshing(false);

        switch (displayState) {
            case Constants.DISPLAY_STATE_NO_INTERNET:

                noInternetCard.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
                emptyCard.setVisibility(View.INVISIBLE);
                stateEmptyLayout.setVisibility(View.INVISIBLE);

                final Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.main_coordinator_layout), "No Internet connection", Snackbar.LENGTH_LONG);
                snackbar.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
                break;

            case Constants.DISPLAY_STATE_ORDERS_OK:
                noInternetCard.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
                emptyCard.setVisibility(View.INVISIBLE);
                stateEmptyLayout.setVisibility(View.INVISIBLE);
                break;

            case Constants.DISPLAY_STATE_ALL_ORDERS_EMPTY:
                noInternetCard.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
                emptyCard.setVisibility(View.VISIBLE);
                stateEmptyLayout.setVisibility(View.INVISIBLE);
                break;

            case Constants.DISPLAY_STATE_FILTER_ORDERS_EMPTY:
                noInternetCard.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
                emptyCard.setVisibility(View.INVISIBLE);
                stateEmptyLayout.setVisibility(View.VISIBLE);
        }

    }
}
