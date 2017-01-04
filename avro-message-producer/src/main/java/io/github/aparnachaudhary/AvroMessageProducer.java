package io.github.aparnachaudhary;

import java.util.Date;
import java.util.Properties;

import GreetingDemo.avro.Greeting;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Demonstrates avro message dispatch using Schema Registry.
 *
 * @author aparna
 * @since 04.01.2017
 */
public class AvroMessageProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AvroMessageProducer.class);

    public static void main(final String[] args) {
        final Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("schema.registry.url", "http://localhost:8081");
        properties.put("key.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer");
        properties.put("value.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer");

        KafkaProducer producer = null;
        try {
            producer = new KafkaProducer<String, String>(properties);
            final String topic = "avroGreetings";
            final Greeting greeting = new Greeting("NL", new Date().toString(), "Aparna", "Say Hello");
            final ProducerRecord<String, Greeting> dataRecord = new ProducerRecord<>(topic, greeting
                    .getId().toString(), greeting);
            producer.send(dataRecord, new SimpleProducerCallback());
        } catch (final Exception e) {
            LOGGER.error("Failed to dispatch message", e);
        } finally {
            producer.close();
        }
    }

    private static class SimpleProducerCallback implements Callback {
        @Override
        public void onCompletion(final RecordMetadata recordMetadata, final Exception e) {
            if (e != null) {
                LOGGER.error("Message dispatch was not successful", e);
            } else {
                LOGGER.info("RecordMetadata: Topic: {} Offset: {} Partition: {}", recordMetadata.topic(),
                        recordMetadata.offset(), recordMetadata.partition());
            }
        }
    }
}
