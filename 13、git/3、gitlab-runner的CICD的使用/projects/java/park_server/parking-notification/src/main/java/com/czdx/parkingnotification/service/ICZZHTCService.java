package com.czdx.parkingnotification.service;

import com.czdx.parkingnotification.domain.czzhtc.response.ChargeResponse;

import java.time.LocalDateTime;

public interface ICZZHTCService {
    ChargeResponse getChargeInfo(String carNumber, LocalDateTime billTime);
}
