// package com.example.CAR_.SYSTEM.config;
// import com.fasterxml.jackson.databind.Module;
// import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
// import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// import java.time.format.DateTimeFormatter;
// import java.time.LocalDate;

// @Configuration
// public class JacksonConfig {

//     private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

//     @Bean
//     public Module javaTimeModule() {
//         JavaTimeModule module = new JavaTimeModule();
//         module.addDeserializer(LocalDate.class, new LocalDateDeserializer(DATE_FORMAT));
       
//         return module;
//     }
// }
