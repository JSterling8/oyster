package com.jms.oyster.service;

import com.jms.oyster.exception.CardNotFoundException;
import com.jms.oyster.model.Card;

public interface CardService {
    Card getCard(Integer cardNum) throws CardNotFoundException;
    Card createCard(String ownerName);
}
