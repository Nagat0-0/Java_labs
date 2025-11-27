package ua.food_delivery.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ua.food_delivery.util.LoggerUtil;

import java.util.logging.Logger;

public class JsonDataSerializer<T> extends AbstractDataSerializer<T> {

    private static final Logger logger = LoggerUtil.getLogger();

    public JsonDataSerializer() {
        super(createDefaultObjectMapper());
        logger.info("JsonDataSerializer initialized with pretty printing enabled");
    }

    public JsonDataSerializer(ObjectMapper objectMapper) {
        super(objectMapper);
        logger.info("JsonDataSerializer initialized with custom ObjectMapper");
    }

    private static ObjectMapper createDefaultObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    @Override
    public String getFormat() {
        return "JSON";
    }
}