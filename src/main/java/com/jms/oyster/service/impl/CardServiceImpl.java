package com.jms.oyster.service.impl;

import com.jms.oyster.exception.CardNotFoundException;
import com.jms.oyster.model.Card;
import com.jms.oyster.repository.CardRepository;
import com.jms.oyster.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CardServiceImpl implements CardService {
    private static final double DEFAULT_STARTING_BALANCE = 0.0D;
    private final CardRepository cardRepository;
    private final Random random = new Random(System.currentTimeMillis());

    @Autowired
    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public Card createCard(String ownerName) {
        Card card = new Card(Math.abs(random.nextInt()), ownerName, DEFAULT_STARTING_BALANCE);

        return cardRepository.createCard(card);
    }

    @Override
    public Card getCard(Integer cardNum) throws CardNotFoundException {
        return cardRepository.getCard(cardNum);
    }
}
