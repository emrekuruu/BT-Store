package com.example.c320.Entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "baskets")
public class Basket {

    @Id
    private String id;
    String userID;
    Double total = 0.0;
    List<Painting> paintings = new ArrayList<Painting>();

    public Basket() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public List<Painting> getPaintings() {
        return paintings;
    }

    public void setPaintings(List<Painting> paintings) {
        double total = 0;

        for(Painting p : paintings)
            total += p.getPrice();

        setTotal(total);
        this.paintings = paintings;
    }
}
