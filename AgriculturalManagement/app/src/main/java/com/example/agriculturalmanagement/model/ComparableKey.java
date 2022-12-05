package com.example.agriculturalmanagement.model;

public abstract class ComparableKey<K>{

    public abstract K min(K key);
    public abstract int sgn();
    public abstract int compareTo(K key);
    public abstract K add(K key);
    public abstract K subtract(K key);

    public abstract K maxVal();
}