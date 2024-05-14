package com.ymj.tourstudy.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.ymj.tourstudy.pojo.Point;

import java.io.IOException;

public class PointKeySerializer extends JsonSerializer<Point> {
    @Override
    public void serialize(Point point, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeFieldName(point.toString());
    }
}
