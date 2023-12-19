package gr.aueb.cf4.orderappandroid.models;


public class WishlistItem {
    private Long id;
    private Wishlist wishlist;
    private Product product;
    private int quantity;

    private Size size;

    public WishlistItem() {
    }

    public WishlistItem(Product product, int quantity, Size size) {
        this.product = product;
        this.quantity = quantity;
        this.size = size;
    }

    public WishlistItem(Long id, Wishlist wishlist, Product product, int quantity, Size size) {
        this.id = id;
        this.wishlist = wishlist;
        this.product = product;
        this.quantity = quantity;
        this.size = size;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Wishlist getWishlist() {
        return wishlist;
    }

    public void setWishlist(Wishlist wishlist) {
        this.wishlist = wishlist;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }
    public String getSizeDisplayName() {
        return (size != null) ? size.getDisplayName() : null;
    }
}
