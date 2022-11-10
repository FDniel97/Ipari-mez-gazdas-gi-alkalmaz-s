package com.example.agriculturalmanagement.model.entities;

import java.lang.String;
import java.lang.Exception;

public class Crop {

    public final Crop ZERO = new Crop("", "", 0, 0);


    private int id;// id for database entity
    private String name;
    private String description;
    private double quantity;
    private int price;


    public Crop() throws Exception {

        name = "";
        description = "";
        quantity = 0.0;
        price = 0;
    }

    /* !@brief Constructor that takes the necessary parameters in order to create crop type
    *
    * @param[in] name Name of the crop
    * @param[in] description Text about the crop
    * @param[in] quantity Harvested, processed amount of crop
    * @param[in] price Unit price of the crop
    */
    public Crop(String name, String description, double quantity, int price) throws Exception{

        if(name.isEmpty()) throw new Exception("Name of crop is empty.");
        else this.name = name;

        if(description.isEmpty()) throw new Exception("Description is empty.");
        else this.description = description;

        if(quantity < 0.0) throw new Exception("Quantity is negative.");
        else this.quantity = quantity;

        if(price < 0) throw new Exception("Price is under zero, negative.");
        else this.price = price;
    }

    // GETTER METHODS FOR MEMBERS

    public int getId(){

        return id;
    }


    public String getName() {

        return name;
    }


    public String getDescription() {

        return description;
    }


    public double getQuantity() {

        return quantity;
    }


    public int getPrice() {

        return price;
    }

    // SETTER METHODS FOR MEMBERS

    public void setName(String name) throws Exception{

        if(name.isEmpty()) throw new Exception("Name of crop is empty.");
        else this.name = name;
    }


    public void setDescription(String description) throws Exception{

        if(description.isEmpty()) throw new Exception("Description is empty.");
        else this.description = description;
    }


    public void setQuantity(double quantity) throws Exception{

        if(quantity < 0.0) throw new Exception("Quantity is negative.");
        else this.quantity = quantity;
    }


    public void setPrice(int price) throws Exception{

        if(price < 0) throw new Exception("Price is under zero, negative.");
        else this.price = price;
    }
}
