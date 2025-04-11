package com.example.food_ordering_mobile_app.models.order;

import com.example.food_ordering_mobile_app.models.location.Location;
import com.example.food_ordering_mobile_app.models.shipper.Shipper;
import com.example.food_ordering_mobile_app.models.store.Store;
import com.example.food_ordering_mobile_app.models.topping.Topping;
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
    private Shipper shipper;
    private String status;
    private String paymentMethod;
    private String createdAt;

    public enum OrderStatus {
        PREORDER, PENDING, CONFIRMED, PREPARING, FINISHED,
        TAKEN, DELIVERING, DELIVERED, DONE,
        CANCELLED;

        public static OrderStatus fromString(String status) {
            return OrderStatus.valueOf(status.toUpperCase());
        }

        public String toValue() {
            return name().toLowerCase(); // match with your backend enum strings
        }
    }



    public Order(){
        id = "";
        user = new User();
        customerName = "";
        customerPhonenumber = "";
        note = "";
        store = new Store();
        items = new ArrayList<>();
        shipLocation = new Location();
        shipper = new Shipper();
        status = "";
        paymentMethod = "";

    }

    public OrderStatus getNextStatus(OrderStatus current) {
        switch (current) {
            case PENDING: return OrderStatus.CONFIRMED;
            case CONFIRMED: return OrderStatus.PREPARING;
            case PREPARING: return OrderStatus.FINISHED;
            case FINISHED: return OrderStatus.TAKEN;
            case TAKEN: return OrderStatus.DELIVERING;
            case DELIVERING: return OrderStatus.DELIVERED;
            case DELIVERED: return OrderStatus.DONE;
            default: return current; // if already final or unknown
        }
    }

    public void cancel() {
        this.status = OrderStatus.CANCELLED.toValue();
    }

    public boolean moveToNextStatus() {
        try {
            OrderStatus current = OrderStatus.fromString(this.status);
            OrderStatus next = getNextStatus(current);
            if (next != current) {
                this.status = next.toValue();
                return true;
            }
        } catch (IllegalArgumentException e) {
            // invalid status string
        }
        return false;
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
            result += order.getDish().getPrice() * order.getQuantity();
            for (Topping topping: order.getToppings()){
                result += topping.getPrice();
            }
        }
        return result;
    }

    public String getOrderSummary(){
        StringBuilder result = new StringBuilder();
        result.append(getTotalItems()).append(" Món").append(" / ").append(getTotalPrice()).append("₫");
        return result.toString();
    }
}
