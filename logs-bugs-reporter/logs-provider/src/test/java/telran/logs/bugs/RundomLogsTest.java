package telran.logs.bugs;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import telran.logs.bugs.dto.LogDto;
import telran.logs.bugs.dto.LogType;

@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration
@ContextConfiguration(classes=LogsProvider.class)
public class RundomLogsTest {
private static final String AUTHENTICATION_ARTIFACT ="AUTHENTICATION";
private static final String AUTHORIZATION_ARTUFACT = "AUTHORIZATION";
private static final String CLASS_ARTIFACT = "class";
private static final long N_LOGS = 0;
@Autowired
LogsProvider randomLogs;

@Test
void logTypeArtifactTest() throws Exception{
	EnumMap<LogType,String>logTypeArtifactMap=getMapForTest();
	logTypeArtifactMap.forEach((k,v)->{
		switch(k) {
		case AUTHENTICATION_EZCEPTION: assertEquals(AUTHENTICATION_ARTIFACT,v);
		break;
		case AUTHORIZATION_EXCEPTION: assertEquals(AUTHORIZATION_ARTUFACT,v);
		break;
		default:
			assertEquals(CLASS_ARTIFACT,v);
			
		}
	});
}

private EnumMap<LogType, String> getMapForTest() throws NoSuchMethodException,IllegalAccessException, InvocationTargetException{
	Method getMapMethod=randomLogs.getClass().getDeclaredMethod("getLogArtifactMap");
	getMapMethod.setAccessible(true);
	@SuppressWarnings("unchecked")
	EnumMap<LogType,String> logTypeArtifactMap=(EnumMap<LogType,String>) getMapMethod.invoke(randomLogs);
	return logTypeArtifactMap;
}


@Test
void generation() throws Exception{
	List <LogDto>logs= Stream.generate(()->randomLogs.createRundomLog()).limit(N_LOGS)
			.collect(Collectors.toList());
	testLogContent(logs);
	Map<LogType,Long> logTypeOccurrences=logs.stream().collect(Collectors.groupingBy(l->l.logType,Collectors.counting()));
	logTypeOccurrences.forEach((k,v)->{
		System.out.printf("LogType %s, count: %d\n",k,v);
	});
	assertEquals(LogType.values().length, logTypeOccurrences.entrySet().size());
}

private void testLogContent(List<LogDto> logs) {
	logs.forEach(log->{
		switch(log.logType) {
		case AUTHENTICATION_EZCEPTION:
			assertEquals(AUTHENTICATION_ARTIFACT,log.artifact);
			assertEquals(0,log.responseTime);
			assertTrue(log.result.isEmpty());
			break;
		case AUTHORIZATION_EXCEPTION:
			assertEquals(AUTHORIZATION_ARTUFACT,log.artifact);
			assertEquals(0,log.responseTime);
			assertTrue(log.result.isEmpty());
			break;
		case NO_EXEPTION:
			assertEquals(CLASS_ARTIFACT,log.artifact);
			assertEquals(0,log.responseTime);
			assertTrue(log.result.isEmpty());
			break;
		default:
			assertEquals(CLASS_ARTIFACT,log.artifact);
			assertEquals(0,log.responseTime);
			assertTrue(log.result.isEmpty());
			break;
		}
	});
	
}
}
