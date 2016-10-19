package com.myown.application;

import javax.json.*;
import javax.json.stream.JsonGenerator;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;

public class JavaxJsonUtil {

    //http://www.java2s.com/Tutorials/Java/JSON/0100__JSON_Java.htm

    public static String prettyPrintToString(JsonObject jsonObject) {
        if(jsonObject == null ) {
            return "null";
        }
        Map<String, Boolean> config = new HashMap<>();
        config.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory jwf = Json.createWriterFactory(config);
        StringWriter sw = new StringWriter();

        try (JsonWriter jsonWriter = jwf.createWriter(sw)) {
            jsonWriter.writeObject(jsonObject);
        }
        return sw.toString();
    }

    public static String printToString(JsonStructure jsonObject) {
        if(jsonObject == null ) {
            return "null";
        }
        Map<String, Boolean> config = new HashMap<>();
        JsonWriterFactory jwf = Json.createWriterFactory(config);
        StringWriter sw = new StringWriter();

        try (JsonWriter jsonWriter = jwf.createWriter(sw)) {
            jsonWriter.write(jsonObject);
        }
        return sw.toString();
    }

    public static StringBuffer printToStringBuffer(JsonObject jsonObject) {
        StringWriter stringWriter = new StringWriter();
        JsonWriter writer = Json.createWriter(stringWriter);
        writer.writeObject(jsonObject);
        writer.close();
        //System.out.println(stringWriter.getBuffer().toString());
        return stringWriter.getBuffer();
    }

    public static JsonObjectBuilder createObjectBuilder() {
        Map<String, Object> config = new HashMap<String, Object>();
        //if you need pretty printing
        config.put("javax.json.stream.JsonGenerator.prettyPrinting", Boolean.valueOf(true));

        JsonBuilderFactory factory = Json.createBuilderFactory(config);
        JsonObjectBuilder objectBuilder = factory.createObjectBuilder();
        return objectBuilder;
    }

    public static JsonObjectBuilder toObjectBuilder(JsonObject jsonObject) {
        JsonObjectBuilder objectBuilder = JavaxJsonUtil.createObjectBuilder();
        Set<Map.Entry<String, JsonValue>> entries = jsonObject.entrySet();
        for (Map.Entry<String, JsonValue> entry : entries) {
            objectBuilder.add(entry.getKey(), entry.getValue());
        }
        return objectBuilder;
    }

    public static void setValue(JsonObjectBuilder builder, String value, String jsName ) {
        if(value != null ) {
            builder.add(jsName, value);
        }
    }

    public static void setValue(JsonObjectBuilder builder, Long value, String jsName ) {
        if(value != null ) {
            builder.add(jsName, value);
        }
    }

    public static void setValue(JsonObjectBuilder builder, Boolean value, String jsName ) {
        if(value != null ) {
            builder.add(jsName, value);
        }
    }

    public static JsonArray listToArray(List<JsonObject> list) {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (JsonObject object : list) {
            arrayBuilder.add(object);
        }
        return arrayBuilder.build();
    }

    public static String getString(JsonStructure structure, String path) {
        if(structure == null ) {
            throw new RuntimeException("a bit difficult to extract something from a null value");
        }
        String[] split = path.split(".");
        for (String piece : split) {
            
        }
        if(structure.getValueType().equals(JsonValue.ValueType.ARRAY)) {

        }
        return null;
    }

    public static JsonObject parse(String content) {
        JsonReader jsonReader = Json.createReader(new StringReader(content));
        //get JsonObject from JsonReader
        return jsonReader.readObject();
    }
}
