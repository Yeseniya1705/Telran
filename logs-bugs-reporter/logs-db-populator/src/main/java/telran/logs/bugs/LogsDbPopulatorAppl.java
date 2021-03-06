package telran.logs.bugs;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;

import telran.logs.bugs.dto.LogDto;
import telran.logs.bugs.dto.LogType;
import telran.logs.bugs.mongo.doc.LogDoc;
import telran.logs.bugs.mongo.repo.LogsRepo;

@SpringBootApplication
public class LogsDbPopulatorAppl {
static Logger LOG = LoggerFactory.getLogger(LogsDbPopulatorAppl.class);
	public static void main(String[] args) {
		SpringApplication.run(LogsDbPopulatorAppl.class, args);

	}
	
	@Bean
	
	Consumer<LogDto> getLogDtoConsumer() {
		return this::takeAndSaveLogDto;
	}
	
	@Autowired
	LogsRepo logsRepository;
	void takeAndSaveLogDto(LogDto logDto) {
		// taking and saving to MongoDB logDto
		LOG.debug("received log: {}", logDto);
		 
			logsRepository.save(new LogDoc(logDto));
			LOG.debug("log: {} saved to Mongo collection", logDto);
			
		
	}

}