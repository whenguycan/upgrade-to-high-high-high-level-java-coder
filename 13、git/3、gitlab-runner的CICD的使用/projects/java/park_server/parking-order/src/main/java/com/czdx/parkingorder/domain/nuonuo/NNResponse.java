package com.czdx.parkingorder.domain.nuonuo;

import lombok.Data;


@Data
public class NNResponse<T> {
    private String code;
    private String describe;
    private T result;

}
