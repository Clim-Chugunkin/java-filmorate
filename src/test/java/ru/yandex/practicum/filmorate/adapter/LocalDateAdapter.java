package ru.yandex.practicum.filmorate.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;

public class LocalDateAdapter extends TypeAdapter<LocalDate> {

    @Override
    public void write(JsonWriter jsonWriter, LocalDate localDate) throws IOException {
        if (localDate == null) {
            jsonWriter.value("null");
        } else {
            jsonWriter.value(localDate.toString());
        }
    }

    @Override
    public LocalDate read(JsonReader jsonReader) throws IOException {
        String jsonStr = jsonReader.nextString();
        if (jsonStr.equals("null")) {
            return null;
        }
        return LocalDate.parse(jsonStr);
    }
}
