import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import calculator.calc.Calculator;
import calculator.calc.RPNCalculator;
import calculator.calc.InvalidExpressionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

@SpringBootTest(classes = RPNCalculator.class)
@TestPropertySource(locations = "classpath: test.properties")
@RunWith(SpringRunner.class)
public class TestCalculator {

    @Autowired
    private Calculator calculator;

    @Test
    public void testCalculations()
            throws InvalidExpressionException {
        assert calculator.calculate("3+2-8^1") == -3;
        assertFalse(calculator.calculate("3.1^4 - 8/4") == 90.3521);
        assertEquals(90.3521, calculator.calculate("3.1^4 - 8/4"), 0.0000001);
        assertNotEquals(90.3521, calculator.calculate("3.1^4 - 8/4"), 0.00000000000000001);
        assert calculator.calculate("(1-6)*4^2") == -80;
        assert calculator.calculate("3*3/5*2-1*6") == -2.4;
        assert calculator.calculate("(1+1)*5") == 10;
        assertFalse(calculator.calculate("0+2") == -3);
    }

    @Test(expected = InvalidExpressionException.class)
    public void testInvalidExpression()
            throws InvalidExpressionException {
        calculator.calculate("-3+*2");
    }

    @Test(expected = InvalidExpressionException.class)
    public void testNotEnoughBraces()
            throws InvalidExpressionException {
        calculator.calculate("(1+4)*(5+2");
    }
}