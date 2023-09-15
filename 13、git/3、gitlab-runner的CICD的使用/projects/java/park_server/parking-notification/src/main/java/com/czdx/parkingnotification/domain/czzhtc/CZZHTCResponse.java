package com.czdx.parkingnotification.domain.czzhtc;

import lombok.Data;

@Data
public class CZZHTCResponse<T> {
    private boolean success;
    private String message;
    private T data;
}
