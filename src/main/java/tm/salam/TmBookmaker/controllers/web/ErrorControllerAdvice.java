package tm.salam.TmBookmaker.controllers.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import jakarta.validation.ConstraintViolation;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.GenericJDBCException;
import org.json.JSONException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import tm.salam.TmBookmaker.exceptions.ApiError;
import tm.salam.TmBookmaker.exceptions.ResourceNotFoundExceptions;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ErrorControllerAdvice extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {

        List<String> errors = new ArrayList<String>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        ApiError apiError =
                new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return handleExceptionInternal(
                ex, apiError, headers, apiError.getStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers,
                                                                          HttpStatusCode status,
                                                                          WebRequest request) {

        String error = ex.getParameterName() + " parameter is missing";

        ApiError apiError =
                new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
        return new ResponseEntity<Object>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<?> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getErrorMessage());

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<?> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                   WebRequest request) {

        String error = ex.getName() + " should be of type " + ex.getRequiredType().getName();
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler(ResourceNotFoundExceptions.class)
    public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundExceptions ex, WebRequest request) {

        ApiError message = new ApiError(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                ex.getLocalizedMessage());


        return new ResponseEntity<>(message, new HttpHeaders(), message.getStatus());
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<?> multipartException(MultipartException multipartException, WebRequest webRequest){

        ApiError message=new ApiError(
                HttpStatus.MULTI_STATUS,
                multipartException.getMessage(),
                multipartException.getLocalizedMessage()
        );

        return new ResponseEntity<>(message, new HttpHeaders(), message.getStatus());
    }

    @ExceptionHandler(RequestRejectedException.class)
    public ResponseEntity<?> requestRejectedException(RequestRejectedException requestRejectedException,
                                                           WebRequest webRequest){

        ApiError message=new ApiError(
                HttpStatus.BAD_REQUEST,
                requestRejectedException.getMessage(),
                requestRejectedException.getLocalizedMessage()
        );

        return new ResponseEntity<>(message, new HttpHeaders(), message.getStatus());
    }

    @ExceptionHandler(GenericJDBCException.class)
    public ResponseEntity<?> genericJDBCException(GenericJDBCException genericJDBCException, NativeWebRequest request) {

        ApiError message=new ApiError(
                HttpStatus.EXPECTATION_FAILED,
                genericJDBCException.getMessage(),
                genericJDBCException.getLocalizedMessage()
        );

        return new ResponseEntity<>(message, new HttpHeaders(), message.getStatus());
    }

    @ExceptionHandler(JpaSystemException.class)
    public ResponseEntity<?> jpaSystemException(JpaSystemException jpaSystemException, NativeWebRequest request) {

        ApiError message=new ApiError(
                HttpStatus.EXPECTATION_FAILED,
                jpaSystemException.getMessage(),
                jpaSystemException.getLocalizedMessage()
        );

        return new ResponseEntity<>(message, new HttpHeaders(), message.getStatus());
    }

    @ExceptionHandler({JSONException.class})
    public ResponseEntity<?> jsonException(JSONException jsonException, NativeWebRequest request) {

        ApiError message=new ApiError(
                HttpStatus.EXPECTATION_FAILED,
                jsonException.getMessage(),
                jsonException.getLocalizedMessage()
        );

        return new ResponseEntity<>(message, new HttpHeaders(), message.getStatus());
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<?> jsonProcessingException(JsonProcessingException jsonProcessingException,
                                                     NativeWebRequest request) {

        ApiError message=new ApiError(
                HttpStatus.EXPECTATION_FAILED,
                jsonProcessingException.getMessage(),
                jsonProcessingException.getLocalizedMessage()
        );

        return new ResponseEntity<>(message, new HttpHeaders(), message.getStatus());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?> nullPointerException(NullPointerException nullPointerException, NativeWebRequest request) {

        ApiError message=new ApiError(
                HttpStatus.BAD_REQUEST,
                nullPointerException.getMessage(),
                nullPointerException.getLocalizedMessage()
        );

        return new ResponseEntity<>(message, new HttpHeaders(), message.getStatus());
    }

}
