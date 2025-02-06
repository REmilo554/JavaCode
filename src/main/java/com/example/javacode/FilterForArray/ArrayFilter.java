package com.example.javacode.FilterForArray;

import java.util.Arrays;

public class ArrayFilter {

    public static <T> T[] filter(T[] array, Filter filter) {
        if (array == null || filter == null || array.length == 0) {
            throw new IllegalArgumentException();
        }
        T[] arrayForFiltering = (T[]) Arrays.copyOf(array, array.length);
        for (int i = 0; i < arrayForFiltering.length; i++) {
            arrayForFiltering[i] = (T) filter.apply(array[i]);
        }
        return arrayForFiltering;
    }

    public static void main(String[] args) {
        FilterImpl filter = new FilterImpl();
        String[] names = {"Jhon", "Bob", "Christian"};
        String[] filteredNames = filter(names,filter);
        for (String name : filteredNames) {
            System.out.println(name);
        }
        Integer[] arrayAfterFiltering = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        Integer[] arrayBeforeFiltering = filter(arrayAfterFiltering, filter);
        for (int i = 0; i < arrayBeforeFiltering.length; i++) {
            System.out.println(arrayBeforeFiltering[i]);
        }
    }
}
