package gr.aueb.cf4.orderappandroid.models;

import java.util.List;

public class Product {
    private Long id;
    private String name;
    private String photoUrl;
    private List<Size> sizes;
    private Subcategory subcategory;

    public Product() {
    }

    public Product(Long id, String name, String photoUrl, List<Size> sizes, Subcategory subcategory) {
        this.id = id;
        this.name = name;
        this.photoUrl = photoUrl;
        this.sizes = sizes;
        this.subcategory = subcategory;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public List<Size> getSizes() {
        return sizes;
    }

    public void setSizes(List<Size> sizes) {
        this.sizes = sizes;
    }

    public Subcategory getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(Subcategory subcategory) {
        this.subcategory = subcategory;
    }
}
