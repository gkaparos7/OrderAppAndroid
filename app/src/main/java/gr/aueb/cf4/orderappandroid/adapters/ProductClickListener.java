package gr.aueb.cf4.orderappandroid.adapters;

import gr.aueb.cf4.orderappandroid.models.Category;
import gr.aueb.cf4.orderappandroid.models.Product;
import gr.aueb.cf4.orderappandroid.models.Subcategory;

public interface ProductClickListener {
    void onCategoryClicked(Category category);
    void onSubcategoryClicked(Subcategory subcategory);
    void onProductClicked(Product product);
}
