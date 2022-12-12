package com.example.agriculturalmanagement.model;

public class GenAreaKey extends ComparableKey<GenAreaKey> {

    public Double val;


    public GenAreaKey(Double val){

        super();

        this.val = val;
    }

    @Override
    public GenAreaKey maxVal(){

        return new GenAreaKey(Double.MAX_VALUE);
    }

    @Override
    public GenAreaKey min(GenAreaKey key) {

        if(this.val <= key.val) return this;
        else return key;
    }


    @Override
    public int sgn() {

        if(this.val == 0) return 0;
        else if(this.val < 0) return -1;
        else return 1;
    }

    @Override
    public int compareTo(GenAreaKey key) {

        return this.val.compareTo(key.val);
    }


    @Override
    public GenAreaKey add(GenAreaKey key) {

        this.val += key.val;
        return this;
    }


    @Override
    public GenAreaKey subtract(GenAreaKey key) {

        return new GenAreaKey(this.val - key.val);
    }

    public static String toString(GenAreaKey key) {

        return "" + key.val;
    }

    public static GenAreaKey fromString(String rawData) {

        return new GenAreaKey(Double.parseDouble(rawData));
    }
}