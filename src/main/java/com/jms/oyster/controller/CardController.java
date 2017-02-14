package com.jms.oyster.controller;

import com.jms.oyster.exception.CardNotFoundException;
import com.jms.oyster.model.Card;
import com.jms.oyster.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller(value = "/card")
public class CardController {
    private final CardService cardService;

    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getCard(@RequestParam Integer cardNum) {
        try {
            Card card = cardService.getCard(cardNum);

            return getDashboardView(card);
        } catch (CardNotFoundException e) {
            return new ModelAndView("register");
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView getCard(@RequestBody String name) {
        Card card = cardService.createCard(name);

        return getDashboardView(card);
    }

    private ModelAndView getDashboardView(Card card) {
        ModelAndView mav = new ModelAndView("dashboard");
        mav.addObject(card);

        return mav;
    }
}
