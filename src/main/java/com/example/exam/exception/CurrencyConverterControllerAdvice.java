package com.example.exam.exception;

import com.example.exam.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class CurrencyConverterControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDTO handleInvalidRequest(RuntimeException re){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setErrorMessage(re.getMessage());
        return responseDTO;
    }

    @ExceptionHandler(ApiExpception.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ResponseDTO handleServiceUnavailable(RuntimeException re){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setErrorMessage(re.getMessage());
        return responseDTO;
    }

    @ExceptionHandler(CurrencyNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseDTO handleCurrencyNotFoundException(RuntimeException re){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setErrorMessage(re.getMessage());
        return responseDTO;
    }
}
