package com.example.javacode.TimeTask;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Locale;


public class MyTime {
    public static void main(String[] args) throws JsonProcessingException {
        LocalDateTime localDateTime = LocalDateTime.now();
        MyData myData = new MyData(localDateTime);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setLocale(Locale.getDefault());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String json = objectMapper.writeValueAsString(myData);
        System.out.println(json);
    }


    @Getter
    @Setter
    static class MyData {
        @JsonFormat(pattern = "yyyy:MM:dd__HH:mm:ss:SSS")
        LocalDateTime time;

        public MyData(LocalDateTime time) {
            this.time = time;
        }
    }
}
