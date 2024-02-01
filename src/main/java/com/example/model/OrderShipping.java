package com.example.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class OrderShipping {

    @Id
    private Long orderId;
    private String shippingCode;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date shippingDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deliveryDate;
    
    private String deliveryTimezone;
    private boolean checked;
    private String uploadStatus;
    private Order order;



    public Long getOrderId() {
        return orderId;
    }
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    public String getShippingCode() {
        return shippingCode;
    }
    public void setShippingCode(String shippingCode) {
        this.shippingCode = shippingCode;
    }
    public Date getShippingDate() {
        return shippingDate;
    }
    public void setShippingDate(Date shippingDate) {
        this.shippingDate = shippingDate;
    }
    public Date getDeliveryDate() {
        return deliveryDate;
    }
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
    public String getDeliveryTimezone() {
        return deliveryTimezone;
    }
    public void setDeliveryTimezone(String deliveryTimezone) {
        this.deliveryTimezone = deliveryTimezone;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(String uploadStatus) {
        this.uploadStatus = uploadStatus;
    }
    
    public Order getOrder() {
        return order;
    }

    // Order エンティティへの参照を設定する setter メソッド
    public void setOrder(Order order) {
        this.order = order;
    }
}
