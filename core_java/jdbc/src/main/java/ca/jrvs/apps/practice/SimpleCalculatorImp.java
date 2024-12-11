package ca.jrvs.apps.practice;

public class SimpleCalculatorImp implements SimpleCalculator {
    @Override
    public int add(int x, int y) {
        return x + y;
    }

    @Override
    public int subtract(int x, int y) {
        return x - y;
    }

    @Override
    public int multiply(int x, int y) {
        return x * y;
    }

    @Override
    public double divide(int x, int y) {
        return (double) x / y;
    }

}
