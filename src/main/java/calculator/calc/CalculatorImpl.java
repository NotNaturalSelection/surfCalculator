package calculator.calc;

import java.util.EmptyStackException;
import java.util.Stack;
import java.util.function.BiFunction;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CalculatorImpl implements Calculator {

    @Value("${calculator.signs}")
    private String signs;

    @Value("${calculator.delimiter}")
    private char delimiter;

    @Override
    public double calculate(String expression)
            throws InvalidExpressionException {
        try {
            return calculateFromRPN(toRPN(expression));
        } catch (EmptyStackException | NumberFormatException e) {
            throw new InvalidExpressionException();
        }
    }

    private double calculateFromRPN(String rpn) {
        Stack<Double> operands = new Stack<>();
        for (String lec : rpn.split(" ")) {
            if (signs.contains(lec)) {
                Double value2 = operands.pop();
                Double value1 = operands.pop();
                operands.push(BinaryAction.ofSign(lec).doAction(value1, value2));
            } else {
                operands.push(Double.parseDouble(lec));
            }
        }
        return operands.pop();
    }

    private enum BinaryAction {
        addition(Double::sum),
        subtraction((aDouble, aDouble2) -> aDouble - aDouble2),
        multiplication((aDouble, aDouble2) -> aDouble * aDouble2),
        division((aDouble, aDouble2) -> aDouble / aDouble2),
        exponentiation(Math::pow);

        private final BiFunction<Double, Double, Double> action;

        BinaryAction(BiFunction<Double, Double, Double> action) {
            this.action = action;
        }

        double doAction(double value1, double value2) {
            return action.apply(value1, value2);
        }

        static BinaryAction ofSign(String sign) {
            switch (sign) {
                case "+":
                    return addition;
                case "-":
                    return subtraction;
                case "*":
                    return multiplication;
                case "/":
                    return division;
                case "^":
                    return exponentiation;
                default:
                    throw new UnsupportedOperationException("There's no operation of sign: " + sign);
            }
        }
    }

    private String toRPN(String source) {
        Stack<Character> operations = new Stack<>();
        StringBuilder sb = new StringBuilder();
        source = replaceUnaryMinus(source);

        boolean isNumber = true;
        boolean isUnaryMinus = false;
        for (int i = 0; i < source.length(); i++) {
            Character current = source.charAt(i);
            if (Character.isDigit(current) || current == delimiter) {
                if (!isNumber) {
                    sb.append(' ');
                }
                if (isUnaryMinus) {
                    sb.append('-');
                    isUnaryMinus = false;
                }
                sb.append(current);
                isNumber = true;
            }
            if (current == '_') {
                isUnaryMinus = true;
            }
            if (current == '(') {
                operations.push(current);
                isNumber = false;
            }
            if (current == ')') {
                isNumber = false;
                Character out;
                do {
                    out = operations.pop();
                    if (out != '(') {
                        sb.append(' ');
                        sb.append(out);
                    }
                } while (!operations.isEmpty() && out != '(');
            }
            if (signs.contains(current.toString())) {
                isNumber = false;
                if (!operations.isEmpty() && !isStackContainsLessPriorityCharacters(current, operations)) {
                    Character out = operations.peek();
                    while (!operations.isEmpty() && symbolPriority(out) >= symbolPriority(current)) {
                        out = operations.pop();
                        sb.append(' ');
                        sb.append(out);
                        if (!operations.isEmpty()) {
                            out = operations.peek();
                        }
                    }
                }
                operations.push(current);
            }
        }
        while (!operations.isEmpty()) {
            sb.append(' ');
            sb.append(operations.pop());
        }
        return sb.toString().trim();
    }

    private boolean isStackContainsLessPriorityCharacters(Character current, Stack<Character> operations) {
        int stackMaxPriority = operations.stream().mapToInt(this::symbolPriority).max().orElse(0);
        return symbolPriority(current) > stackMaxPriority;
    }

    private int symbolPriority(Character current) {
        switch (current) {
            case '(':
                return 1;
            case '+':
            case '-':
                return 2;
            case '*':
            case '/':
                return 3;
            case '^':
                return 4;
            default:
                return 0;
        }
    }

    private String replaceUnaryMinus(String source) {
        StringBuilder result = new StringBuilder(source.replaceAll(" ", ""));
        for (int i = 0; i < result.length(); i++) {
            if (result.charAt(i) == '-') {
                if (i == 0 || result.charAt(i - 1) == '(' || signs.contains(Character.valueOf(result.charAt(i - 1)).toString())) {
                    result.setCharAt(i, '_');
                }
            }
        }
        return result.toString();
    }
}
