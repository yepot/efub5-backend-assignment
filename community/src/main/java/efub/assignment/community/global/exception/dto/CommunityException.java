package efub.assignment.community.global.exception.dto;

import org.springframework.http.HttpStatusCode;

public class CommunityException extends RuntimeException{
    private final ExceptionCode exceptionCode;

    public CommunityException(ExceptionCode exceptionCode){
        this.exceptionCode = exceptionCode;
    }

    @Override
    public String getMessage() {
        return exceptionCode.getMessage();
    }

    public HttpStatusCode getHttpStatusCode(){
        return exceptionCode.getHttpStatus();
    }

    public String getClientExceptionCodeName(){
        return exceptionCode.getClientExceptionCode().name();
    }


}
