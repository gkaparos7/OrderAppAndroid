package gr.aueb.cf4.orderappandroid.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.List;

import gr.aueb.cf4.orderappandroid.R;
import gr.aueb.cf4.orderappandroid.adapters.WishlistItemAdapter;
import gr.aueb.cf4.orderappandroid.models.Wishlist;
import gr.aueb.cf4.orderappandroid.models.WishlistItem;
import gr.aueb.cf4.orderappandroid.requests.WishlistClearRequest;
import gr.aueb.cf4.orderappandroid.requests.WishlistRequest;
import gr.aueb.cf4.orderappandroid.requests.WishlistToOrderRequest;

public class WishlistFragment extends Fragment {

    private WishlistItemAdapter wishlistItemAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);

        // Initialize RecyclerView and Adapter
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewWishlistItems);
        wishlistItemAdapter = new WishlistItemAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(wishlistItemAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                WishlistItem wishlistItem = wishlistItemAdapter.getWishlistItems().get(position);
                Log.d("WishlistItemAdapter", "Swipe detected. Deleting item at position: " + position);
                WishlistItemAdapter.ItemViewHolder.deleteWishlistItem(requireContext(), wishlistItemAdapter, wishlistItem, position);
            }
        };

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        // Button click listeners
        Button createOrderButton = view.findViewById(R.id.btnCreateOrder);
        createOrderButton.setOnClickListener(v -> onOrderFromWishlistClick(wishlistItemAdapter.getWishlistItems()));

        Button clearWishlistButton = view.findViewById(R.id.btnClearWishlist);
        clearWishlistButton.setOnClickListener(v -> onClearWishlistClick());

        // Fetch wishlist data
        fetchWishlistData();

        return view;
    }

    private void fetchWishlistData() {
        WishlistRequest.fetchWishlistForCurrentUser(requireContext(),
                new Response.Listener<Wishlist>() {
                    @Override
                    public void onResponse(Wishlist wishlist) {
                        // Update the adapter with the new wishlist data
                        updateWishlistAdapter(wishlist.getWishlistItems());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(requireContext(), "Error fetching wishlist data", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                });
    }

    private void updateWishlistAdapter(List<WishlistItem> wishlistItems) {
        wishlistItemAdapter.setWishlistItems(wishlistItems);
    }

    private void onOrderFromWishlistClick(List<WishlistItem> wishlistItems) {
        WishlistToOrderRequest.createOrderFromWishlist(
                requireContext(),
                wishlistItems,
                response -> {
                    // Handle success, e.g., update UI
                    Log.d("WishlistToOrderRequest", "Order created successfully");
                    Toast.makeText(requireContext(),"Order created from wishlist successfully", Toast.LENGTH_SHORT).show();

                    // Clear the wishlist after a successful order creation
                    onClearWishlistClick();
                },
                error -> {
                    // Handle error, e.g., display an error message
                    Toast.makeText(requireContext(),"Order failed to be created from wishlist...", Toast.LENGTH_SHORT).show();
                    Log.e("WishlistToOrderRequest", "Failed to create order from wishlist: " + error.toString());
                });
    }

    private void onClearWishlistClick() {
        // Call the clearWishlistForCurrentUser method
        WishlistClearRequest.clearWishlistForCurrentUser(
                requireContext(),
                clearedWishlist -> {
                    Toast.makeText(requireContext(),"Wishlist cleared successfully", Toast.LENGTH_SHORT).show();
                    fetchWishlistData();
                },
                error -> {
                    Toast.makeText(requireContext(),"Wishlist couldn't be cleared...", Toast.LENGTH_SHORT).show();
                });
    }
}
