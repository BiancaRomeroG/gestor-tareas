package com.example.gestortareas.data.responses;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse<T> {
    private T payload;
    private String message;
    private int statusCode;
}


//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class ApiResponse<T> {
//    private T payload;
//    private String message;
//    private int statusCode;
//}
