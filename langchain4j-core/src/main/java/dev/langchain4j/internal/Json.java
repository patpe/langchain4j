package dev.langchain4j.internal;

import dev.langchain4j.spi.json.JsonCodecFactory;

import java.io.IOException;
import java.io.InputStream;

import static dev.langchain4j.spi.ServiceHelper.loadFactories;

/**
 * A utility class for JSON.
 */
public class Json {
    private Json() {
    }

    /**
     * The abstract JSON codec interface.
     */
    public interface JsonCodec {

        /**
         * Convert the given object to JSON.
         *
         * @param o the object to convert.
         * @return the JSON string.
         */
        String toJson(Object o);

        /**
         * Convert the given JSON string to an object of the given type.
         *
         * @param json the JSON string.
         * @param type the type of the object.
         * @param <T>  the type of the object.
         * @return the object.
         */
        <T> T fromJson(String json, Class<T> type);

        /**
         * Convert the given JSON string to an object of the given type.
         * 
         * This method is useful if the specified object is a generic type.
         * For non-generic objects, use {@link #fromJson(String, Class)} instead. 
         *
         * @param json the JSON string.
         * @param typeToken The specific genericized type of the object.
         * @param <T>  the type of the object.
         * @param <U>  the argument type of the class of the object
         * @return the object.
         */
        <T, U> T fromJson(String json, Class<T> rawType, Class<U> typeArgumentClass);

        /**
         * Convert the given object to an {@link InputStream}.
         *
         * @param o    the object to convert.
         * @param type the type of the object.
         * @return the {@link InputStream}.
         * @throws IOException if an I/O error occurs.
         */
        InputStream toInputStream(Object o, Class<?> type) throws IOException;
    }

    private static final JsonCodec CODEC = loadCodec();

    private static JsonCodec loadCodec() {
        for (JsonCodecFactory factory : loadFactories(JsonCodecFactory.class)) {
            return factory.create();
        }
        return new GsonJsonCodec();
    }

    /**
     * Convert the given object to JSON.
     *
     * @param o the object to convert.
     * @return the JSON string.
     */
    public static String toJson(Object o) {
        return CODEC.toJson(o);
    }

    /**
     * Convert the given JSON string to an object of the given type.
     *
     * @param json the JSON string.
     * @param type the type of the object.
     * @param <T>  the type of the object.
     * @return the object.
     */
    public static <T> T fromJson(String json, Class<T> type) {
        return CODEC.fromJson(json, type);
    }

    /**
     * Convert the given JSON string to an object of the given type.
     *
     * @param json the JSON string.
     * @param rawType the type of the object.
     * @param typeArgument the generic type argument of the object.
     * @param <T> the type of the object.
     * @param <U> the generic type of {@code rawType}. 
     * @return the object.
     */
    public static <T, U> T fromJson(String json, Class<T> rawType, Class<U> typeArgument) {
        return CODEC.fromJson(json, rawType, typeArgument);
    }

    /**
     * Convert the given object to an {@link InputStream}.
     *
     * @param o    the object to convert.
     * @param type the type of the object.
     * @return the {@link InputStream}.
     * @throws IOException if an I/O error occurs.
     */
    public static InputStream toInputStream(Object o, Class<?> type) throws IOException {
        return CODEC.toInputStream(o, type);
    }
}
