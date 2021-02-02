package telran.logs.bugs;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import telran.logs.bugs.dto.LogDto;
import telran.logs.bugs.dto.LogType;
import telran.logs.bugs.services.LogsAnalyzerService;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
public class LogsAnilyzerTest {
	static Logger LOG=LoggerFactory.getLogger(LogsAnilyzerTest.class);
	@Autowired
	InputDestination producer;
	@Autowired
	OutputDestination consumer;
@Value("${app-binding-name}")
	String bindName;
@BeforeEach
void setup() {
	consumer.clear();
}
	@Test
	void analyzerTestNonException() {
		LogDto logDto=new LogDto(new Date(), LogType.NO_EXEPTION, "artifact", 0, "result");
		producer.send(new GenericMessage<LogDto>(logDto));
		assertThrows(Exception.class,consumer::receive);
		
		
	}
	@Test
	void analyzerTestException() {
		
		LogDto logDtoException=new LogDto(new Date(), LogType.AUTHENTICATION_EZCEPTION, "artifact", 0, "result");
		producer.send(new GenericMessage<LogDto>(logDtoException));
		//assertNull(consumer.receive(0,bindName+"12345"));
		Message<byte[]> message=consumer.receive(0,bindName);
		assertNotNull(message);
		LOG.debug("received in consumer{}",new String(message.getPayload()));
	}

}
