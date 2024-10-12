package ru.yandex.practicum.filmorate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FilmorateApplication {
	private static final Logger LOG = LoggerFactory.getLogger(FilmorateApplication.class);

	public static void main(final String[] args) {
		SpringApplication.run(FilmorateApplication.class, args);

		((ch.qos.logback.classic.Logger) LoggerFactory
				.getLogger(Logger.ROOT_LOGGER_NAME))
				.setLevel(Level.WARN);
	}
}
