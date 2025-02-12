package com.example.javacode.ParallelStream;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParallelStream {
    public static void main(String[] args) {
        List<Student> students = Arrays.asList(
                new Student("Student1", Map.of("Math", 90, "Physics", 85)),
                new Student("Student2", Map.of("Math", 95, "Physics", 88)),
                new Student("Student3", Map.of("Math", 88, "Chemistry", 92)),
                new Student("Student4", Map.of("Physics", 78, "Chemistry", 85))
        );

        Map<String, Double> average = students.parallelStream()
                .flatMap(student -> student.getGrades().entrySet().stream())
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.averagingInt(Map.Entry::getValue)));

        average.forEach((k, v) -> System.out.println(k + ": " + v));
    }
}
