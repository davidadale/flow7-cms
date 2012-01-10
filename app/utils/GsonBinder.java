package utils;

import java.lang.annotation.*;
import com.google.gson.*;
import play.data.binding.*;
import java.lang.reflect.Type;

@Global
public class GsonBinder implements TypeBinder<JsonObject> {

    public Object bind( String name, Annotation[] annotations, String value,  Class actualClass, Type genericType ) throws Exception {
        return new JsonParser().parse(value);
    }
}