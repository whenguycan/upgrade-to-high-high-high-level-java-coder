package com.czdx.parkingcharge.system.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {

    final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-ddHH:mm:ss");

    @Override
    public void write(JsonWriter jsonWriter, LocalDateTime dateTime) throws IOException {
        jsonWriter.value(dateTime.format(dateTimeFormatter));
    }

    @Override
    public LocalDateTime read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        } else {
            // 自定义的工具类，将 String 转成 LocalDate 类型
            LocalDateTime localDateTime = LocalDateTime.parse(in.nextString(), dateTimeFormatter);
            return localDateTime;
        }
    }
}
