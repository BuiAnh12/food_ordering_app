package com.example.food_ordering_mobile_app.models.order;

import com.example.food_ordering_mobile_app.models.location.Location;
import com.example.food_ordering_mobile_app.models.store.Store;
import com.example.food_ordering_mobile_app.models.user.User;
import com.example.food_ordering_mobile_app.network.SingleOrListOrIdDeserializer;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class Order {
    @Expose
    @SerializedName("_id")
    private String id;
    private User user;
    private String customerName;
    private String customerPhonenumber;
    private String note;
    private Store store;
    private ArrayList<OrderItem> items;
    private Location shipLocation;
    private String shipper;
    private String status;
    private String paymentMethod;
    private String createdAt;

    public Order(){
        id = "";
        user = new User();
        customerName = "";
        customerPhonenumber = "";
        note = "";
        store = new Store();
        items = null;
        shipLocation = new Location();
        shipper = "";
        status = "";
        paymentMethod = "";
    }



    public String getOrderDetail(){
        StringBuilder result = new StringBuilder();
        for (OrderItem order: items){
            result.append(order.getQuantity()).append(" x ");
            result.append(order.getDish().getName());
            result.append("\n");
        }
        return result.toString();
    }

    public int getTotalItems(){
        int result = 0;
        for (OrderItem order: items){
            result += order.getQuantity();
        }
        return result;
    }

    public long getTotalPrice(){
        long result = 0;
        for (OrderItem order: items){
            result += order.getUnitPrice() * order.getQuantity();
        }
        return result;
    }

    public String getOrderSummary(){
        StringBuilder result = new StringBuilder();
        result.append(getTotalItems()).append(" Món").append(" / ").append(getTotalPrice()).append("₫");
        return result.toString();
    }
}
