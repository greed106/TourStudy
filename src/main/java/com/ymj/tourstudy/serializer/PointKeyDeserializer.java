package com.ymj.tourstudy.serializer;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.ymj.tourstudy.pojo.Point;

import java.io.IOException;

public class PointKeyDeserializer extends KeyDeserializer {
    @Override
    public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
        String[] parts = key.replace("Point[", "").replace("]", "").split(", ");
        String name = parts[0].split("=")[1];
        int index = Integer.parseInt(parts[1].split("=")[1]);
        int x = Integer.parseInt(parts[2].split("=")[1]);
        int y = Integer.parseInt(parts[3].split("=")[1]);
        return new Point(index, x, y, name);
    }
}
