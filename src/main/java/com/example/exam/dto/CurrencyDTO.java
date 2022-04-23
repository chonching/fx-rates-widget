package com.example.exam.dto;

import com.example.exam.common.MessageConstants;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
public class CurrencyDTO implements Serializable {

    @ApiModelProperty(notes = "Buy Currency", name = "buyCurrency", required = true)
    @NotEmpty(message = MessageConstants.BUY_CURRENY_NOT_FOUND)
    private String buyCurrency;

    @ApiModelProperty(notes = "Sell Currency", name = "sellCurrency", required = true)
    @NotEmpty(message = MessageConstants.SELL_CURRENCY_NOT_FOUND)
    private String sellCurrency;

    @ApiModelProperty(notes = "Buy Amount", name = "buyAmount")
    @DecimalMin(value = "0.0")
    private BigDecimal buyAmount = BigDecimal.ZERO;

    @ApiModelProperty(notes = "Sell Amount", name = "sellAmount")
    @DecimalMin(value = "0.0")
    private BigDecimal sellAmount = BigDecimal.ZERO;
}
