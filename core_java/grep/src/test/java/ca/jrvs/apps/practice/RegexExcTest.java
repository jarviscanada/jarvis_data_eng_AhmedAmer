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
        RegexExc testRegex = new RegexComparator();
        assertAll(() -> assertTrue(testRegex.ipMatch("0.0.0.0")),
                () -> assertTrue(testRegex.ipMatch("999.999.999.999")),
                () -> assertFalse(testRegex.ipMatch("r.e.3.2")),
                () -> assertFalse(testRegex.ipMatch("yyy.yyy.yyy.yyy")),
                () -> assertTrue(testRegex.ipMatch("3.234.23.1"))
        );
    }

    @Test
    public void testIsEmptyLine() {
        RegexExc testRegex = new RegexComparator();
        assertAll(() -> assertTrue(testRegex.isEmptyLine("")),
                () -> assertTrue(testRegex.isEmptyLine("    ")),
                () -> assertFalse(testRegex.isEmptyLine(" y ")),
                () -> assertFalse(testRegex.isEmptyLine(" _ __ ! ?      ")));
    }
}