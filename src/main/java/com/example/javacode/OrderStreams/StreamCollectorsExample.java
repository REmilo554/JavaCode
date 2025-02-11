package com.example.javacode.OrderStreams;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamCollectorsExample {
    public static void main(String[] args) {
        List<Order> orders = List.of(
                new Order("Laptop", 1200.0),
                new Order("Smartphone", 800.0),
                new Order("Laptop", 1500.0),
                new Order("Tablet", 500.0),
                new Order("Smartphone", 900.0)
        );

        List<Order> firstTask = Stream
                .of(new Order("Table", 800.0),
                        new Order("Bicycle", 600.0),
                        new Order("FlyingPudge", 90000.0))
                .toList();

        Map<String, List<Order>> secondTask = orders.stream().collect(Collectors.groupingBy(Order::getProduct));

        Map<String, Double> thirdTask = orders.stream()
                .collect(Collectors.groupingBy(Order::getProduct,
                        Collectors.summingDouble(Order::getCost)));

        //использовал мапу из пункта 3,дабы не дублировать код
        List<Map.Entry<String, Double>> fourthTask = thirdTask.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).toList();

        List<Order> fifthTask = orders.stream().sorted(Comparator.comparing(Order::getCost).reversed()).limit(3).toList();

        //так же использовал мапу из пункта 3,ибо условия задания аналогичные
        double sixthTask = thirdTask.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(3).mapToDouble(Map.Entry::getValue).sum();
        System.out.println("Три самых дорогих продукта: ");
        thirdTask.forEach((k, v) -> System.out.println(k + ": " + v));
        System.out.println("Их общая стоимость: " + sixthTask);
    }
}
