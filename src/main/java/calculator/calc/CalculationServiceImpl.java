package calculator.calc;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import calculator.data.entities.CalcResult;

@Service
public class CalculationServiceImpl implements CalculationService {

    @Autowired
    private Calculator calculator;

    @Override
    public CalcResult calculate(String expression)
            throws InvalidExpressionException {
        double doubleResult = calculator.calculate(expression);

        CalcResult calcResult = new CalcResult();
        calcResult.setExpression(expression);
        calcResult.setResult(doubleResult);
        calcResult.setDate(LocalDateTime.now());

        return calcResult;
    }
}
