package telran.logs.bugs;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumMap;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

import telran.logs.bugs.dto.LogDto;
import telran.logs.bugs.dto.LogType;

@Component
public class LogsProvider {
	  int exceptionProb = 10;
	 int secExceptionProb = 30;
	int authenticationProb = 70;
	
	public LogDto createRundomLog() {
		LogType logType=getLogType();
		return new LogDto(new Date(),logType,getArtyfact(logType),getResponseTime(logType),"");
	}


private int getResponseTime(LogType logType) {
		return logType==LogType.NO_EXEPTION? 
				ThreadLocalRandom.current().nextInt(20,200):0;
	}


private String getArtyfact(LogType logType) {
		EnumMap<LogType,String> logArtifact=getLogArtufactMap();
		return logArtifact.get(logType);
	}



private EnumMap<LogType, String> getLogArtufactMap() {
	EnumMap<LogType,String> res=new EnumMap<>(LogType.class);
	Arrays.asList(LogType.values()).forEach(lt->{
		fillLogTypeArtufactMap(res,lt);
	});
	return res;
}


private void fillLogTypeArtufactMap(EnumMap<LogType, String> res, LogType lt) {
	switch(lt){
	case BAD_REQUEST_EXCEPTION:
		res.put(LogType.BAD_REQUEST_EXCEPTION,"bad_request_exception");
	case DUBLICATED_KEY_EXCEPTION:
		res.put(LogType.DUBLICATED_KEY_EXCEPTION,"dublicate_key");
	case NOT_FOUND_EXCEPTION:
		res.put(LogType.NOT_FOUND_EXCEPTION,"not_found_exception");
	case NO_EXEPTION:
		res.put(LogType.NO_EXEPTION,"no_exception");
	case SERVER_EXCEPTION:
		res.put(LogType.SERVER_EXCEPTION,"server_exception");
	case AUTHENTICATION_EZCEPTION:
		res.put(LogType.AUTHENTICATION_EZCEPTION,"authentication_exception");
	case AUTHORIZATION_EXCEPTION:
		res.put(LogType.AUTHORIZATION_EXCEPTION,"authorization_exception");
	default:
		break;
	}
	
}


private LogType getLogType() {
	int  chance=getChance();
	return chance <= exceptionProb ? getException():LogType.NO_EXEPTION;
}


private LogType getException() {
	return getChance()<= secExceptionProb ? getSecurytyExceptionLog():getNonSecurytyExceptionLog();
}


private LogType getNonSecurytyExceptionLog() {
	LogType nonSecException[]= {
			LogType.BAD_REQUEST_EXCEPTION,
			LogType.DUBLICATED_KEY_EXCEPTION,
			LogType.NOT_FOUND_EXCEPTION,
			LogType.SERVER_EXCEPTION
	};
	int ind=ThreadLocalRandom.current().nextInt(0,nonSecException.length);
	return nonSecException[ind];
}


private LogType getSecurytyExceptionLog() {
	return getChance()<=authenticationProb ? LogType.AUTHENTICATION_EZCEPTION : LogType.AUTHORIZATION_EXCEPTION;
	
}


private int getChance() {
	return ThreadLocalRandom.current().nextInt(1,101);
}
}
