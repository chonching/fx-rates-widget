package com.example.exam.service;

import com.example.exam.common.MessageConstants;
import com.example.exam.dto.CurrencyDTO;
import com.example.exam.exception.InvalidRequestException;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ValidateRequestService {

    private static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static Validator validator = factory.getValidator();

    public void validate(CurrencyDTO currencyDTO){
        Set<ConstraintViolation<CurrencyDTO>> violations = validator.validate(currencyDTO);
        checkBuyAndSellAmount(currencyDTO);
        if (!violations.isEmpty())
            throw new InvalidRequestException(interpretViolation(violations));
    }

    private String interpretViolation(Set<ConstraintViolation<CurrencyDTO>> violations) {
        return violations.stream().map(ConstraintViolation::getMessage).sorted().collect(Collectors.joining(", "));
    }

    private void checkBuyAndSellAmount(CurrencyDTO currencyDTO){
        if ((Objects.isNull(currencyDTO.getBuyAmount()) || currencyDTO.getBuyAmount().compareTo(BigDecimal.ZERO) == 0)
                && (Objects.isNull(currencyDTO.getSellAmount()) || currencyDTO.getSellAmount().compareTo(BigDecimal.ZERO) == 0)){
            throw new InvalidRequestException(MessageConstants.ZERO_NULL_BUY_SELL_AMOUNT);
        } else if((Objects.nonNull(currencyDTO.getBuyAmount()) && currencyDTO.getBuyAmount().compareTo(BigDecimal.ZERO) == 0)
                && (Objects.nonNull(currencyDTO.getSellAmount()) && currencyDTO.getSellAmount().compareTo(BigDecimal.ZERO) == 0)){
            throw new InvalidRequestException(MessageConstants.ZERO_NULL_BUY_SELL_AMOUNT);
        } else if ((Objects.nonNull(currencyDTO.getBuyAmount()) && currencyDTO.getBuyAmount().compareTo(BigDecimal.ZERO) > 0)
                && (Objects.nonNull(currencyDTO.getSellAmount()) && currencyDTO.getSellAmount().compareTo(BigDecimal.ZERO) > 0)){
            throw new InvalidRequestException(MessageConstants.BUY_AND_SELL_AMOUNT_FOUND);
        }
    }

}
