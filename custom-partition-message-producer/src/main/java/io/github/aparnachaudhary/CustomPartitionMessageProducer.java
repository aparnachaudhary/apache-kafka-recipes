package io.github.aparnachaudhary;

import java.util.Date;
import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple message producer for Kafka. It sends a message to "countryTopic" with key as "countryCode".
 *
 * @author aparna
 * @since 04.01.2017
 */
public class CustomPartitionMessageProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomPartitionMessageProducer.class);

    public static void main(final String[] args) {
        final Properties properties = new Properties();
        properties.put("bootstrap.servers", System.getProperty("bootstrap.servers", "localhost:9092"));
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("partitioner.class", "io.github.aparnachaudhary.CountryCodePartitioner");
        KafkaProducer producer = null;
        try {
            producer = new KafkaProducer<String, String>(properties);
            final String topic = System.getProperty("topicName", "jobfeeds");

            final CountryService countryService = new CountryService();
            for (final String countryCode : countryService.getAllCountryCodes()) {
                final ProducerRecord<String, String> dataRecord = new ProducerRecord<>(topic, countryCode,
                        "Hello World " + new Date().toString());
                producer.send(dataRecord, new SimpleProducerCallback());
            }
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
