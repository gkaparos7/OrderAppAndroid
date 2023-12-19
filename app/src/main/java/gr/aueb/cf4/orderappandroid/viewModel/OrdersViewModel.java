package gr.aueb.cf4.orderappandroid.viewModel;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import gr.aueb.cf4.orderappandroid.models.Order;
import gr.aueb.cf4.orderappandroid.models.OrderItem;
import gr.aueb.cf4.orderappandroid.requests.OrderItemsRequest;
import gr.aueb.cf4.orderappandroid.requests.OrdersRequest;

public class OrdersViewModel extends AndroidViewModel {
    private final MutableLiveData<List<Order>> orders;
    private final MutableLiveData<List<OrderItem>> orderItems;

    public OrdersViewModel(@NonNull Application application) {
        super(application);
        orders = new MutableLiveData<>();
        orderItems = new MutableLiveData<>();
        fetchOrdersForCurrentUser(application.getApplicationContext()); // Load initial orders
    }

    public LiveData<List<Order>> getOrders() {
        return orders;
    }

    public void fetchOrdersForCurrentUser(Context context) {
        OrdersRequest.fetchOrdersForCurrentUser(
                context,
                ordersArray -> {
                    List<Order> orderList = Arrays.asList(ordersArray);

                    // Reverse the order of the list
                    Collections.reverse(orderList);

                    orders.setValue(orderList);
                    Log.d("OrdersViewModel", "Orders fetched successfully: " + orderList.size() + " orders");
                },
                error -> {
                    // Handle error, e.g., update orders LiveData with an empty list
                    orders.setValue(Collections.emptyList());
                    Log.e("OrdersViewModel", "Error fetching orders: " + error.toString());
                }
        );
    }

    public void fetchOrderItemsForCurrentOrder(Context context, long orderId) {
        OrderItemsRequest.fetchOrderItemsByOrderId(
                context,
                orderId,
                orderItemsList -> {
                    // Assuming the response is a list of OrderItem
                    orderItems.postValue(orderItemsList);
                    Log.d("OrdersViewModel", "Order items fetched successfully: " + orderItemsList.size() + " items");
                },
                error -> {
                    // Handle error, e.g., update order items LiveData with an empty list
                    orderItems.postValue(Collections.emptyList());
                    Log.e("OrdersViewModel", "Error fetching order items: " + error.toString());
                }
        );
    }


    public LiveData<List<OrderItem>> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> items) {
        orderItems.setValue(items);
    }
}

