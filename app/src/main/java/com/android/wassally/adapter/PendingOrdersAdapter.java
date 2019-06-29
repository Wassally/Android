package com.android.wassally.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.wassally.R;
import com.android.wassally.model.Order;

import java.util.List;

public class PendingOrdersAdapter extends RecyclerView.Adapter<PendingOrdersAdapter.ViewHolder> {


    private List<Order> mOrders;


    /**
     * The interface that receives onClick messages.
     */
    public interface ListItemClickListener{
        void onListItemClick(Order clickedOrder);
    }

    public PendingOrdersAdapter() {

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView requestNumber;
        private TextView receiverName;
        private TextView toAddress;

        public ViewHolder(View itemView) {
            super(itemView);
           // requestNumber = itemView.findViewById(R.id.tv_pp_requests_number);
            receiverName = itemView.findViewById(R.id.tv_pp_toPerson_display);
            toAddress = itemView.findViewById(R.id.tv_pp_location_display);

           // itemView.setOnClickListener(this);

        }
        /**
         * Called whenever a user clicks on an item in the list.
         * @param view The View that was clicked

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            Order clickedItem = mOrders.get(clickedPosition);
            //mOnClickListener.onListItemClick(clickedItem);
        }
    */
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.pending_package_list_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       Order order = mOrders.get(position);

        //will be implemented in the second release
        //TextView requestNumberTextView = holder.requestNumber;
        //requestNumberTextView.setText(String.valueOf(order.getRequestNumber()));

        TextView receiverNameTextView = holder.receiverName;
        TextView toAddressTextView = holder.toAddress;

        receiverNameTextView.setText(order.getReceiverName());

    }

    @Override
    public int getItemCount() {
        if(null== mOrders){
            return 0;
        }
        return mOrders.size();
    }


    /**
     * This method is used to set the Pending orders data , if we've already
     * created one. This is handy when we get new data from the web but don't want to create a
     * new MovieAdapter to display it.
     *
     * @param orders The new pending orders data to be displayed.
     */
    public void setOrdersData(List<Order> orders) {
        mOrders = orders;
        notifyDataSetChanged();
    }

}
