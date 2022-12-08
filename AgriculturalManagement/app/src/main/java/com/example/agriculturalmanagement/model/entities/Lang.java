package com.example.agriculturalmanagement.model.entities;

import java.util.Iterator;
import java.util.Map;
import java.lang.Exception;

public class Lang {

    private Map<String, String> fieldLabels;

    public Lang(){

        // TODO...
    }

    public Lang(Map<String, String> fieldLabels) throws Exception{

        if(fieldLabels.isEmpty()) throw new Exception("Field list is empty, language can not be loaded.");

        // TODO...
    }

    public String get(String keyLabel){

        return fieldLabels.containsKey(keyLabel) ? fieldLabels.get(keyLabel) : "NullField";
    }
}
