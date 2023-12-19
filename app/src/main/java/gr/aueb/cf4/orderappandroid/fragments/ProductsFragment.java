package gr.aueb.cf4.orderappandroid.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import gr.aueb.cf4.orderappandroid.R;
import gr.aueb.cf4.orderappandroid.adapters.ProductsAdapter;
import gr.aueb.cf4.orderappandroid.adapters.ProductsClickListener;
import gr.aueb.cf4.orderappandroid.viewModel.ProductsViewModel;

import java.util.ArrayList;

public class ProductsFragment extends Fragment {
    private ProductsViewModel viewModel;
    private ProductsAdapter productsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(ProductsViewModel.class);

        // Set up a callback for the back press
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                handleBackPress();
            }
        };
        // Add the callback to the back press dispatcher
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_products, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerViewCategories = view.findViewById(R.id.recyclerViewCategories);
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(getContext()));

        ProductsClickListener clickListener = new ProductsClickListener(viewModel, getParentFragmentManager(), requireContext());
        productsAdapter = new ProductsAdapter(requireContext(), new ArrayList<>(), clickListener);
        recyclerViewCategories.setAdapter(productsAdapter);

        observeCategories();
        observeSubcategories();
        observeProducts();
    }

    private void observeCategories() {
        viewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
            productsAdapter.setCategories(categories);
            productsAdapter.notifyDataSetChanged();
        });
    }

    private void observeSubcategories() {
        viewModel.getSubcategories().observe(getViewLifecycleOwner(), subcategories -> {
            productsAdapter.setSubcategories(subcategories);
            productsAdapter.notifyDataSetChanged();
        });
    }

    private void observeProducts() {
        viewModel.getProducts().observe(getViewLifecycleOwner(), products -> {
            productsAdapter.setProducts(products);
            productsAdapter.notifyDataSetChanged();
        });
    }

    private void handleBackPress() {
        ProductsFragment productsFragment = new ProductsFragment();
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, productsFragment, "ProductsFragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
