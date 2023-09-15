package com.czdx.parkingcharge.utils;

import com.google.protobuf.Timestamp;
import com.google.type.Date;
import com.google.type.Money;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Proto转换工具
 */
public class Proto2Utils {

    public static Timestamp toGoogleTimestamp(final LocalDateTime localDateTime) {
        return Timestamp.newBuilder()
                .setSeconds(localDateTime.toEpochSecond(ZoneOffset.of("+8")))
                .setNanos(localDateTime.getNano())
                .build();
    }

    public static LocalDateTime fromGoogleTimestamp(final Timestamp googleTimestamp) {
        return Instant.ofEpochSecond(googleTimestamp.getSeconds(), googleTimestamp.getNanos())
                .atOffset(ZoneOffset.of("+8"))
                .toLocalDateTime();
    }

    public static Date toGoogleDate(final LocalDate localDate) {
        return Date.newBuilder()
                .setYear(localDate.getYear())
                .setMonth(localDate.getMonth().getValue())
                .setDay(localDate.getDayOfMonth())
                .build();
    }

    public static LocalDate fromGoogleDate(final Date googleDate) {
        return LocalDate.of(googleDate.getYear(), googleDate.getMonth(), googleDate.getDay());
    }

    public static Money toGoogleMoney(final BigDecimal decimal) {
        return Money.newBuilder()
                .setCurrencyCode("USD")
                .setUnits(decimal.longValue())
                .setNanos(decimal.remainder(BigDecimal.ONE).movePointRight(decimal.scale()).intValue())
                .build();
    }

    public static BigDecimal fromGoogleMoney(final Money googleMoney) {
        return new BigDecimal(googleMoney.getUnits())
                .add(new BigDecimal(googleMoney.getNanos(), new MathContext(9)));
    }

}
