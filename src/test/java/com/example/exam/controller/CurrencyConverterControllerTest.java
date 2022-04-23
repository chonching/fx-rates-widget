package com.example.exam.controller;

import com.example.exam.config.CurrencyConverterConfig;
import com.example.exam.dto.CurrencyDTO;
import com.example.exam.service.ValidateRequestService;
import com.example.exam.service.impl.CurrencyConverterServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = CurrencyConverterController.class)
class CurrencyConverterControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    ValidateRequestService validateRequestService;
    @SpyBean
    CurrencyConverterServiceImpl currencyConverterService;
    CurrencyDTO currencyDTO;
    @MockBean
    private CurrencyConverterConfig currencyConverterConfig;

    @BeforeEach
    void setup() {
        currencyDTO = new CurrencyDTO();
        currencyDTO.setBuyCurrency("USD");
        currencyDTO.setSellCurrency("PHP");
        currencyDTO.setBuyAmount(BigDecimal.valueOf(10));
        currencyDTO.setSellAmount(BigDecimal.ZERO);
    }

    @AfterEach
    void tearDown() {
        currencyDTO = null;
    }

    @Test
    void testConvertCurrency() throws Exception {
        ReflectionTestUtils.setField(currencyConverterService, "currencyConverterConfig", getConfig());
        currencyDTO = new CurrencyDTO();
        currencyDTO.setBuyCurrency("USD");
        currencyDTO.setSellCurrency("PHP");
        currencyDTO.setBuyAmount(BigDecimal.valueOf(10));
        currencyDTO.setSellAmount(BigDecimal.ZERO);
        mockMvc.perform(MockMvcRequestBuilders.post("/exchangerates/convert")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(currencyDTO)))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.currencyDTO").exists())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE));
    }

    private CurrencyConverterConfig getConfig(){
        CurrencyConverterConfig currencyConverterConfig = new CurrencyConverterConfig();
        currencyConverterConfig.setApiKey("7a9cdc90da484bb74f4e994a14decab9");
        currencyConverterConfig.setApiUrl("http://api.exchangeratesapi.io/v1/latest?access_key=%s&symbols=%s");
        return currencyConverterConfig;

    }
}
