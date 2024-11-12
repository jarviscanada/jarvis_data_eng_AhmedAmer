package ca.jrvs.apps.practice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotSoSimpleCalculator_Test {

    NotSoSimpleCalculator notSimpleCalc;

    @Mock
    SimpleCalculator mockSimpleCalc;


    @BeforeEach
    void init(){
        notSimpleCalc = new NotSoSimpleCalculatorImp(mockSimpleCalc);
    }

    @Test
    void test_power(){
        double expected = 64;
        double actual = notSimpleCalc.power(2,6);
        assertEquals(expected, actual);
    }

    @Test
    void test_abs(){
        when(notSimpleCalc.abs(128)).thenReturn(128);
        int expected = 128;
        int actual = notSimpleCalc.abs(128);
        assertEquals(expected, actual);
    }

    @Test
    void test_sqrt(){
        double expected = 8.0;
        double actual = notSimpleCalc.sqrt(64);
        assertEquals(expected, actual);
    }
}
