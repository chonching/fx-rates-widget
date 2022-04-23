package com.example.exam;

import com.example.exam.config.CurrencyConverterConfig;
import com.example.exam.dto.CurrencyDTO;
import com.example.exam.dto.ExchangeRateDTO;
import com.example.exam.exception.ApiExpception;
import com.example.exam.exception.CurrencyNotFoundException;
import com.example.exam.service.CurrencyConverterService;
import com.example.exam.service.ValidateRequestService;
import com.example.exam.service.impl.CurrencyConverterServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CurrencyConverterServiceTest {

    @InjectMocks
    private CurrencyConverterService currencyConverterService;
    @Mock
    private CurrencyConverterConfig currencyConverterConfig;
    @Mock
    private ValidateRequestService validateRequestService;
    @Mock
    private RestTemplate restTemplate;

    CurrencyDTO currencyDTO;
    public CurrencyConverterServiceTest(){
        currencyConverterService = new CurrencyConverterServiceImpl();
    }

    @BeforeEach
    public void setUp() {
        currencyDTO = new CurrencyDTO();
    }

    @Test
    void testConvertCurrencyBuy() throws JsonProcessingException {
        ReflectionTestUtils.setField(currencyConverterService, "currencyConverterConfig", getConfig());
        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.<HttpEntity<?>>any(),
                ArgumentMatchers.<Class<ExchangeRateDTO>>any()))
                .thenReturn(getExchangeRateResponse());
        currencyDTO.setBuyCurrency("USD");
        currencyDTO.setSellCurrency("PHP");
        currencyDTO.setBuyAmount(BigDecimal.valueOf(10));
        Assertions.assertNotNull(currencyConverterService.convertCurrency(currencyDTO));
    }

    @Test
    void testConvertCurrencySell() throws JsonProcessingException {
        ReflectionTestUtils.setField(currencyConverterService, "currencyConverterConfig", getConfig());
        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.<HttpEntity<?>>any(),
                ArgumentMatchers.<Class<ExchangeRateDTO>>any()))
                .thenReturn(getExchangeRateResponse());
        currencyDTO.setBuyCurrency("USD");
        currencyDTO.setSellCurrency("PHP");
        currencyDTO.setSellAmount(BigDecimal.valueOf(10));
        Assertions.assertNotNull(currencyConverterService.convertCurrency(currencyDTO));
    }

    @Test
    void testConvertCurrencyUnknownBuyCurrency() throws JsonProcessingException {
        ReflectionTestUtils.setField(currencyConverterService, "currencyConverterConfig", getConfig());
        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.<HttpEntity<?>>any(),
                ArgumentMatchers.<Class<ExchangeRateDTO>>any()))
                .thenReturn(getExchangeRateResponseUnknownBuyCurrency());
        currencyDTO.setBuyCurrency("USDSD");
        currencyDTO.setSellCurrency("PHP");
        currencyDTO.setBuyAmount(BigDecimal.valueOf(10));
        Assertions.assertThrows(CurrencyNotFoundException.class, () -> currencyConverterService.convertCurrency(currencyDTO));
    }

    @Test
    void testConvertCurrencyUnknownSellCurrency() throws JsonProcessingException {
        ReflectionTestUtils.setField(currencyConverterService, "currencyConverterConfig", getConfig());
        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.<HttpEntity<?>>any(),
                ArgumentMatchers.<Class<ExchangeRateDTO>>any()))
                .thenReturn(getExchangeRateResponseUnknownSellCurrency());
        currencyDTO.setBuyCurrency("USD");
        currencyDTO.setSellCurrency("PHPSD");
        currencyDTO.setBuyAmount(BigDecimal.valueOf(10));
        Assertions.assertThrows(CurrencyNotFoundException.class, () -> currencyConverterService.convertCurrency(currencyDTO));
    }

    @Test
    void testConvertCurrencyBuyAmountDownAPIServer() throws JsonProcessingException {
        ReflectionTestUtils.setField(currencyConverterService, "currencyConverterConfig", getConfig());
        Mockito.when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.<HttpEntity<?>>any(),
                ArgumentMatchers.<Class<ExchangeRateDTO>>any()))
                .thenThrow(NullPointerException.class);
        currencyDTO.setBuyCurrency("USD");
        currencyDTO.setSellCurrency("PHP");
        currencyDTO.setBuyAmount(BigDecimal.valueOf(10));
        Assertions.assertThrows(ApiExpception.class, () -> currencyConverterService.convertCurrency(currencyDTO));
    }



    private CurrencyConverterConfig getConfig(){
        CurrencyConverterConfig currencyConverterConfig = new CurrencyConverterConfig();
        currencyConverterConfig.setApiKey("7a9cdc90da484bb74f4e994a14decab9");
        currencyConverterConfig.setApiUrl("http://api.exchangeratesapi.io/v1/latest?access_key=%s&symbols=%s");
        return currencyConverterConfig;

    }

    ResponseEntity<ExchangeRateDTO> getExchangeRateResponse(){
        ExchangeRateDTO exchangeRateDTO = new ExchangeRateDTO();
        exchangeRateDTO.setBase("EUR");
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        exchangeRateDTO.setDate(date);
        exchangeRateDTO.setTimestamp(timestamp);
        exchangeRateDTO.setSuccess(true);
        Map<String, BigDecimal> ratesMap = new HashMap<>();
        ratesMap.put("USD",BigDecimal.valueOf(1.080345));
        ratesMap.put("PHP", BigDecimal.valueOf(56.624095));
        exchangeRateDTO.setRates(ratesMap);
        return new ResponseEntity<ExchangeRateDTO>(exchangeRateDTO, HttpStatus.OK);
    }

    ResponseEntity<ExchangeRateDTO> getExchangeRateResponseUnknownBuyCurrency(){
        ExchangeRateDTO exchangeRateDTO = new ExchangeRateDTO();
        exchangeRateDTO.setBase("EUR");
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        exchangeRateDTO.setDate(date);
        exchangeRateDTO.setTimestamp(timestamp);
        exchangeRateDTO.setSuccess(true);
        Map<String, BigDecimal> ratesMap = new HashMap<>();
        ratesMap.put("PHP", BigDecimal.valueOf(56.624095));
        exchangeRateDTO.setRates(ratesMap);
        return new ResponseEntity<ExchangeRateDTO>(exchangeRateDTO, HttpStatus.OK);
    }

    ResponseEntity<ExchangeRateDTO> getExchangeRateResponseUnknownSellCurrency(){
        ExchangeRateDTO exchangeRateDTO = new ExchangeRateDTO();
        exchangeRateDTO.setBase("EUR");
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        exchangeRateDTO.setDate(date);
        exchangeRateDTO.setTimestamp(timestamp);
        exchangeRateDTO.setSuccess(true);
        Map<String, BigDecimal> ratesMap = new HashMap<>();
        ratesMap.put("USD",BigDecimal.valueOf(1.080345));
        exchangeRateDTO.setRates(ratesMap);
        return new ResponseEntity<ExchangeRateDTO>(exchangeRateDTO, HttpStatus.OK);
    }
}
