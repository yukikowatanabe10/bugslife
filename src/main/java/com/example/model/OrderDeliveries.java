package com.example.model;
import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import java.sql.Timestamp;

import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "order_deliveries")
public class OrderDeliveries extends TimeEntity implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 他のフィールドやメソッドは省略

     @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    

    // @Column
    // private Integer orderId;

    @Column
    private String shippingCode;

    @Column
    private Timestamp shippingDate;

    @Column
    private Timestamp deliveryDate;

    @Column
    private String deliveryTimeZone;

    @Column
    private Boolean checked;

    @Column
    private String uploadStatus;

    public OrderDeliveries(Long id, String shippingCode, Timestamp shippingDate, Timestamp deliveryDate,String deliveryTimeZone){
        this.id = id;
        this.shippingCode = shippingCode;
        this.shippingDate = shippingDate;
        this.deliveryDate = deliveryDate;
        this.deliveryTimeZone = deliveryTimeZone;
        
    }

    public OrderDeliveries(){}

    public void setOrder(Order order){
        this.order = order;
    }

    
    
}
