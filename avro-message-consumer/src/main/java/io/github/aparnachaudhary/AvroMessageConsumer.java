package io.github.aparnachaudhary;

import java.util.Collections;
import java.util.Properties;

import GreetingDemo.avro.Greeting;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Demonstrates avro message consumer using Schema Registry.
 *
 * @author aparna
 * @since 05.01.2017
 */
public class AvroMessageConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AvroMessageConsumer.class);

    public static void main(final String[] args) {
        final Properties properties = new Properties();
        properties.put("bootstrap.servers", System.getProperty("bootstrap.servers", "localhost:9092"));
        properties.put("schema.registry.url", System.getProperty("schema.registry.url", "http://localhost:8081"));
        properties.put("key.deserializer", "io.confluent.kafka.serializers.KafkaAvroDeserializer");
        properties.put("value.deserializer", "io.confluent.kafka.serializers.KafkaAvroDeserializer");
        properties.put("group.id", System.getProperty("group.id", "TEAM-B"));

        KafkaConsumer consumer = null;
        try {
            final String topic = System.getProperty("topicName", "avroGreetings");
            consumer = new KafkaConsumer<String, String>(properties);
            consumer.subscribe(Collections.singletonList(topic));

            while (true) {
                final ConsumerRecords<String, Greeting> records = consumer.poll(100);
                for (final ConsumerRecord<String, Greeting> record : records) {
                    LOGGER.info("RecordMetadata: Topic: {} Partition: {} Offset: {} Key: {} Value: {}", record.topic(),
                            record.partition(), record.offset(), record.key(), record.value());
                }
            }
        } catch (final Exception e) {
            LOGGER.error("Failed to consume message", e);
        } finally {
            consumer.close();
        }
    }
}
