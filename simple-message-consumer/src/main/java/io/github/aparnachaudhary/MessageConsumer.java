package io.github.aparnachaudhary;

import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple message consumer for Kafka. It receives messages from topic "greetingTopic".
 *
 * @author aparna
 * @since 05.01.2017
 */
public class MessageConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageConsumer.class);

    public static void main(final String[] args) {
        final Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("group.id", "TEAM-A");
        KafkaConsumer consumer = null;
        try {
            consumer = new KafkaConsumer<String, String>(properties);
            final String topic = "greetingTopic";
            consumer.subscribe(Collections.singletonList(topic));

            while (true) {
                final ConsumerRecords<String, String> records = consumer.poll(100);
                for (final ConsumerRecord<String, String> record : records) {
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
