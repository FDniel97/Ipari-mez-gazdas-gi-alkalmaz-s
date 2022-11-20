package com.example.agriculturalmanagement;

import com.example.agriculturalmanagement.model.ComparableKey;

public class TestKey extends ComparableKey<TestKey> {

    public Integer val;


    public TestKey(Integer val){

        this.val = val;
    }

    @Override
    public TestKey maxVal(){

        return new TestKey(Integer.MAX_VALUE);
    }

    @Override
    public TestKey min(TestKey key) {

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
    public int compareTo(TestKey key) {

        return this.val.compareTo(key.val);
    }


    @Override
    public TestKey add(TestKey key) {

        return new TestKey(this.val + key.val);
    }


    @Override
    public TestKey subtract(TestKey key) {

        return new TestKey(this.val - key.val);
    }
}