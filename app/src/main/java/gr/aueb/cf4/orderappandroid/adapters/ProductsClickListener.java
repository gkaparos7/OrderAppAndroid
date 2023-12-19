package gr.aueb.cf4.orderappandroid.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentManager;

import gr.aueb.cf4.orderappandroid.models.Category;
import gr.aueb.cf4.orderappandroid.models.Subcategory;
import gr.aueb.cf4.orderappandroid.models.Product;
import gr.aueb.cf4.orderappandroid.viewModel.ProductsViewModel;

public class ProductsClickListener implements ProductClickListener {
    private final ProductsViewModel viewModel;
    private final FragmentManager fragmentManager;
    private final Context context;

    public ProductsClickListener(ProductsViewModel viewModel, FragmentManager fragmentManager, Context context) {
        this.viewModel = viewModel;
        this.fragmentManager = fragmentManager;
        this.context = context;
    }

    @Override
    public void onCategoryClicked(Category category) {
        viewModel.fetchSubcategoriesForCategory(context, category.getId());
    }

    @Override
    public void onSubcategoryClicked(Subcategory subcategory) {
        viewModel.fetchProductsForSubcategory(context, subcategory.getId());
    }

    @Override
    public void onProductClicked(Product product) {

    }

}

