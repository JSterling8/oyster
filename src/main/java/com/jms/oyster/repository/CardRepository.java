package com.jms.oyster.repository;

import com.jms.oyster.exception.CardNotFoundException;
import com.jms.oyster.model.Card;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CardRepository {
    private Map<Integer, Card> stub = new HashMap<>();

    public Card getCard(Integer cardNum) throws CardNotFoundException {
        Card card = stub.get(cardNum);

        if(card == null) {
            throw new CardNotFoundException("Card with number {" + cardNum + "} does not exist.");
        }

        return card;
    }

    public Card createCard(Card card) {
        stub.put(card.getNumber(), card);

        return card;
    }
}
