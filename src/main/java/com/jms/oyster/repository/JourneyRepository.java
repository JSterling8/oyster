package com.jms.oyster.repository;

import com.jms.oyster.model.Barrier;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class JourneyRepository {
    private Map<Integer, Barrier> mostRecentTubeBarrierPassing = new HashMap<>();

    public void addJourney(Integer cardNumber, Barrier barrier) {
        mostRecentTubeBarrierPassing.put(cardNumber, barrier);
    }

    public Barrier getMostRecentTubeBarrierPassed(Integer cardNumber) {
        return mostRecentTubeBarrierPassing.get(cardNumber);
    }
}
