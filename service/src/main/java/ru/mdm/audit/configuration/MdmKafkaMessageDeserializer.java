package ru.mdm.audit.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import ru.mdm.kafka.model.MdmKafkaMessage;

import java.io.IOException;

public class MdmKafkaMessageDeserializer implements Deserializer<MdmKafkaMessage<Object>> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public MdmKafkaMessage<Object> deserialize(String topic, byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }

        try {
            return objectMapper.readValue(data, objectMapper.getTypeFactory()
                    .constructParametricType(MdmKafkaMessage.class, Object.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
