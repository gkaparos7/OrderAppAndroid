package gr.aueb.cf4.orderappandroid.adapters;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import gr.aueb.cf4.orderappandroid.R;
import gr.aueb.cf4.orderappandroid.models.Category;
import gr.aueb.cf4.orderappandroid.models.Product;
import gr.aueb.cf4.orderappandroid.models.Size;
import gr.aueb.cf4.orderappandroid.models.Subcategory;
import gr.aueb.cf4.orderappandroid.requests.ToWishlistRequest;

public class ProductsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEW_TYPE_CATEGORY = 1;
    public static final int VIEW_TYPE_SUBCATEGORY = 2;
    public static final int VIEW_TYPE_PRODUCT = 3;

    private final Context context;
    private final List<Object> items;
    private final ProductClickListener productClickListener;

    private int currentItemType = VIEW_TYPE_CATEGORY;

    public ProductsAdapter(Context context, List<Object> items, ProductClickListener productClickListener) {
        this.context = context;
        this.items = items;
        this.productClickListener = productClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case VIEW_TYPE_CATEGORY:
                View categoryView = inflater.inflate(R.layout.category_item_view, parent, false);
                return new CategoryViewHolder(categoryView);

            case VIEW_TYPE_SUBCATEGORY:
                View subcategoryView = inflater.inflate(R.layout.subcategory_item_view, parent, false);
                return new SubcategoryViewHolder(subcategoryView);

            case VIEW_TYPE_PRODUCT:
                View productView = inflater.inflate(R.layout.product_view, parent, false);
                return new ProductViewHolder(productView);

            default:
                throw new IllegalArgumentException("Invalid view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = items.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_CATEGORY:
                CategoryViewHolder categoryViewHolder = (CategoryViewHolder) holder;
                Category category = (Category) item;
                categoryViewHolder.bind(category);
                break;

            case VIEW_TYPE_SUBCATEGORY:
                SubcategoryViewHolder subcategoryViewHolder = (SubcategoryViewHolder) holder;
                Subcategory subcategory = (Subcategory) item;
                subcategoryViewHolder.bind(subcategory);
                break;

            case VIEW_TYPE_PRODUCT:
                ProductViewHolder productViewHolder = (ProductViewHolder) holder;
                Product product = (Product) item;
                productViewHolder.bind(product);
                break;

            default:
                throw new IllegalArgumentException("Invalid view type");
        }
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        Object item = items.get(position);

        if (item instanceof Category) {
            currentItemType = VIEW_TYPE_CATEGORY;
            return VIEW_TYPE_CATEGORY;
        } else if (item instanceof Subcategory) {
            currentItemType = VIEW_TYPE_SUBCATEGORY;
            return VIEW_TYPE_SUBCATEGORY;
        } else if (item instanceof Product) {
            currentItemType = VIEW_TYPE_PRODUCT;
            return VIEW_TYPE_PRODUCT;
        } else {
            throw new IllegalArgumentException("Invalid item type");
        }
    }

    // ViewHolder for Categories
    private class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final ImageView categoryImageView;
        private final TextView categoryNameTextView;

        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryImageView = itemView.findViewById(R.id.imageCategory);
            categoryNameTextView = itemView.findViewById(R.id.textCategoryName);
        }

        void bind(Category category) {
            // Bind category data to views
            categoryNameTextView.setText(category.getName());

            // Load image using Picasso
            Picasso.get()
                    .load(category.getPhotoUrl())
                    .placeholder(R.drawable.ic_product)  // Optional placeholder image while loading
                    .error(R.drawable.ic_error)  // Optional error image if the loading fails
                    .into(categoryImageView);

            // Set onClickListener to handle category click
            itemView.setOnClickListener(v -> {
                if (productClickListener != null) {
                    productClickListener.onCategoryClicked(category);
                }
            });
        }
    }

    // ViewHolder for Subcategories
    private class SubcategoryViewHolder extends RecyclerView.ViewHolder {
        private final ImageView subcategoryImageView;
        private final TextView subcategoryNameTextView;

        SubcategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            subcategoryImageView = itemView.findViewById(R.id.imageSubcategory);
            subcategoryNameTextView = itemView.findViewById(R.id.textSubcategoryName);
        }

        void bind(Subcategory subcategory) {
            // Bind subcategory data to views
            subcategoryNameTextView.setText(subcategory.getName());

            // Load image using Picasso
            Picasso.get()
                    .load(subcategory.getPhotoUrl())
                    .placeholder(R.drawable.default_category_image)  // Optional placeholder image while loading
                    .error(R.drawable.ic_error)  // Optional error image if the loading fails
                    .into(subcategoryImageView);

            // Set onClickListener to handle subcategory click
            itemView.setOnClickListener(v -> {
                if (productClickListener != null) {
                    productClickListener.onSubcategoryClicked(subcategory);
                }
            });
        }
    }

    // ViewHolder for Products
    private class ProductViewHolder extends RecyclerView.ViewHolder {
        private final ImageView productImageView;
        private final TextView productNameTextView;
        private final EditText editTextQuantity;
        private final Spinner spinnerSize;
        private final Button buttonToWishlist;
        private final ImageButton buttonClose;

        ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageView = itemView.findViewById(R.id.imageViewProduct);
            productNameTextView = itemView.findViewById(R.id.textViewProductName);
            editTextQuantity = itemView.findViewById(R.id.editTextQuantity);
            spinnerSize = itemView.findViewById(R.id.spinnerSize);
            buttonToWishlist = itemView.findViewById(R.id.buttonToWishlist);
            buttonClose = itemView.findViewById(R.id.buttonClose);
        }

        void bind(Product product) {
            // Bind product data to views
            productNameTextView.setText(product.getName());

            // Load image using Picasso
            Picasso.get()
                    .load(product.getPhotoUrl())
                    .placeholder(R.drawable.ic_product)  // Optional placeholder image while loading
                    .error(R.drawable.ic_error)  // Optional error image if the loading fails
                    .into(productImageView);

            // Set onClickListener to handle product click
            itemView.setOnClickListener(v -> {
                if (productClickListener != null) {
                    productClickListener.onProductClicked(product);
                    showHiddenViews();
                    enlargeProductView();
                }
            });

            bindSpinner(product.getSizes());

            buttonToWishlist.setOnClickListener(v -> addToWishlist(product));

            buttonClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideHiddenViews();
                    shrinkProductView();
                }
            });

        }

        private void bindSpinner(List<Size> sizes) {
            // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter<Size> adapter = new ArrayAdapter<>(itemView.getContext(),
                    android.R.layout.simple_spinner_item, sizes);

            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            // Apply the adapter to the spinner
            spinnerSize.setAdapter(adapter);
        }

        public void showHiddenViews() {
            editTextQuantity.setVisibility(View.VISIBLE);
            spinnerSize.setVisibility(View.VISIBLE);
            buttonToWishlist.setVisibility(View.VISIBLE);
            buttonClose.setVisibility(View.VISIBLE);
        }

        public void enlargeProductView() {
            ViewGroup.LayoutParams layoutParams = productImageView.getLayoutParams();
            layoutParams.height = 110;
            productImageView.setLayoutParams(layoutParams);
        }

        private void hideHiddenViews() {
            editTextQuantity.setVisibility(View.GONE);
            spinnerSize.setVisibility(View.GONE);
            buttonToWishlist.setVisibility(View.GONE);
            buttonClose.setVisibility(View.GONE);
        }

        private void shrinkProductView() {
            ViewGroup.LayoutParams layoutParams = productImageView.getLayoutParams();
            layoutParams.height = 370;
            productImageView.setLayoutParams(layoutParams);
        }

        private void addToWishlist(Product product) {
            String quantity = editTextQuantity.getText().toString();
            String selectedSize = spinnerSize.getSelectedItem().toString();

            // Make a request using the extracted data
            ToWishlistRequest.createWishlistItem(
                    itemView.getContext(), // Replace with the appropriate context
                    product,
                    quantity,
                    selectedSize,
                    wishlistItem -> {
                        // Wishlist item created successfully, handle the response as needed
                        Log.d("addToWishlist", "Wishlist item added successfully");
                        Toast.makeText(itemView.getContext(), "Wishlist item added successfully", Toast.LENGTH_SHORT).show();

                        // Reverse the actions
                        hideHiddenViews();
                        shrinkProductView();
                    },
                    error -> {
                        // Handle error response
                        Log.e("addToWishlist", "Error adding to wishlist: " + error.toString());
                        Toast.makeText(itemView.getContext(), "Error adding to wishlist: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
            );
        }

    }

    public void setCategories(List<Category> categories) {
        items.clear();
        items.addAll(categories);
        notifyDataSetChanged();
    }

    public void setSubcategories(List<Subcategory> subcategories) {
        items.clear();
        items.addAll(subcategories);
        notifyDataSetChanged();
    }

    public void setProducts(List<Product> products) {
        if (products != null) {
            items.clear();
            items.addAll(products);
            notifyDataSetChanged();
        }
    }

}

