package com.jms.oyster.service.impl;

import com.jms.oyster.exception.CardNotFoundException;
import com.jms.oyster.model.Card;
import com.jms.oyster.service.CardService;
import org.springframework.stereotype.Service;

@Service
public class CardServiceImpl implements CardService {

    @Override
    public Card getCard(Integer cardNum) throws CardNotFoundException {
        return new Card(1, "Jonathan Sterling", 0.0D);
    }
}
