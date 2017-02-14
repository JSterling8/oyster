package com.jms.oyster.controller;

import com.jms.oyster.exception.CardNotFoundException;
import com.jms.oyster.model.Card;
import com.jms.oyster.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CardController {
    private final CardService cardService;

    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @RequestMapping(value = "/card", method = RequestMethod.GET)
    public ModelAndView getCard(@RequestParam Integer cardNum) {
        try {
            Card card = cardService.getCard(cardNum);

            ModelAndView mav = new ModelAndView("dashboard");
            mav.addObject(card);

            return mav;
        } catch (CardNotFoundException e) {
            return new ModelAndView("register");
        }
    }
}
