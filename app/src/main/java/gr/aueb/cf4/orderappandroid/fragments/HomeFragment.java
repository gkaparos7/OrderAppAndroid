package gr.aueb.cf4.orderappandroid.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import gr.aueb.cf4.orderappandroid.R;
import gr.aueb.cf4.orderappandroid.utils.AuthManager;

public class HomeFragment extends Fragment {
    private TextView tvWelcomeMessage;

    private BottomNavigationView bottomNV;

    // Constructor to receive the reference of bottomNV
    public HomeFragment(BottomNavigationView bottomNV) {
        this.bottomNV = bottomNV;
    }

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tvWelcomeMessage = view.findViewById(R.id.tvWelcomeMessage);


        // Use AuthManager to get the authentication token
        AuthManager authManager = new AuthManager(requireContext());
        String userName = authManager.getLoggedInUsername();

        // Set the welcome message
        tvWelcomeMessage.setText("Welcome, " + userName + "!");

        // Set up click listeners for buttons
        Button btnProducts = view.findViewById(R.id.btnProducts);
        Button btnWishlist = view.findViewById(R.id.btnWishlist);
        Button btnOrders = view.findViewById(R.id.btnOrders);

        btnProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show ProductsFragment and hide others
                showFragment("ProductsFragment");
                // Update the selected item in BottomNavigationView
                bottomNV.setSelectedItemId(R.id.productsItem);
            }
        });

        btnWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show WishlistFragment and hide others
                showFragment("WishlistFragment");
                // Update the selected item in BottomNavigationView
                bottomNV.setSelectedItemId(R.id.wishlistItem);
            }
        });

        btnOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show OrderFragment and hide others
                showFragment("OrderFragment");
                // Update the selected item in BottomNavigationView
                bottomNV.setSelectedItemId(R.id.orderItem);
            }
        });
        return view;
    }
    private void showFragment(String tag) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        androidx.fragment.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Hide all fragments
        for (Fragment fragment : fragmentManager.getFragments()) {
            fragmentTransaction.hide(fragment);
        }

        // Show the selected fragment
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        if (fragment == null) {
            // If the fragment is not in the FragmentManager, add it
            if (tag.equals("ProductsFragment")) {
                fragment = new ProductsFragment();
            } else if (tag.equals("WishlistFragment")) {
                fragment = new WishlistFragment();
            } else if (tag.equals("OrderFragment")) {
                fragment = new OrdersFragment();
            }
            fragmentTransaction.add(R.id.frameLayout, fragment, tag);
        } else {
            // If the fragment is already in the FragmentManager, just show it
            fragmentTransaction.show(fragment);
        }

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
