package calculator.calc;

import java.util.PriorityQueue;

import calculator.calc.operands.AbstractOperand;

public class ExpressionImpl implements Expression {
    private PriorityQueue<AbstractOperand> operations = new PriorityQueue<>();

    public static ExpressionImpl from(String expression) {
        ExpressionImpl result = new ExpressionImpl();
        expression = expression.replaceAll(" ", "");
        int priority = 0;
        char current;
        for (int i = 0; i < expression.length(); i++) {
            current = expression.charAt(i);
            if (Character.isDigit(current) || current == '-') {

            }
        }
        return result;
    }

    public double calculate() {
        return 0;
    }
}
