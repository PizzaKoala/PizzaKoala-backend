//package com.PizzaKoala.Pizza.global.config;
//
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
//import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
//@Configuration
//public class JacksonConfig {
//    @Bean
//    public ObjectMapper objectMapper() {
//        ObjectMapper mapper = new ObjectMapper();
//        // 빈 객체에 대해서도 에러를 발생시키지 않도록 설정
//        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
//        // 알 수 없는 속성에 대해서도 에러를 발생시키지 않도록 설정
//        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        // LocalDateTime을 처리할 수 있도록 JavaTimeModule 등록
//        JavaTimeModule javaTimeModule = new JavaTimeModule();
//        // LocalDateTime의 직렬화 및 역직렬화 포맷 설정
//        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
//        mapper.registerModule(javaTimeModule);
//        return mapper;
//
//    }
//}
