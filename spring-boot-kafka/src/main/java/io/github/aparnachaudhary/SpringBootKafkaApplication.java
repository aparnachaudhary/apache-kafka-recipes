package io.github.aparnachaudhary;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class SpringBootKafkaApplication implements CommandLineRunner{

	public static Logger LOGGER = LoggerFactory.getLogger(SpringBootKafkaApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringBootKafkaApplication.class, args);
	}

	private final CountDownLatch latch = new CountDownLatch(3);

	@Autowired
	private KafkaTemplate<String, String> template;

	@Override
	public void run(String... args) throws Exception {
		this.template.send("greetingTopic", "messageId", "Hello World " + new Date().toString());
		this.template.send("greetingTopic", "messageId", "Hello World " + new Date().toString());
		this.template.send("greetingTopic", "messageId", "Hello World " + new Date().toString());
		latch.await(60, TimeUnit.SECONDS);
		LOGGER.info("All dispatched");
	}

	@KafkaListener(topics = "greetingTopic")
	public void listen(ConsumerRecord<?, ?> cr) throws Exception {
		LOGGER.info(cr.toString());
		latch.countDown();
	}
}
