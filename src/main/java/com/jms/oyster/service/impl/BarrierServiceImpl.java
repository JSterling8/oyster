package com.jms.oyster.service.impl;

import com.jms.oyster.exception.IllegalParameterException;
import com.jms.oyster.exception.InsufficientCardBalanceException;
import com.jms.oyster.model.Barrier;
import com.jms.oyster.model.Card;
import com.jms.oyster.repository.JourneyRepository;
import com.jms.oyster.service.BarrierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

import static com.jms.oyster.model.Barrier.Direction;
import static com.jms.oyster.model.Barrier.Type;

@Service
public class BarrierServiceImpl implements BarrierService {

    public static final double MAX_COST = 3.2D;
    public static final double BUS_COST = 1.8D;
    
    private static final double COST_ONLY_ZONE_ONE = 2.5D;
    private static final double COST_ONE_ZONE_NOT_INCLUDING_ZONE_ONE = 2.0D;
    private static final double COST_TWO_ZONES_INCLUDING_ZONE_ONE = 3.0D;
    private static final double COST_TWO_ZONES_EXCLUDING_ZONE_ONE = 2.25D;

    private final JourneyRepository journeyRepository;

    @Autowired
    public BarrierServiceImpl(JourneyRepository journeyRepository) {
        this.journeyRepository = journeyRepository;
    }

    @Override
    public Card attemptToPassBarrier(Barrier barrier, Card card)
            throws InsufficientCardBalanceException, IllegalParameterException {
        Barrier mostRecentTubeBarrierPassed = journeyRepository.getMostRecentTubeBarrierPassed(card.getNumber());

        verifyTripIsValid(barrier, mostRecentTubeBarrierPassed);

        if(isUserDueRefund(mostRecentTubeBarrierPassed)) {
            card.addAmount(MAX_COST);
        }

        Double cost = getCost(barrier, card);

        if(card.getBalance() < cost) {
            throw new InsufficientCardBalanceException("Not enough money on card to pay fare");
        }

        card.removeAmount(cost);

        if(barrier.getType() == Type.TUBE) {
            journeyRepository.addJourney(card.getNumber(), barrier);
        }

        return card;
    }

    @Override
    public boolean mustHaveCrossedZoneOne(Set<Integer> from, Set<Integer> to, int minZonesCrossed) {
        return (from.size() == 1 && from.contains(1)) || (to.size() == 1 && to.contains(1));

    }

    @Override
    public int getMinZonesCrossed(Set<Integer> from, Set<Integer> to) {
        int minZonesVisited = Integer.MAX_VALUE;

        outerloop:
        for(int fromZone : from) {
            for(int toZone: to) {
                int zonesVisited = Math.abs(fromZone - toZone) + 1;
                if(zonesVisited < minZonesVisited) {
                    minZonesVisited = zonesVisited;
                }

                if(minZonesVisited == 1) {
                    break outerloop;
                }
            }
        }

        return minZonesVisited;
    }

    private boolean isUserDueRefund(Barrier mostRecentTubeBarrierPassed) {
        return mostRecentTubeBarrierPassed != null && mostRecentTubeBarrierPassed.getDirection() == Direction.IN;
    }

    private Double getCost(Barrier barrier, Card card) {
        if(barrier.getType() == Type.BUS) {
            return BUS_COST;
        }

        Barrier tubeBarrierComingFrom = journeyRepository.getMostRecentTubeBarrierPassed(card.getNumber());

        if(isStartingTubeJourney(tubeBarrierComingFrom)) {
            return MAX_COST;
        }

        int minZonesCrossed = getMinZonesCrossed(tubeBarrierComingFrom.getZones(), barrier.getZones());
        boolean zoneOneCrossed = mustHaveCrossedZoneOne(tubeBarrierComingFrom.getZones(), barrier.getZones(), minZonesCrossed);

        return getCost(minZonesCrossed, zoneOneCrossed);
    }

    private Double getCost(int minZonesCrossed, boolean zoneOneCrossed) {
        if(crossedOnlyZoneOne(minZonesCrossed, zoneOneCrossed)) {
            return COST_ONLY_ZONE_ONE;
        }

        if(crossedOneZoneOutsideOfZoneOne(minZonesCrossed, zoneOneCrossed)) {
            return COST_ONE_ZONE_NOT_INCLUDING_ZONE_ONE;
        }

        if(crossedTwoZonesIncludingZoneOne(minZonesCrossed, zoneOneCrossed)) {
            return COST_TWO_ZONES_INCLUDING_ZONE_ONE;
        }

        if(crossedTwoZonesExcludingZoneOne(minZonesCrossed, zoneOneCrossed)) {
            return COST_TWO_ZONES_EXCLUDING_ZONE_ONE;
        }

        if(crossedThreeZones(minZonesCrossed)) {
            return MAX_COST;
        }

        return MAX_COST;
    }

    private boolean isStartingTubeJourney(Barrier comingFromTubeBarrier) {
        return comingFromTubeBarrier == null || comingFromTubeBarrier.getDirection() == Direction.OUT;
    }

    private void verifyTripIsValid(Barrier barrier, Barrier mostRecentTubeBarrierPassed) throws IllegalParameterException {
        if(barrier.getDirection() == Direction.OUT &&
                (isStartingTubeJourney(mostRecentTubeBarrierPassed))) {
            throw new IllegalParameterException("You can't tap out when you never tapped in.");
        }
    }

    private boolean crossedThreeZones(int minZonesCrossed) {
        return minZonesCrossed == 3;
    }

    private boolean crossedTwoZonesExcludingZoneOne(int minZonesCrossed, boolean zoneOneCrossed) {
        return minZonesCrossed == 2 && !zoneOneCrossed;
    }

    private boolean crossedTwoZonesIncludingZoneOne(int minZonesCrossed, boolean zoneOneCrossed) {
        return minZonesCrossed == 2 && zoneOneCrossed;
    }

    private boolean crossedOnlyZoneOne(int minZonesCrossed, boolean zoneOneCrossed) {
        return minZonesCrossed == 1 && zoneOneCrossed;
    }

    private boolean crossedOneZoneOutsideOfZoneOne(int minZonesCrossed, boolean zoneOneCrossed) {
        return minZonesCrossed == 1 && !zoneOneCrossed;
    }
}
