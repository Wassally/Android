package com.android.wassally.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.wassally.R;
import com.android.wassally.model.Order;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {

    private List<Order> mOrders;
    private Context context;

    private ListItemClickListener listItemClickListener;

    /**
     * The interface that receives onClick messages.
     */
    public interface ListItemClickListener{
        void onListItemClick(Order clickedOrder);
    }

    public OrdersAdapter(Context context,ListItemClickListener listener) {
        this.context = context;
        this.listItemClickListener = listener;


    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mStateImageView;
        private TextView mStateTextView;
        private TextView mStateCaptionTextView;
        private TextView mToLocationTextView;
        private TextView mToNameTextView;
        private TextView mCreatedAtTextView;


        ViewHolder(View itemView) {
            super(itemView);

            mStateImageView = itemView.findViewById(R.id.list_item_state_image);
            mStateTextView = itemView.findViewById(R.id.list_item_state_tv);
            mStateCaptionTextView = itemView.findViewById(R.id.list_item_state_caption);
            mToLocationTextView = itemView.findViewById(R.id.list_item_to_location_tv);
            mToNameTextView = itemView.findViewById(R.id.list_item_to_name_tv);
            mCreatedAtTextView = itemView.findViewById(R.id.list_item_created_at_tv);

            itemView.setOnClickListener(this);

        }
        /**
         * Called whenever a user clicks on an item in the list.
         * @param view The View that was clicked
         */
        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            Order clickedItem = mOrders.get(clickedPosition);
            listItemClickListener.onListItemClick(clickedItem);
        }

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.package_list_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       Order order = mOrders.get(position);
       holder.mStateTextView.setText(order.getState());
       holder.mToLocationTextView.setText(order.getPackageAddress().getToAddress().getFormatedAddress());
       holder.mToNameTextView.setText(order.getReceiverName());
       holder.mCreatedAtTextView.setText(order.getCreatedAt());

       String state = order.getState();
       switch (state){
           case "waiting":
               holder.mStateCaptionTextView.setText(context.getString(R.string.state_waiting_caption));
               holder.mStateImageView.setImageResource(R.drawable.state_waiting);
               break;
           case "pending":
               holder.mStateCaptionTextView.setText(context.getString(R.string.state_pending_caption));
               holder.mStateImageView.setImageResource(R.drawable.state_pending);
               break;
           case "on_way":
               holder.mStateCaptionTextView.setText(context.getString(R.string.state_on_way_caption));
               holder.mStateImageView.setImageResource(R.drawable.state_on_way);
               break;
           case "delivered":
               holder.mStateCaptionTextView.setText(context.getString(R.string.state_delivered_caption));
               holder.mStateImageView.setImageResource(R.drawable.state_delivered);
               break;
       }
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
