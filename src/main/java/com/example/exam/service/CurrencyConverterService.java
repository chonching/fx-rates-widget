package com.example.exam.service;

import com.example.exam.dto.CurrencyDTO;
import com.example.exam.dto.ResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.RequestBody;

public interface CurrencyConverterService {

    ResponseDTO convertCurrency(@RequestBody CurrencyDTO currencyDTO) throws JsonProcessingException;
}
