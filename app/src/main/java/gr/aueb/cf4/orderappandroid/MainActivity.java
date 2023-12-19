package gr.aueb.cf4.orderappandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import gr.aueb.cf4.orderappandroid.fragments.HomeFragment;
import gr.aueb.cf4.orderappandroid.fragments.OrdersFragment;
import gr.aueb.cf4.orderappandroid.fragments.ProductsFragment;
import gr.aueb.cf4.orderappandroid.fragments.WishlistFragment;

public class MainActivity extends AppCompatActivity {
    private MaterialToolbar toolbar;
    private FrameLayout frameLayout;
    private BottomNavigationView bottomNV;
    private TextView titleTV;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        titleTV = findViewById(R.id.titleTV);
        frameLayout = findViewById(R.id.frameLayout);
        bottomNV = findViewById(R.id.bottomNV);

        fragmentManager = getSupportFragmentManager();
        titleTV.setText("Home");
        bottomNV.getMenu().getItem(0).setChecked(true);

        // Pass reference to HomeFragment
        HomeFragment homeFragment = new HomeFragment(bottomNV);
        fragmentManager.beginTransaction().replace(R.id.frameLayout, homeFragment, "HomeFragment").commit();


        bottomNV.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.orderItem) {
                    fragmentManager.beginTransaction().replace(R.id.frameLayout, new OrdersFragment(), "OrderFragment").commit();
                    titleTV.setText("Orders");
                } else if (itemId == R.id.wishlistItem) {
                    fragmentManager.beginTransaction().replace(R.id.frameLayout, new WishlistFragment(), "WishlistFragment").commit();
                    titleTV.setText("Wishlist");
                } else if (itemId == R.id.homeItem) {
                    HomeFragment homeFragment = new HomeFragment(bottomNV);
                    fragmentManager.beginTransaction().replace(R.id.frameLayout, homeFragment, "HomeFragment").commit();
                    titleTV.setText("Home");
                } else if (itemId == R.id.productsItem) {
                    fragmentManager.beginTransaction().replace(R.id.frameLayout, new ProductsFragment(), "ProductsFragment").commit();
                    titleTV.setText("Products");
                }
                return true;
            }
        });
    }
}