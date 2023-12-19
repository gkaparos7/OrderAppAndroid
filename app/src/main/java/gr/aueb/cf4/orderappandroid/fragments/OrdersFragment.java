package gr.aueb.cf4.orderappandroid.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import gr.aueb.cf4.orderappandroid.R;
import gr.aueb.cf4.orderappandroid.adapters.OrderClickListener;
import gr.aueb.cf4.orderappandroid.adapters.OrdersAdapter;
import gr.aueb.cf4.orderappandroid.models.Order;
import gr.aueb.cf4.orderappandroid.models.OrderItem;
import gr.aueb.cf4.orderappandroid.viewModel.OrdersViewModel;

public class OrdersFragment extends Fragment implements OrderClickListener {
    private OrdersViewModel ordersViewModel;
    private OrdersAdapter ordersAdapter;
    public static boolean isShowingOrderItems = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ordersViewModel = new ViewModelProvider(this).get(OrdersViewModel.class);

        // Set up a callback for the back press
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                handleBackPress();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView ordersRecyclerView = view.findViewById(R.id.ordersRecyclerView);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ordersAdapter = new OrdersAdapter(new ArrayList<>(), this);
        ordersRecyclerView.setAdapter(ordersAdapter);

        observeOrders();
    }

    private void observeOrders() {
        ordersViewModel.getOrders().observe(getViewLifecycleOwner(), new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orders) {
                isShowingOrderItems = false;
                ordersAdapter.setOrders(orders);
                ordersAdapter.notifyDataSetChanged();
            }
        });
    }

    private void observeOrderItems(long orderId) {
        ordersViewModel.getOrderItems().observe(getViewLifecycleOwner(), new Observer<List<OrderItem>>() {
            @Override
            public void onChanged(List<OrderItem> orderItems) {
                isShowingOrderItems = true;
                ordersAdapter.setOrderItems(orderItems);
                ordersAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onOrderClicked(Order order) {
        // Update the OrderItems LiveData with the order items from the clicked order
        ordersViewModel.fetchOrderItemsForCurrentOrder(requireContext(), order.getId());
        // Observe order items for the clicked order
        observeOrderItems(order.getId());
    }

    private void handleBackPress() {
        if (isShowingOrderItems) {
            // If showing order items, set isShowingOrderItems to false and refresh the orders
            isShowingOrderItems = false;
            observeOrders();
        } else {
            // If not showing order items, pop the back stack
            getParentFragmentManager().popBackStack();
        }
    }

}
