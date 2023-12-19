package gr.aueb.cf4.orderappandroid.models;

public class OrderItem {
    private Long id;
    private int quantity;
    private Size size;
    private Product product;
    private Order order;

    public OrderItem() {
    }

    public OrderItem(Long id, int quantity, Size size, Product product, Order order) {
        this.id = id;
        this.quantity = quantity;
        this.size = size;
        this.product = product;
        this.order = order;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
    public String getSizeDisplayName() {
        return (size != null) ? size.getDisplayName() : null;
    }
}
