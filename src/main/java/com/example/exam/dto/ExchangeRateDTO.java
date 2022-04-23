package com.example.exam.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

@Getter
@Setter
public class ExchangeRateDTO {

    private Boolean success;
    private Timestamp timestamp;
    private String base;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date date;
    Map<String, BigDecimal> rates;

}
