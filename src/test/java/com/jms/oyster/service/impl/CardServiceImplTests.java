package com.jms.oyster.service.impl;


import com.jms.oyster.exception.CardNotFoundException;
import com.jms.oyster.model.Card;
import com.jms.oyster.service.CardService;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CardServiceImplTests {
    private static final String OWNER_NAME = "An Owner Name";
    private CardService cardService;

    @Before
    public void setup() {
        cardService = new CardServiceImpl();
    }

    @Test
    public void createCard_shouldCreateACard() {
        Card created = cardService.createCard(OWNER_NAME);

        assertNotNull(created.getNumber());
        assertNotNull(created.getBalance());
        assertEquals(OWNER_NAME, created.getOwnerName());
    }

    @Test
    public void getCard_shouldReturnCardIfExists() throws CardNotFoundException {
        Card expected = cardService.createCard(OWNER_NAME);
        Card actual = cardService.getCard(expected.getNumber());

        assertEquals(expected, actual);
    }

    @Test(expected = CardNotFoundException.class)
    public void getCard_shouldThrowNotFoundExceptionIfCardDoesNotExist() throws CardNotFoundException {
        cardService.getCard(Integer.MAX_VALUE);
    }
}
