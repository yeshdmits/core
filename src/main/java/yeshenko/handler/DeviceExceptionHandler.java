package yeshenko.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import yeshenko.exception.DeviceDatabaseException;
import yeshenko.exception.DeviceNotFoundException;

@ControllerAdvice
public class DeviceExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = DeviceDatabaseException.class)
    protected ResponseEntity<Object> handleDatabaseException(RuntimeException exception, WebRequest request) {
        return handleExceptionInternal(exception, exception.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = DeviceNotFoundException.class)
    protected ResponseEntity<Object> handleNotFoundException(RuntimeException exception, WebRequest request) {
        return handleExceptionInternal(exception, exception.getMessage(), new HttpHeaders(), HttpStatus.NO_CONTENT, request);
    }
}
