package by.weekmenu.api.entity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class CurrencyTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @Before
    public void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void testNameIsNull() {
        Currency currency = new Currency(null, "BYN", true);
        Set<ConstraintViolation<Currency>> violations = validator.validate(currency);
        assertEquals(violations.size(), 1);
        assertEquals("Currency must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testNameIsBlank() {
        Currency currency = new Currency("   ", "BYN", true);
        Set<ConstraintViolation<Currency>> violations = validator.validate(currency);
        assertEquals(violations.size(), 1);
        assertEquals("Currency must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testNameIsEmpty() {
        Currency currency = new Currency("", "BYN", true);
        Set<ConstraintViolation<Currency>> violations = validator.validate(currency);
        assertEquals(violations.size(), 1);
        assertEquals("Currency must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testCodeIsNull() {
        Currency currency = new Currency("руб.", null, true);
        Set<ConstraintViolation<Currency>> violations = validator.validate(currency);
        assertEquals(violations.size(), 1);
        assertEquals("Currency must have code.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testCodeIsBlank() {
        Currency currency = new Currency("руб.", "   ", true);
        Set<ConstraintViolation<Currency>> violations = validator.validate(currency);
        assertEquals(violations.size(), 1);
        assertEquals("Currency must have code.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testCodeIsEmpty() {
        Currency currency = new Currency("руб.", "", true);
        Set<ConstraintViolation<Currency>> violations = validator.validate(currency);
        List<String> messages = violations.stream()
                .map((ConstraintViolation<Currency> violation) -> violation.getMessage())
                .collect(Collectors.toList());
        assertEquals(violations.size(), 2);
        assertTrue(messages.contains("Currency's code '' must be '3' characters long."));
        assertTrue(messages.contains("Currency must have code."));
    }

    @Test
    public void testCodeHasTooManyCharacters() {
        Currency currency = new Currency("руб.", "BYNBYN", true);
        Set<ConstraintViolation<Currency>> violations = validator.validate(currency);
        assertEquals(violations.size(), 1);
        assertEquals("Currency's code 'BYNBYN' must be '3' characters long.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testSymbolIsInvalid() {
        Currency currency = new Currency("руб.", "BYN", true);
        currency.setSymbol("%");
        Set<ConstraintViolation<Currency>> violations = validator.validate(currency);
        assertEquals(violations.size(), 1);
        assertEquals("Currency's symbol '%' must match pattern.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIsActiveIsNull() {
        Currency currency = new Currency("руб.", "BYN", null);
        Set<ConstraintViolation<Currency>> violations = validator.validate(currency);
        assertEquals(violations.size(), 1);
        assertEquals("Currency must have field 'isActive' defined.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIsValid() {
        Currency currency = new Currency("руб.", "BYN", true);
        currency.setSymbol("$");
        Set<ConstraintViolation<Currency>> violations = validator.validate(currency);
        assertEquals(violations.size(), 0);
    }

    @After
    public void tearDown() {
        validatorFactory.close();
    }
}