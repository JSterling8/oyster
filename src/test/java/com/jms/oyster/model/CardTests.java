package com.jms.oyster.model;

import com.jms.oyster.exception.IllegalParameterException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class CardTests {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private static final Double BALANCE = 5.0D;
    private static final String OWNER_NAME = "An Owner Name";
    private static final Integer CARD_NUMBER = 11;

    private Card card;

    @Before
    public void setup() {
        card = new Card(CARD_NUMBER, OWNER_NAME, BALANCE);
    }

    @Test
    public void constructor_shouldSetInitialValuesBasedOnParams(){
        assertEquals(BALANCE, card.getBalance());
        assertEquals(OWNER_NAME, card.getOwnerName());
        assertEquals(CARD_NUMBER, card.getNumber());
    }

    @Test
    public void addAmount_shouldAddToBalance() throws IllegalParameterException {
        Double amountToAdd = 5.5D;

        card.addAmount(amountToAdd);

        Double expectedBalance = BALANCE + amountToAdd;
        assertEquals(expectedBalance, card.getBalance());
    }

    @Test
    public void addAmount_shouldNotAllowNegativeInput() throws IllegalParameterException {
        thrown.expect(IllegalParameterException.class);
        card.addAmount(-5.0D);

        assertEquals(BALANCE, card.getBalance());
    }

    @Test
    public void removeAmount_shouldNotAllowNegativeBalance() throws IllegalParameterException {
        thrown.expect(IllegalParameterException.class);
        card.removeAmount(-5.0D);

        assertEquals(BALANCE, card.getBalance());
    }

    @Test
    public void removeAmount_shouldNotAllowNegativeInput() throws IllegalParameterException {
        thrown.expect(IllegalParameterException.class);
        card.removeAmount(-5.0D);

        assertEquals(BALANCE, card.getBalance());
    }
}
