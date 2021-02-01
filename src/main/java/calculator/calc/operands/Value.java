package calculator.calc.operands;

public class Value extends AbstractOperand {
    private final double value;

    @Override
    public double returnValue() {
        return value;
    }

    public Value(int priority, double value) {
        super(priority);
        this.value = value;
    }
}
