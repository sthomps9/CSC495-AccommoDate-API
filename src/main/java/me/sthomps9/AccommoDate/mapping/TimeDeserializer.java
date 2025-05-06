package me.sthomps9.AccommoDate.mapping;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.sql.Time;

public class TimeDeserializer extends JsonDeserializer<Time> {
    @Override
    public Time deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return Time.valueOf(p.getText());
    }
}