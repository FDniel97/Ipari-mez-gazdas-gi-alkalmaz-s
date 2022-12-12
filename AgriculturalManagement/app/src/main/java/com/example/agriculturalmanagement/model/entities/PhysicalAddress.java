package com.example.agriculturalmanagement.model.entities;

import java.lang.String;
import java.lang.Exception;

public class PhysicalAddress {

    private String zipCode;
    private String country;
    private String municipality;
    private String city;
    private String district;
    private String street;
    private String number;
    private String parcelNumber;

    public PhysicalAddress(){

        zipCode = "";
        country = "";
        municipality = "";
        city = "";
        district = "";
        street = "";
        number = "";
        parcelNumber = "";

    }

    public PhysicalAddress(String zipCode, String country, String municipality, String city, String district,
                           String street, String number, String parcelNumber) throws Exception {

        setZipCode(zipCode);

        setCountry(country);

        setMunicipality(municipality);

        setCity(city);

        setDistrict(district);

        setStreet(street);

        setNumber(number);

        setParcelNumber(parcelNumber);
    }

    // GETTER METHODS FOR MEMBERS

    public String getZipCode() {

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


    public void setZipCode(String zipCode) throws Exception{

        if(zipCode.isEmpty()) throw new Exception("Zip code is empty.");
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


    public void setNumber(String number) throws Exception{

        // can be null in case of existing parcel number
        if(number.isEmpty()) throw new Exception("Number is empty");
        else this.number = number;
    }

    public void setParcelNumber(String parcelNumber) throws Exception{

        if(parcelNumber.isEmpty()) throw new Exception("Parcel number is empty.");
        this.parcelNumber = parcelNumber;
    }

    public String toString() {

        return zipCode + ":" + country + ":" + municipality + ":" + city + ":" + district + ":" +
                street + ":" + number + ":" + parcelNumber;
    }

    public void fromString(String rawData) throws Exception {

        String[] rawDataArr = rawData.split(":");

        if(rawDataArr.length != 8) throw new Exception("Ill conditioned data during deserialization.");

        zipCode = rawDataArr[0];
        country = rawDataArr[1];
        municipality = rawDataArr[2];
        city = rawDataArr[3];
        district = rawDataArr[4];
        street = rawDataArr[5];
        number = rawDataArr[6];
        parcelNumber = rawDataArr[7];
    }
}
