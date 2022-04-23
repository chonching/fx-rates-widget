package com.example.exam.controller;

import com.example.exam.dto.CurrencyDTO;
import com.example.exam.dto.ResponseDTO;
import com.example.exam.service.CurrencyConverterService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Api(value = "ApplicationController")
@Validated
@RestController
@RequestMapping("exchangerates")
public class CurrencyConverterController {

    @Autowired
    private CurrencyConverterService currencyConverterService;

    @ApiOperation(value = "Converts two currency",response = Iterable.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 503, message = "Service Unavailable")
    })
    @PostMapping(value="/convert", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseDTO convertCurrency(@RequestBody  CurrencyDTO currencyDTO) throws JsonProcessingException {
        return currencyConverterService.convertCurrency(currencyDTO);
    }
}
