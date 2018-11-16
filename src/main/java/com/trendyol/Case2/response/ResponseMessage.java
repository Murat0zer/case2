package com.trendyol.Case2.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ResponseMessage {

    private boolean status;
    private String message;
    private Object returnObject;

}
