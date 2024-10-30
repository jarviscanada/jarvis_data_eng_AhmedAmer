package ca.jrvs.apps.practice;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class RegexExcTest {

    @Test
    public void testJpegMatch() {
        RegexExc testRegex = new RegexComparator();
        assertAll(() -> assertTrue(testRegex.jpegMatch("random.jpeg")),
                () -> assertFalse(testRegex.jpegMatch("random.jspg")),
                () -> assertTrue(testRegex.jpegMatch("random.jpg")),
                () -> assertFalse(testRegex.jpegMatch("jpeg.pdf")));
//
    }

    @Test
    public void testIpMatch() {
    }

    @Test
    public void testIsEmptyLine() {
    }
}