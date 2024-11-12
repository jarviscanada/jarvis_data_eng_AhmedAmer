package ca.jrvs.apps.practice;


import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SimpleCalculator_Test {

    SimpleCalculator calc;

    @BeforeEach
    void init() {
        calc = new SimpleCalculatorImp();
    }

    @Test
    public void test_add() {
        int expected = 2;
        int actual = calc.add(1,1);
        assertEquals(expected, actual);
    }

    @Test
    public void test_subtract() {
        int expected = 3;
        int actual = calc.subtract(5, 2);
        assertEquals(expected, actual);
    }

    @Test
    public void test_multiply() {
        int expected = 4;
        int actual = calc.multiply(2, 2);
        assertEquals(expected, actual);
    }

    @Test
    public void test_divide() {
        double expected = 5.0;
        double actual = calc.divide(25, 5);
        assertEquals(expected, actual);
    }
}
