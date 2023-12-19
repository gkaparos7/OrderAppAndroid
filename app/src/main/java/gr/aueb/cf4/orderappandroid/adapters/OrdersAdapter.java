package gr.aueb.cf4.orderappandroid.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import gr.aueb.cf4.orderappandroid.R;
import gr.aueb.cf4.orderappandroid.fragments.OrdersFragment;
import gr.aueb.cf4.orderappandroid.models.Order;
import gr.aueb.cf4.orderappandroid.models.OrderItem;
import gr.aueb.cf4.orderappandroid.models.Size;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {
    private List<Order> orderList;
    private List<OrderItem> orderItemsList;
    private final OrderClickListener orderClickListener;

    public OrdersAdapter(List<Order> orderList, OrderClickListener orderClickListener) {
        this.orderList = orderList;
        this.orderClickListener = orderClickListener;
        this.orderItemsList = new ArrayList<>();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == Order.class.hashCode()) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_view, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderitem_view, parent, false);
        }
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        if (orderItemsList.isEmpty()) {
            Order order = orderList.get(position);
            holder.bind(order);
            Log.d("OrdersAdapter", "Order bound: " + order.getId());
        } else {
            if (position < orderItemsList.size()) {
                OrderItem orderItem = orderItemsList.get(position);
                holder.bind(orderItem);
            } else {
                Log.e("OrdersAdapter", "Invalid position for orderItemsList: " + position);
            }
        }
    }


    @Override
    public int getItemCount() {
        return OrdersFragment.isShowingOrderItems && !orderItemsList.isEmpty() ? orderItemsList.size() : orderList.size();
    }


    public void setOrders(List<Order> orderList) {
        this.orderList = orderList;
        notifyDataSetChanged();
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        Collections.reverse(orderList);
        this.orderItemsList = orderItems;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        // Return a unique identifier for each type of item
        if (orderItemsList.isEmpty()) {
            return Order.class.hashCode();
        } else {
            return OrderItem.class.hashCode();
        }
    }
    class OrderViewHolder extends RecyclerView.ViewHolder {
        private final TextView orderTitleTextView;
        private final TextView orderDateTextView;
        private final TextView productNameTextView;
        private final TextView productSizeTextView;
        private final TextView productQuantityTextView;

        OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderTitleTextView = itemView.findViewById(R.id.orderTitleTV);
            orderDateTextView = itemView.findViewById(R.id.orderDateTV);
            productNameTextView = itemView.findViewById(R.id.productNameTV);
            productSizeTextView = itemView.findViewById(R.id.productSizeTV);
            productQuantityTextView = itemView.findViewById(R.id.productQuantityTV);

            itemView.setOnClickListener(v -> {
                if (orderClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        orderClickListener.onOrderClicked(orderList.get(position));
                    }
                }
            });
        }

        void bind(Order order) {
            orderTitleTextView.setText("Order #" + order.getId());

            // Check if the order date is not null before formatting
            if (order.getOrderDate() != null) {
                // Format the date
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String formattedDate = dateFormat.format(order.getOrderDate());

                orderDateTextView.setText(formattedDate);
            } else {
                orderDateTextView.setText("No date available");
            }
        }

        void bind(OrderItem orderItem) {
            // Check if orderItem or its product is null
            if (orderItem == null || orderItem.getProduct() == null) {
                // Handle the case where orderItem or its product is null
                productNameTextView.setText("N/A");
                productSizeTextView.setText("N/A");
                productQuantityTextView.setText("N/A");
                return;
            }

            // Set product name
            productNameTextView.setText(orderItem.getProduct().getName());

            // Set product size
            String sizeDisplayName = orderItem.getSizeDisplayName();
            if (sizeDisplayName != null) {
                // Use the helper method to get Size from display name
                Size sizeFromDisplayName = Size.fromDisplayName(sizeDisplayName);
                if (sizeFromDisplayName != null) {
                    productSizeTextView.setText(sizeFromDisplayName.getDisplayName());
                } else {
                    productSizeTextView.setText("N/A");
                }
            } else {
                productSizeTextView.setText("N/A");
            }

            // Set product quantity
            productQuantityTextView.setText(String.valueOf(orderItem.getQuantity()));
        }

    }
}
