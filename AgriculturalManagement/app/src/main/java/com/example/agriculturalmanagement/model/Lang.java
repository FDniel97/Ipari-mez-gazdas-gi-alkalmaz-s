package com.example.agriculturalmanagement.model;

import java.util.Iterator;
import java.util.Map;
import java.lang.Exception;

public class Lang {

    private Map<String, String> fieldLabels;

    Lang(){

        // TODO...
    }

    Lang(Map<String, String> fieldLabels) throws Exception{

        if(fieldLabels.isEmpty()) throw new Exception("Field list is empty, language can not be loaded.");

        // TODO...
    }

    public String Get(String key_label){

        return fieldLabels.containsKey(key_label) ? fieldLabels.get(key_label) : "NullField";
    }
}
