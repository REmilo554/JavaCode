package com.example.javacode.CountOfElements;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

public class ElementsCount {

    public static  <T> Map<T,Integer> getCountOfElementsInArray(T[] array){
        Map<T,Integer> countOfElements = new HashMap<T,Integer>();
        for (T element : array) {
            countOfElements.getOrDefault(element,0);
            countOfElements.put(element,countOfElements.getOrDefault(element,0) + 1);
        }
        return countOfElements;
    }

    public static void main(String[] args) {
        Integer[] array = new Integer[]{1,2,3,4,5,6,7,8,9,1,2,3,4,1,2,3,4,1,2,3,};
        Map<Integer, Integer> countOfElementsInArray = getCountOfElementsInArray(array);
        System.out.println(countOfElementsInArray);
        String[] array1 = new String[]{"a","b","c","d","e","f","a","b","c","d", "a","b","c","d","a","b","f"};
        Map<String, Integer> countOfElementsInArray1 = getCountOfElementsInArray(array1);
        System.out.println(countOfElementsInArray1);
    }
}
