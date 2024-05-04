package tm.salam.TmBookmaker.helpers;

import com.google.gson.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


@Component
public class JsonParser {

    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateDeserializer())
            .registerTypeAdapter(LocalTime.class, new LocalTimeDeserializer())
            .create();

    public <T> T fromJson(String json, Class<T> tClass) {

        return gson.fromJson(json, tClass);
    }

    public <T> T[] fromJsonAsArray(String json, Class<T[]> tClass) {

        return gson.fromJson(json, tClass);
    }

}

class LocalTimeDeserializer implements JsonDeserializer<LocalTime> {
    @Override
    public LocalTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        String timeString = json.getAsString();
        return LocalTime.parse(timeString, DateTimeFormatter.ISO_LOCAL_TIME);
    }

}

class LocalDateDeserializer implements JsonDeserializer<LocalDate> {
    @Override
    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        String timeString = json.getAsString();
        return LocalDate.parse(timeString, DateTimeFormatter.ISO_LOCAL_DATE);
    }

}
