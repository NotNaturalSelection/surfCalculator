package calculator.calc;

import calculator.data.entities.CalcResult;

public interface CalculationService {
    CalcResult calculate(String expression)
            throws InvalidExpressionException;
}
