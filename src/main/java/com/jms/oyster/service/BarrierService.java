package com.jms.oyster.service;

import com.jms.oyster.exception.IllegalParameterException;
import com.jms.oyster.exception.InsufficientCardBalanceException;
import com.jms.oyster.model.Barrier;
import com.jms.oyster.model.Card;

import java.util.Set;

public interface BarrierService {
    Card attemptToPassBarrier(Barrier barrier, Card card)
            throws InsufficientCardBalanceException, IllegalParameterException;
    int getMinZonesCrossed(Set<Integer> from, Set<Integer> to);
    boolean mustHaveCrossedZoneOne(Set<Integer> from, Set<Integer> to, int minZonesCrossed);
}
