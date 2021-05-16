package gr.codehub.sacchon.representation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResultData<T> {

    private String message;
    private int statusCode;
    private T data;



}
