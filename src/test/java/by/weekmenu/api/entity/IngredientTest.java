package by.weekmenu.api.entity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class IngredientTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @Before
    public void setUp() throws Exception {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    public void testIngredientNameIsNull() {
        Ingredient ingredient = new Ingredient(null, new Ownership("Пользователь"), new UnitOfMeasure("литр"));
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientNameIsBlank() {
        Ingredient ingredient = new Ingredient("   ", new Ownership("Пользователь"), new UnitOfMeasure("литр"));
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientNameIsEmpty() {
        Ingredient ingredient = new Ingredient("", new Ownership("Пользователь"), new UnitOfMeasure("литр"));
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientCaloriesAreNegative() {
        Ingredient ingredient = new Ingredient(-100, 100, 100, 100);
        ingredient.setName("молоко");
        ingredient.setUnitOfMeasure(new UnitOfMeasure("литр"));
        ingredient.setOwnership(new Ownership("Пользователь"));
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient's calories '-100' must be positive.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientCaloriesAreZero() {
        Ingredient ingredient = new Ingredient(0, 100, 100, 100);
        ingredient.setName("молоко");
        ingredient.setUnitOfMeasure(new UnitOfMeasure("литр"));
        ingredient.setOwnership(new Ownership("Пользователь"));
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient's calories '0' must be positive.",
                violations.iterator().next().getMessage());
    }


    @Test
    public void testIngredientProteinsAreNegative() {
        Ingredient ingredient = new Ingredient(100, -100, 100, 100);
        ingredient.setName("молоко");
        ingredient.setUnitOfMeasure(new UnitOfMeasure("литр"));
        ingredient.setOwnership(new Ownership("Пользователь"));
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient's proteins '-100' must be positive or '0'.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientFatsAreNegative() {
        Ingredient ingredient = new Ingredient(100, 100, -100, 100);
        ingredient.setName("молоко");
        ingredient.setUnitOfMeasure(new UnitOfMeasure("литр"));
        ingredient.setOwnership(new Ownership("Пользователь"));
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient's fats '-100' must be positive or '0'.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientCarbsAreNegative() {
        Ingredient ingredient = new Ingredient(100, 100, 100, -100);
        ingredient.setName("молоко");
        ingredient.setUnitOfMeasure(new UnitOfMeasure("литр"));
        ingredient.setOwnership(new Ownership("Пользователь"));
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient's carbs '-100' must be positive or '0'.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testHasInvalidIngredientCurrencies() {
        Ingredient ingredient = new Ingredient("курица", new Ownership("Пользователь"), new UnitOfMeasure("литр"));
        IngredientCurrency ingredientCurrency = new IngredientCurrency(new BigDecimal("-1.11"), ingredient, new Currency("руб", "BYN", "$", true));
        ingredient.getIngredientCurrencies().add(ingredientCurrency);
        ingredient.getIngredientCurrencies().add(null);
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        List<String> messages = violations.stream()
                .map((ConstraintViolation<Ingredient> violation) -> violation.getMessage())
                .collect(Collectors.toList());
        assertEquals(violations.size(), 2);
        assertTrue(messages.contains("Ingredient must have list of ingredientCurrencies without null elements."));
        assertTrue(messages.contains("Ingredient_Currency's Price_Value '-1.11' must be positive."));
    }

    @Test
    public void testHasInvalidRecipeIngredients() {
        Ingredient ingredient = new Ingredient("курица", new Ownership("Пользователь"), new UnitOfMeasure("литр"));
        RecipeIngredient recipeIngredient = new RecipeIngredient(new BigDecimal("-111.12"),
                new Ingredient("курица", new Ownership("пользователь"), new UnitOfMeasure("литр")),
                new Recipe("рецепт", true, new CookingMethod("жарка"), new Ownership("пользователь")));
        ingredient.getRecipeIngredients().add(recipeIngredient);
        ingredient.getRecipeIngredients().add(null);
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        List<String> messages = violations.stream()
                .map((ConstraintViolation<Ingredient> violation) -> violation.getMessage())
                .collect(Collectors.toList());
        assertEquals(violations.size(), 2);
        assertTrue(messages.contains("Ingredient must have list of recipeIngredients without null elements."));
        assertTrue(messages.contains("RecipeIngredient's qty '-111.12' must be positive."));
    }

    @Test
    public void testUnitOfMeasureIsNull() {
        Ingredient ingredient = new Ingredient("курица", new Ownership("Пользователь"), null);
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient's unitOfMeasure mustn't be null.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testUnitOfMeasureIsInvalid() {
        Ingredient ingredient = new Ingredient("курица", new Ownership("Пользователь"), new UnitOfMeasure(""));
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 1);
        assertEquals("UnitOfMeasure must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testOwnershipIsNull() {
        Ingredient ingredient = new Ingredient("курица", null, new UnitOfMeasure("литр"));
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 1);
        assertEquals("Ingredient's ownership mustn't be null.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testOwnershipIsInvalid() {
        Ingredient ingredient = new Ingredient("курица", new Ownership(""), new UnitOfMeasure("литр"));
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 1);
        assertEquals("Ownership must have name.",
                violations.iterator().next().getMessage());
    }

    @Test
    public void testIngredientIsValid() {
        Ingredient ingredient = new Ingredient("молоко",100, 100, 100, 100,
                new UnitOfMeasure("литр"), new Ownership("Пользователь"));
        RecipeIngredient recipeIngredient = new RecipeIngredient(new BigDecimal("111.12"),
                new Ingredient("курица", new Ownership("пользователь"), new UnitOfMeasure("литр")),
                new Recipe("рецепт", true, new CookingMethod("жарка"), new Ownership("пользователь")));
        ingredient.getRecipeIngredients().add(recipeIngredient);
        ingredient.getIngredientCurrencies().add(new IngredientCurrency(new BigDecimal("1.11"), ingredient, new Currency("руб", "BYN", "$", true)));
        Set<ConstraintViolation<Ingredient>> violations = validator.validate(ingredient);
        assertEquals(violations.size(), 0);
    }

    @After
    public void tearDown() {
        validatorFactory.close();
    }
}
