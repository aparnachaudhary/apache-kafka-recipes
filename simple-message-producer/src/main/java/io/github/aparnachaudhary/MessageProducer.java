package io.github.aparnachaudhary;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

/**
 * Simple message producer for Kafka
 *
 * @author aparna
 * @since 04.01.2017
 */
public class MessageProducer {

    public static void main(final String[] args) {
        final Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        KafkaProducer producer = null;
        try {
            producer = new KafkaProducer<String, String>(properties);
            final String topic = "greetingTopic";
            final ProducerRecord<String, String> dataRecord = new ProducerRecord<>(topic, "messageId",
                    "Hello World");
            producer.send(dataRecord, new SimpleProducerCallback());
        } catch (final Exception e) {
            e.printStackTrace();
        } finally {
            producer.close();
        }
    }

    private static class SimpleProducerCallback implements Callback {
        @Override
        public void onCompletion(final RecordMetadata recordMetadata, final Exception e) {
            if (e != null) {
                e.printStackTrace();
            } else {
                System.out.println("RecordMetadata: Topic: " + recordMetadata.topic() + " Offset: " + recordMetadata
                        .offset() + " Partition: " + recordMetadata.partition());
            }
        }
    }
}
