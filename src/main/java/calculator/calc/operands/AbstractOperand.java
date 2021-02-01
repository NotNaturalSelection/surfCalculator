package calculator.calc.operands;

public abstract class AbstractOperand implements OperandWithPriority, Comparable<AbstractOperand> {
    private final int priority;

    @Override
    public int getPriority() {
        return priority;
    }

    public AbstractOperand(int priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(AbstractOperand abstractOperand) {
        return abstractOperand.priority - this.priority;
    }
}
