package me.sthomps9.AccommoDate.mapping;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.sql.Time;

public class TimeSerializer extends JsonSerializer<Time> {
    @Override
    public void serialize(Time value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value.toString()); // outputs as "HH:mm:ss"
    }
}

