package gr.aueb.cf4.orderappandroid.models;

import java.util.List;

public class Wishlist {
    private Long id;
    private User user;
    private List<WishlistItem> wishlistItems;

    public Wishlist() {
    }

    public Wishlist(Long id, User user, List<WishlistItem> wishlistItems) {
        this.id = id;
        this.user = user;
        this.wishlistItems = wishlistItems;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<WishlistItem> getWishlistItems() {
        return wishlistItems;
    }

    public void setWishlistItems(List<WishlistItem> wishlistItems) {
        this.wishlistItems = wishlistItems;
    }
}
