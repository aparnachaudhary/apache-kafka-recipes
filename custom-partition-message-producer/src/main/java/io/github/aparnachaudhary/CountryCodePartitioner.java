package io.github.aparnachaudhary;

import java.util.Map;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Partitions the messages based on country code.
 *
 * @author aparna
 * @since 06.01.2017
 */
public class CountryCodePartitioner implements Partitioner {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountryCodePartitioner.class);


    CountryService countryService = null;

    public CountryCodePartitioner() {
        countryService = new CountryService();
    }

    @Override
    public int partition(final String topic, final Object key, final byte[] keyBytes, final Object value, final byte[] valueBytes, final Cluster cluster) {
        LOGGER.info("Total Partitions {} ", cluster.availablePartitionsForTopic(topic));
        int partition = 0;
        final String countryCode = (String) key;
        final Integer countryId = countryService.getIdByCode(countryCode);
        if (countryId != null) {
            partition = countryId;
        }
        LOGGER.info("Setting partition id as {}", partition);
        return partition;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(final Map<String, ?> configs) {

    }
}
