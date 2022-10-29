package com.example.agriculturalmanagement.model;

import java.lang.String;
import java.lang.Exception;
import java.util.concurrent.ExecutionException;

public class Address {

    private int zipCode;
    private String country;
    private String municipality;
    private String city;
    private String district;
    private String street;
    private String number;
    private String parcelNumber;

    public Address(){

        // TODO...
    }

    public Address(int zipCode, String country, String municipality, String city, String district,
                   String street, String number, String parcelNumber) throws Exception {

        setZipCode(zipCode);
        setCountry(country);
        setMunicipality(municipality);
        setCity(city);
        setDistrict(district);
        setStreet(street);
        setNumber(number, parcelNumber);
        setParcelNumber(parcelNumber);
    }

    // GETTER METHODS FOR MEMBERS

    public int getZipCode() {

        return zipCode;
    }

    public String getCountry() {

        return country;
    }

    public String getMunicipality() {

        return municipality;
    }

    public String getCity() {

        return city;
    }

    public String getDistrict() {

        return district;
    }

    public String getStreet() {

        return street;
    }

    public String getNumber() {

        return number;
    }

    public String getParcelNumber() {

        return parcelNumber;
    }

    // SETTER METHODS FOR MEMBERS


    public void setZipCode(int zipCode) throws Exception{

        if(zipCode < 0) throw new Exception("Zip code is negative.");
        else this.zipCode = zipCode;
    }

    public void setCountry(String country) throws Exception{

        if(country.isEmpty()) throw new Exception("Country is empty.");
        else this.country = country;
    }

    public void setMunicipality(String municipality) throws Exception{

        if(municipality.isEmpty()) throw new Exception("Municipality is empty.");
        this.municipality = municipality;
    }

    public void setCity(String city) throws Exception{

        if(city.isEmpty()) throw new Exception("City is empty.");
        this.city = city;
    }

    public void setDistrict(String district) throws Exception{

        // can be null due to law conditions, regulations
        if(district.isEmpty()) district = "";
        this.district = district;
    }

    public void setStreet(String street) throws Exception{

        if(street.isEmpty()) throw new Exception("Street is empty");
        this.street = street;
    }

    public void setNumber(String number, String parcelNumber) throws Exception{

        // can be null in case of existing parcel number
        if(number.isEmpty() && !parcelNumber.isEmpty()) this.number = "";
        else if(parcelNumber.isEmpty()) throw new Exception("Number is empty");
        else this.number = number;
    }

    public void setParcelNumber(String parcelNumber) throws Exception{

        if(parcelNumber.isEmpty()) throw new Exception("Parcel number is empty.");
        this.parcelNumber = parcelNumber;
    }
}
