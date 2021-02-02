package calculator.calc;

import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RPNCalculator implements Calculator, InitializingBean {

    private static final Character UNDERSCORE = '_';
    private static final Character MINUS = '-';
    private static final String SIGNS = "+-*/^";

    @Value("${calculator.regexp}")
    private String regexp;

    @Value("${calculator.delimiter}")
    private char delimiter;

    private Pattern pattern;

    @Override
    public double calculate(String expression)
            throws InvalidExpressionException {
        if (!pattern.matcher(expression).matches()) {
            throw new InvalidExpressionException();
        }
        try {
            return calculateRPN(toRPN(expression));
        } catch (EmptyStackException | NumberFormatException e) {
            throw new InvalidExpressionException();
        }
    }

    private double calculateRPN(List<String> rpn) {
        Stack<Double> operands = new Stack<>();
        for (String lec : rpn) {
            if (SIGNS.contains(lec)) {
                Double value2 = operands.pop();
                Double value1 = operands.pop();
                operands.push(BinaryAction.ofSign(lec).doAction(value1, value2));
            } else {
                operands.push(Double.parseDouble(lec));
            }
        }
        return operands.pop();
    }

    private boolean isPartOfNumber(Character c) {
        return Character.isDigit(c) || c == delimiter || c == UNDERSCORE;
    }

    private List<String> toRPN(String source) {
        Stack<Character> operations = new Stack<>();
        List<String> result = new LinkedList<>();
        source = replaceMinusWithUnderscore(source);

        for (int i = 0; i < source.length(); i++) {
            Character current = source.charAt(i);

            if (isPartOfNumber(current)) {
                StringBuilder sb = new StringBuilder();
                do {
                    sb.append(current);
                    if (i + 1 < source.length()) {
                        current = source.charAt(++i);
                    } else {
                        break;
                    }
                } while (isPartOfNumber(current));
                result.add(sb.toString().replace(UNDERSCORE, MINUS));
            }

            if (current == '(') {
                operations.push(current);
            }

            if (current == ')') {
                Character out = operations.pop();
                while (!operations.isEmpty() && out != '(') {
                    result.add(out.toString());
                    out = operations.pop();
                }
            }

            if (SIGNS.contains(current.toString())) {
                if (!operations.isEmpty() && !isStackContainsLessPriorityCharacters(current, operations)) {
                    Character out = operations.peek();
                    while (!operations.isEmpty() && symbolPriority(out) >= symbolPriority(current)) {
                        out = operations.pop();
                        result.add(out.toString());
                        if (!operations.isEmpty()) {
                            out = operations.peek();
                        }
                    }
                }
                operations.push(current);
            }
        }

        while (!operations.isEmpty()) {
            result.add(operations.pop().toString());
        }

        return result;
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

    private String replaceMinusWithUnderscore(String source) {
        StringBuilder result = new StringBuilder(source.replaceAll(" ", ""));
        for (int i = 0; i < result.length(); i++) {
            if (result.charAt(i) == MINUS) {
                if (i == 0 || result.charAt(i - 1) == '(' || SIGNS.contains(Character.valueOf(result.charAt(i - 1)).toString())) {
                    result.setCharAt(i, UNDERSCORE);
                }
            }
        }
        return result.toString();
    }

    @Override
    public void afterPropertiesSet() {
        pattern = Pattern.compile(regexp);
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
}
