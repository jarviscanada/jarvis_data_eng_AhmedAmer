package ca.jrvs.apps.practice;

class NotSoSimpleCalculatorImp implements NotSoSimpleCalculator {
    private final SimpleCalculator calc;
    public NotSoSimpleCalculatorImp(SimpleCalculator calc) {
        this.calc = calc;
    }

    @Override
    public double power(int x, int y) {
        return Math.pow(x, y);
    }

    @Override
    public int abs(int x) {
        //Method not implemented correctly
        return calc.multiply(x, -1);
    }

    @Override
    public double sqrt(int x) {
        return Math.sqrt(x);
    }
}
