package gr.aueb.cf4.orderappandroid.viewModel;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.AuthFailureError;

import java.util.Collections;
import java.util.List;

import gr.aueb.cf4.orderappandroid.models.Category;
import gr.aueb.cf4.orderappandroid.models.Product;
import gr.aueb.cf4.orderappandroid.models.Subcategory;
import gr.aueb.cf4.orderappandroid.requests.CategoryRequest;
import gr.aueb.cf4.orderappandroid.requests.ProductsRequest;
import gr.aueb.cf4.orderappandroid.requests.SubcategoryRequest;

public class ProductsViewModel extends AndroidViewModel {
    private final MutableLiveData<List<Category>> categories;
    private final MutableLiveData<List<Subcategory>> subcategories;
    private final MutableLiveData<List<Product>> products;

    public ProductsViewModel(@NonNull Application application) {
        super(application);
        categories = new MutableLiveData<>();
        subcategories = new MutableLiveData<>();
        products = new MutableLiveData<>();
        fetchCategories(application.getApplicationContext()); // Load initial categories
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    public LiveData<List<Subcategory>> getSubcategories() {
        return subcategories;
    }

    public LiveData<List<Product>> getProducts() {
        return products;
    }

    public void fetchCategories(Context context) {
        try {
            CategoryRequest.fetchCategories(
                    context,
                    categoriesList -> {
                        categories.setValue(categoriesList);
                        Log.d("ProductsViewModel", "Categories fetched successfully: " + categoriesList.size() + " categories");
                    },
                    error -> {
                        // Handle error, e.g., update categories LiveData with an empty list
                        categories.setValue(Collections.emptyList());
                        Log.e("ProductsViewModel", "Error fetching categories: " + error.toString());
                    }
            );
        } catch (AuthFailureError e) {
            throw new RuntimeException(e);
        }
    }

    public void fetchSubcategoriesForCategory(Context context, long categoryId) {
        SubcategoryRequest.fetchSubcategories(
                context,
                categoryId,
                subcategoriesList -> {
                    subcategories.setValue(subcategoriesList);
                    Log.d("ProductsViewModel", "Subcategories fetched successfully: " + subcategoriesList.size() + " subcategories");
                },
                error -> {
                    // Handle error, e.g., update subcategories LiveData with an empty list
                    subcategories.setValue(Collections.emptyList());
                    Log.e("ProductsViewModel", "Error fetching subcategories: " + error.toString());
                }
        );
    }

    public void fetchProductsForSubcategory(Context context, long subcategoryId) {
        ProductsRequest.fetchProducts(
                context,
                subcategoryId,
                productsList -> {
                    products.setValue(productsList);
                    Log.d("ProductsViewModel", "Products fetched successfully: " + productsList.size() + " products");
                },
                error -> {
                    // Handle error, e.g., update products LiveData with an empty list
                    products.setValue(Collections.emptyList());
                    Log.e("ProductsViewModel", "Error fetching products: " + error.toString());
                }
        );
    }
}
