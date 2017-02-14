package com.jms.oyster.service.impl;

import com.jms.oyster.model.Barrier;
import com.jms.oyster.model.Card;
import com.jms.oyster.repository.JourneyRepository;
import com.jms.oyster.service.BarrierService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.jms.oyster.model.Barrier.Direction;
import static com.jms.oyster.model.Barrier.Type;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class BarrierServiceImplTests {
    private static final double INITIAL_BALANCE = 30D;
    private static final String OWNER_NAME = "Owner Name";
    private static final int CARD_NUMBER = 1;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private BarrierService barrierService;

    @Mock
    private JourneyRepository journeyRepository;

    @Mock
    private Card card;

    @Before
    public void setup() {
        barrierService = new BarrierServiceImpl(journeyRepository);
        when(card.getBalance()).thenReturn(INITIAL_BALANCE);
        when(card.getNumber()).thenReturn(CARD_NUMBER);
        when(card.getOwnerName()).thenReturn(OWNER_NAME);
    }

    @Test
    public void attemptToPassBarrier_shouldRemoveMaxFareWhenPassingInForTubeJourney() throws Exception {
        Barrier barrier = new Barrier(generateZoneSetWithValues(1), "Holborn", Type.TUBE, Direction.IN);
        barrierService.attemptToPassBarrier(barrier, card);

        verify(card).removeAmount(BarrierServiceImpl.MAX_COST);
    }

    @Test
    public void attemptToPassBarrier_shouldRefundPreviousIntoTubeFareIfNewFareIsOutOfTube() throws Exception {
        Barrier previousBarrier = new Barrier(generateZoneSetWithValues(1), "Holborn", Type.TUBE, Direction.IN);
        Barrier currentBarrier = new Barrier(generateZoneSetWithValues(1, 2), "Earl's Court", Type.TUBE, Direction.OUT);
        when(journeyRepository.getMostRecentTubeBarrierPassed(CARD_NUMBER)).thenReturn(previousBarrier);
        barrierService.attemptToPassBarrier(currentBarrier, card);

        verify(card).addAmount(BarrierServiceImpl.MAX_COST);
    }

    @Test
    public void attemptToPassBarrier_shouldAddPassToJourneyRepository() throws Exception {
        Barrier currentBarrier = new Barrier(generateZoneSetWithValues(1, 2), "Earl's Court", Type.TUBE, Direction.IN);
        barrierService.attemptToPassBarrier(currentBarrier, card);

        verify(card, never()).addAmount(BarrierServiceImpl.MAX_COST);
    }

    @Test
    public void attemptToPassBarrier_shouldNotRefundFareIfNoPreviousJourney() throws Exception {
        Barrier currentBarrier = new Barrier(generateZoneSetWithValues(1, 2), "Earl's Court", Type.TUBE, Direction.IN);
        barrierService.attemptToPassBarrier(currentBarrier, card);

        verify(card, never()).addAmount(BarrierServiceImpl.MAX_COST);
    }

    @Test
    public void attemptToPassBarrier_shouldNotRefundFareIfPreviousTubeBarrierWasExit() throws Exception {
        Barrier previousBarrier = new Barrier(generateZoneSetWithValues(1, 2), "Earl's Court", Type.TUBE, Direction.OUT);
        Barrier currentBarrier = new Barrier(generateZoneSetWithValues(1, 2), "Earl's Court", Type.TUBE, Direction.IN);
        when(journeyRepository.getMostRecentTubeBarrierPassed(CARD_NUMBER)).thenReturn(previousBarrier);
        barrierService.attemptToPassBarrier(currentBarrier, card);

        verify(card, never()).addAmount(BarrierServiceImpl.MAX_COST);
    }

    @Test
    public void attemptToPassBarrier_shouldOnlyCharge180ForBusJourney() throws Exception {
        Barrier busBarrier = new Barrier(generateZoneSetWithValues(1), "Any", Type.BUS, Direction.IN);
        barrierService.attemptToPassBarrier(busBarrier, card);

        verify(card, never()).addAmount(anyDouble());
        verify(card).removeAmount(BarrierServiceImpl.BUS_COST);
    }

    @Test
    public void minZonesCrossed_shouldReturnOneZoneIfOneZoneCrossed() {
        int expectedZonesCrossed = 1;
        int actualZonesCrossed =
                barrierService.getMinZonesCrossed(generateZoneSetWithValues(2), generateZoneSetWithValues(2));

        assertEquals(expectedZonesCrossed, actualZonesCrossed);
    }

    @Test
    public void minZonesCrossed_shouldReturnMinimumZonesCrossedIfAmbiguous1() {
        int expectedZonesCrossed = 1;
        int actualZonesCrossed =
                barrierService.getMinZonesCrossed(generateZoneSetWithValues(1), generateZoneSetWithValues(1, 2));

        assertEquals(expectedZonesCrossed, actualZonesCrossed);
    }

    @Test
    public void minZonesCrossed_shouldReturnMinimumZonesCrossedIfAmbiguous2() {
        int expectedZonesCrossed = 1;
        int actualZonesCrossed =
                barrierService.getMinZonesCrossed(generateZoneSetWithValues(1, 2), generateZoneSetWithValues(2, 3));

        assertEquals(expectedZonesCrossed, actualZonesCrossed);
    }

    @Test
    public void mustHaveCrossedZoneOne_shouldReturnTrueIfOnlyInZoneOne() {
        boolean expectedCrossed = true;
        boolean actualCrossed = barrierService
                .mustHaveCrossedZoneOne(generateZoneSetWithValues(1), generateZoneSetWithValues(1), 1);

        assertEquals(expectedCrossed, actualCrossed);
    }

    @Test
    public void mustHaveCrossedZoneOne_shouldReturnFalseIfAmbiguous1() {
        boolean expectedCrossed = false;
        boolean actualCrossed = barrierService
                .mustHaveCrossedZoneOne(generateZoneSetWithValues(1, 2), generateZoneSetWithValues(2, 3), 2);

        assertEquals(expectedCrossed, actualCrossed);
    }

    @Test
    public void mustHaveCrossedZoneOne_shouldReturnFalseIfAmbiguous2() {
        boolean expectedCrossed = false;
        boolean actualCrossed = barrierService
                .mustHaveCrossedZoneOne(generateZoneSetWithValues(1, 2), generateZoneSetWithValues(2, 3), 1);

        assertEquals(expectedCrossed, actualCrossed);
    }

    private Set<Integer> generateZoneSetWithValues(Integer... values) {
        return new HashSet<>(Arrays.asList(values));
    }
}
