package calculator.calc.operands;


import java.util.function.Function;

public class UnaryOperand extends AbstractOperand {

    private final OperandWithPriority value;
    private final UnaryAction action;

    @Override
    public double returnValue() {
        return action.doAction(value.returnValue());
    }

    public UnaryOperand(int priority, OperandWithPriority value, UnaryAction action) {
        super(priority);
        this.value = value;
        this.action = action;
    }

    public enum UnaryAction {
        sin(Math::sin),
        cos(Math::cos),
        tg(Math::tan),
        ctg(aDouble -> 1/Math.tan(aDouble)),
        sh(Math::sinh),
        ch(Math::cosh),
        th(Math::tanh),
        cth(aDouble -> 1/Math.tanh(aDouble));

        private final Function<Double, Double> action;

        UnaryAction(Function<Double, Double> action) {
            this.action = action;
        }

        double doAction(double value) {
            return action.apply(value);
        }
    }
}
