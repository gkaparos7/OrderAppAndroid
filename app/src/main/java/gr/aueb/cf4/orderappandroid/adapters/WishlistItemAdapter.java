package gr.aueb.cf4.orderappandroid.adapters;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;
import gr.aueb.cf4.orderappandroid.R;
import gr.aueb.cf4.orderappandroid.models.Size;
import gr.aueb.cf4.orderappandroid.models.WishlistItem;
import gr.aueb.cf4.orderappandroid.requests.WishlistItemDeleteRequest;

public class WishlistItemAdapter extends RecyclerView.Adapter<WishlistItemAdapter.ItemViewHolder> {

    private final List<WishlistItem> wishlistItems;

    public WishlistItemAdapter() {
        this.wishlistItems = new ArrayList<>();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wishlist_item, parent, false);
        return new ItemViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        WishlistItem wishlistItem = wishlistItems.get(position);
        holder.bind(wishlistItem);
    }

    @Override
    public int getItemCount() {
        return wishlistItems.size();
    }

    public void setWishlistItems(List<WishlistItem> wishlistItems) {
        this.wishlistItems.clear();
        if (wishlistItems != null) {
            this.wishlistItems.addAll(wishlistItems);
        }
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        wishlistItems.remove(position);
        notifyItemRemoved(position);
    }

    public List<WishlistItem> getWishlistItems() {
        return wishlistItems;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView productTV;
        TextView sizeTV;
        TextView quantityTV;
        WishlistItemAdapter wishlistItemAdapter;

        public ItemViewHolder(@NonNull View itemView, WishlistItemAdapter adapter) {
            super(itemView);
            wishlistItemAdapter = adapter;
            productTV = itemView.findViewById(R.id.productTV);
            sizeTV = itemView.findViewById(R.id.sizeTV);
            quantityTV = itemView.findViewById(R.id.quantityTV);
        }

        public void bind(WishlistItem wishlistItem) {
            // Bind data to views
            productTV.setText(wishlistItem.getProduct().getName());

            // Check if the size display name is provided
            String sizeDisplayName = wishlistItem.getSizeDisplayName();
            if (sizeDisplayName != null) {
                // Use the helper method to get Size from display name
                Size sizeFromDisplayName = Size.fromDisplayName(sizeDisplayName);
                if (sizeFromDisplayName != null) {
                    sizeTV.setText(sizeFromDisplayName.getDisplayName());
                } else {
                    sizeTV.setText("N/A");
                }
            } else {
                sizeTV.setText("N/A");
            }

            quantityTV.setText(String.valueOf(wishlistItem.getQuantity()));
        }

        public static void deleteWishlistItem(
                Context context,
                WishlistItemAdapter wishlistItemAdapter,
                WishlistItem wishlistItem,
                int position) {
            Log.d("WishlistItemAdapter", "Deleting item: " + wishlistItem.getId());
            WishlistItemDeleteRequest.deleteWishlistItem(
                    context,
                    wishlistItem,
                    response -> {
                        // Handle success, e.g., update UI
                        wishlistItemAdapter.deleteItem(position);
                        Toast.makeText(context, "Wishlist item deleted successfully", Toast.LENGTH_SHORT).show();
                        Log.d("WishlistItemDeleteRequest", "Wishlist item deleted successfully");
                    },
                    error -> {
                        // Handle error, e.g., display an error message
                        Toast.makeText(context, "Failed to delete wishlist item: " + error.toString(), Toast.LENGTH_SHORT).show();
                        Log.e("WishlistItemDeleteRequest", "Failed to delete wishlist item: " + error.toString());
                    });
        }

    }
}
