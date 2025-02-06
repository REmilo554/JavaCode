package com.example.javacode.FilterForArray;

import java.util.Arrays;

public class ArrayFilter {

    public <T> Object[] filter(T[] array,FilterImpl filter) {
        if(array==null || filter==null){
            throw new IllegalArgumentException();
        }
        Object[] result = new Object[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = filter.apply(array[i]);
        }
        return result;
    }
}
