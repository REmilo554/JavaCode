package com.example.javacode.FilterForArray;

import java.util.Optional;

public class FilterImpl implements Filter {

    @Override
    public Object apply(Object element) {
        if (element instanceof String) {
            if(((String) element).length() > 5) {
                return "Valid element: " + element;
            }
            else return "No valid element: " + element;
        } else if (element instanceof Integer) {
            return (Integer) element * (Integer) element + 1;
        }
        return element;
    }
}
