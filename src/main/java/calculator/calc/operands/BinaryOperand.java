package calculator.calc.operands;

import java.util.function.BiFunction;

public class BinaryOperand extends AbstractOperand {

    private final OperandWithPriority value1;
    private final OperandWithPriority value2;
    private final BinaryAction action;

    @Override
    public double returnValue() {
        return action.doAction(value1.returnValue(), value2.returnValue());
    }

    public BinaryOperand(int priority, OperandWithPriority value1, OperandWithPriority value2, BinaryAction action) {
        super(priority);
        this.value1 = value1;
        this.value2 = value2;
        this.action = action;
    }

    public enum BinaryAction {
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
    }
}
