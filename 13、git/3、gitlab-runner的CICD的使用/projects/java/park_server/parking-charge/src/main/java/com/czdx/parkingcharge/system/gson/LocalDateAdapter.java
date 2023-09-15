package com.czdx.parkingcharge.system.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapter extends TypeAdapter<LocalDate> {

    final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void write(JsonWriter jsonWriter, LocalDate date) throws IOException {
        jsonWriter.value(date.format(dateFormatter));
    }
    @Override
    public LocalDate read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        } else {
            // 自定义的工具类，将 String 转成 LocalDate 类型
            LocalDate localDate = LocalDate.parse(in.nextString(), dateFormatter);
            return localDate;
        }
    }

}
