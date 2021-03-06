package by.weekmenu.api.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.validation.Valid;
import javax.validation.constraints.*;

import lombok.EqualsAndHashCode;

@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id"})
@Entity
@Table(name = "INGREDIENT_CURRENCY")
public class IngredientCurrency {

    private static final long serialVersionUID = 1115642071168789374L;

    @Embeddable
    public static class Id implements Serializable {

        private static final long serialVersionUID = 1125642071168789374L;

        @Column(name = "INGREDIENT_ID")
        private Long ingredientId;

        @Column(name = "CURRENCY_ID")
        private Byte currencyId;

        public Id() {

        }

        public Id(Long ingredientId, Byte currencyId) {
            this.ingredientId = ingredientId;
            this.currencyId = currencyId;
        }

        public boolean equals(Object o) {
            if (o != null && o instanceof Id) {
                Id that = (Id) o;
                return this.ingredientId.equals(that.ingredientId) && this.currencyId.equals(that.currencyId);
            }

            return false;
        }

        public int hashCode() {
            return ingredientId.hashCode() + currencyId.hashCode();
        }

        public Byte getCurrencyId() {
            return currencyId;
        }

        public Long getIngredientId() {
            return ingredientId;
        }
    }

    @EmbeddedId
    private Id id = new Id();


    @Column(name = "PRICE_VALUE")
    @Digits(
            integer = 7,
            fraction = 2,
            message = "Ingredient_Currency's Price_Value '${validatedValue}' must have up to '{integer}' integer digits and '{fraction}' fraction digits."
    )
    @Positive(message = "Ingredient_Currency's Price_Value '${validatedValue}' must be positive.")
    private BigDecimal priceValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INGREDIENT_ID")
    @Valid
    @NotNull(message = "Ingredient_Currency's Ingredient mustn't be null.")
    private Ingredient ingredient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CURRENCY_ID")
    @Valid
    @NotNull(message = "Ingredient_Currency's Currency mustn't be null.")
    private Currency currency;

    public IngredientCurrency(BigDecimal priceValue, Ingredient ingredient, Currency currency) {
        this.priceValue = priceValue;
        this.ingredient = ingredient;
        this.currency = currency;
    }

    public IngredientCurrency(BigDecimal priceValue) {
        this.priceValue = priceValue;
    }

}
