package com.jms.oyster.service.impl;

import com.jms.oyster.exception.CardNotFoundException;
import com.jms.oyster.model.Card;
import com.jms.oyster.service.CardService;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@Service
public class CardServiceImpl implements CardService {

    @Override
    public Card createCard(String ownerName) {
        throw new NotImplementedException();
    }

    @Override
    public Card getCard(Integer cardNum) throws CardNotFoundException {
        throw new NotImplementedException();
    }
}
