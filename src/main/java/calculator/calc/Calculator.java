package calculator.calc;

public interface Calculator {
    double calculate(String expression) throws InvalidExpressionException;
}
