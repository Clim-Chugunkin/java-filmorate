package ru.yandex.practicum.filmorate.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
    //private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm:ss");

    @Override
    public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
        if (localDateTime == null) {
            jsonWriter.value("null");
        } else {
            jsonWriter.value(localDateTime.toString());
        }
    }

    @Override
    public LocalDateTime read(JsonReader jsonReader) throws IOException {
        String jsonStr = jsonReader.nextString();
        if (jsonStr.equals("null")) {
            return null;
        }
        return LocalDateTime.parse(jsonStr);
    }
}
