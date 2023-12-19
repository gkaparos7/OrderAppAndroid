package gr.aueb.cf4.orderappandroid.models;

import java.util.Date;
import java.util.List;

public class Order {
    private Long id;
    private User user;
    private List<OrderItem> orderItems;
    private Date orderDate;

    public Order() {
    }

    public Order(Long id, User user, List<OrderItem> orderItems, Date orderDate) {
        this.id = id;
        this.user = user;
        this.orderItems = orderItems;
        this.orderDate = orderDate;
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

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }
}
