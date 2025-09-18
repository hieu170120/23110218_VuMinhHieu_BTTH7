package com.example.demo.model;

public class CategoryModel {

	private Long categoryId;  // ID của danh mục
    private String name;      // Tên của danh mục
    private boolean isEdit;   // Xác định xem đây là chỉnh sửa hay thêm mới

    // Constructor mặc định
    public CategoryModel() {
    }

    // Constructor với các tham số
    public CategoryModel(Long categoryId, String name, boolean isEdit) {
        this.categoryId = categoryId;
        this.name = name;
        this.isEdit = isEdit;
    }

    // Getter và Setter cho categoryId
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    // Getter và Setter cho name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter và Setter cho isEdit
    public boolean isEdit() {
        return isEdit;
    }

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }
    
    // Override toString (Tùy chọn)
    @Override
    public String toString() {
        return "CategoryModel{" +
                "categoryId=" + categoryId +
                ", name='" + name + '\'' +
                ", isEdit=" + isEdit +
                '}';
    }

}
