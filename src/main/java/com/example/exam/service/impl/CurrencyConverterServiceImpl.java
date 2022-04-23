package com.example.exam.service.impl;

import com.example.exam.common.MessageConstants;
import com.example.exam.config.CurrencyConverterConfig;
import com.example.exam.dto.CurrencyDTO;
import com.example.exam.dto.ExchangeRateDTO;
import com.example.exam.dto.ResponseDTO;
import com.example.exam.exception.ApiExpception;
import com.example.exam.exception.CurrencyNotFoundException;
import com.example.exam.service.CurrencyConverterService;
import com.example.exam.service.ValidateRequestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Service
public class CurrencyConverterServiceImpl implements CurrencyConverterService {

    @Autowired
    private CurrencyConverterConfig currencyConverterConfig;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ValidateRequestService validateRequestService;
    private static final BigDecimal DIVISOR_PERCENT = new BigDecimal(100);
    private static final Integer DECIMAL_POINT = 6;

    private static final Logger LOGGER = LogManager.getLogger(CurrencyConverterServiceImpl.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public ResponseDTO convertCurrency(CurrencyDTO currencyDTO) throws JsonProcessingException {
        LOGGER.info("request: {}", MAPPER.writeValueAsString(currencyDTO));
        validateRequestService.validate(currencyDTO);

        String symbols = String.format("%s,%s",currencyDTO.getBuyCurrency(), currencyDTO.getSellCurrency());
        String requestUrl = String.format(currencyConverterConfig.getApiUrl(), currencyConverterConfig.getApiKey(), symbols);
        ExchangeRateDTO exchangeRateDTO = new ExchangeRateDTO();
        try {
            exchangeRateDTO = restTemplate.exchange(requestUrl, HttpMethod.GET, null, ExchangeRateDTO.class).getBody();
            LOGGER.info("exchangeRates API response in {}: {}", exchangeRateDTO.getBase(), MAPPER.writeValueAsString(exchangeRateDTO));
        }catch (NullPointerException e){
            throw new ApiExpception(MessageConstants.EXCHANGE_RATES_API_NOT_RESPONDING);
        }
        getExchangeRate(currencyDTO, exchangeRateDTO);

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setCurrencyDTO(currencyDTO);
        return responseDTO;
    }

    private CurrencyDTO getExchangeRate(CurrencyDTO currencyDTO, ExchangeRateDTO exchangeRateDTO){
        BigDecimal buyAmount = exchangeRateDTO.getRates().get(currencyDTO.getBuyCurrency());
        if(Objects.isNull(buyAmount)){
            throw new CurrencyNotFoundException(String.format(MessageConstants.UNKNOWN_CURRENCY, currencyDTO.getBuyCurrency()));
        }
        BigDecimal sellAmount = exchangeRateDTO.getRates().get(currencyDTO.getSellCurrency());
        if(Objects.isNull(sellAmount)){
            throw new CurrencyNotFoundException(String.format(MessageConstants.UNKNOWN_CURRENCY, currencyDTO.getSellCurrency()));
        }
        if(Objects.isNull(currencyDTO.getSellAmount()) || currencyDTO.getSellAmount() == BigDecimal.ZERO){
            BigDecimal buyRemainder = buyAmount.subtract(BigDecimal.ONE);
            BigDecimal onePercent = buyAmount.divide(DIVISOR_PERCENT).setScale(DECIMAL_POINT, RoundingMode.HALF_UP);
            BigDecimal buyRemainderPercent = buyRemainder.divide(onePercent, DECIMAL_POINT, RoundingMode.HALF_UP);
            BigDecimal sellRemainder = sellAmount.multiply(buyRemainderPercent.divide(DIVISOR_PERCENT));
            BigDecimal totalSellAmount = sellAmount.subtract(sellRemainder).multiply(currencyDTO.getBuyAmount());
            currencyDTO.setSellAmount(totalSellAmount.setScale(DECIMAL_POINT, RoundingMode.HALF_UP));
            currencyDTO.setBuyAmount(currencyDTO.getBuyAmount().setScale(DECIMAL_POINT));
        } else if(Objects.isNull(currencyDTO.getBuyAmount()) || currencyDTO.getBuyAmount() == BigDecimal.ZERO){
            BigDecimal sellRemainder = sellAmount.subtract(BigDecimal.ONE);
            BigDecimal onePercent = sellAmount.divide(DIVISOR_PERCENT).setScale(DECIMAL_POINT, RoundingMode.HALF_UP);
            BigDecimal sellRemainderPercent = sellRemainder.divide(onePercent, DECIMAL_POINT, RoundingMode.HALF_UP);
            BigDecimal buyRemainder = buyAmount.multiply(sellRemainderPercent.divide(DIVISOR_PERCENT));
            BigDecimal totalBuyAmount = buyAmount.subtract(buyRemainder).multiply(currencyDTO.getSellAmount());
            currencyDTO.setBuyAmount(totalBuyAmount.setScale(DECIMAL_POINT, RoundingMode.HALF_UP));
            currencyDTO.setSellAmount(currencyDTO.getSellAmount().setScale(DECIMAL_POINT));
        }
        return currencyDTO;
    }
}
