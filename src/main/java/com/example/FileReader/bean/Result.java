package com.example.FileReader.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Result {

    private int result_code;
    private String result_msg;
    private Object data;

    public static Result create_simple_success() {
        return new Result(0,"OK",null);
    }

    public static Result create_simple_error()
    {
        return new Result(-1,"Error",null);
    }
}
