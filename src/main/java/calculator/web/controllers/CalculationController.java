package calculator.web.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import calculator.calc.InvalidExpressionException;
import calculator.data.HistoryServiceImpl;

@RestController
@RequestMapping("/calculator")
public class CalculationController {

    @Autowired
    private HistoryServiceImpl historyService;

    @PostMapping(value = "/calculate")
    public Double calculate(
            @RequestParam
                    String expression, Principal principal
    ) throws InvalidExpressionException {
        return historyService.calculateAndSave(principal.getName(), expression);
    }
}
