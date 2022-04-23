package com.example.exam.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDTO {

    @ApiModelProperty(notes = "Error Message", name = "errorMessage")
    private String errorMessage;

    @ApiModelProperty(notes = "Currency Object", name = "currencyDTO", required = true)
    private CurrencyDTO currencyDTO;
}
