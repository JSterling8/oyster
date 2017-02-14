package com.jms.oyster;

import com.jms.oyster.exception.IllegalParameterException;
import com.jms.oyster.exception.InsufficientCardBalanceException;
import com.jms.oyster.model.Barrier;
import com.jms.oyster.model.Card;
import com.jms.oyster.service.BarrierService;
import com.jms.oyster.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.jms.oyster.model.Barrier.Direction;
import static com.jms.oyster.model.Barrier.Type;

@Component
public class Runner implements CommandLineRunner {

    private final CardService cardService;
    private final BarrierService barrierService;
    private List<Barrier> mockBarriers = new ArrayList<>();

    @Autowired
    public Runner(CardService cardService, BarrierService barrierService) {
        this.cardService = cardService;
        this.barrierService = barrierService;

        mockBarriers.add(new Barrier(generateZoneSetWithValues(1), "Holborn", Type.TUBE, Direction.IN));
        mockBarriers.add(new Barrier(generateZoneSetWithValues(1), "Holborn", Type.TUBE, Direction.OUT));

        mockBarriers.add(new Barrier(generateZoneSetWithValues(1, 2), "Earl's Court", Type.TUBE, Direction.IN));
        mockBarriers.add(new Barrier(generateZoneSetWithValues(1, 2), "Earl's Court", Type.TUBE, Direction.OUT));

        mockBarriers.add(new Barrier(generateZoneSetWithValues(3), "Wimbledon", Type.TUBE, Direction.IN));
        mockBarriers.add(new Barrier(generateZoneSetWithValues(3), "Wimbledon", Type.TUBE, Direction.OUT));

        mockBarriers.add(new Barrier(generateZoneSetWithValues(2), "Hammersmith", Type.TUBE, Direction.IN));
        mockBarriers.add(new Barrier(generateZoneSetWithValues(2), "Hammersmith", Type.TUBE, Direction.OUT));
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("To generate an Oyster card, please type your name, then press <enter>:");
        String name = scanner.nextLine();

        Card card = cardService.createCard(name);

        System.out.println("Great. We've made you an Oyster card, but there's no money on it. Please enter the amount " +
                "of money you'd like to add to the card then press <enter>:");

        Double amount = -1D;

        while(amount < 0D) {
            try {
                amount = scanner.nextDouble();

                break;
            } catch (NumberFormatException e) {
                // Suppress...
            }

            System.out.println("Please enter the amount of money you'd like to add to the card then press <enter> " +
                    "(can't be negative or non-numeric):");
        }

        card.addAmount(amount);

        boolean userWantsToTravel = true;

        while(userWantsToTravel) {
            System.out.println("Your card balance is £" + card.getBalance() + ". Would you like to start/finish a " +
                    "(1) bus or (2) tube journey?");
            int journeyType = scanner.nextInt();

            if (journeyType == 1) {
                // Since zones don't matter with our implementation, we don't need to ask the user to specify them...
                Barrier busBarrier = new Barrier(generateZoneSetWithValues(1),
                        "Generic Bus",
                        Barrier.Type.BUS,
                        Barrier.Direction.IN);

                boolean success = attemptToPassBarrier(card, busBarrier);

                if(success) {
                    System.out.println("Congrats, you made it. Your card balance is now £" + card.getBalance() + ". " +
                            "You can get off the bus whenever you want, and you don't need to swipe out.");
                } else {
                    System.out.println("Whoops, you didn't have enough money. Goodbye.");

                    return;
                }
            } else {
                System.out.println("Select the barrier you'd like to try your card on:");

                for(int i = 0; i < mockBarriers.size(); i++) {
                    System.out.println("(" + i + ") " + mockBarriers.get(i));
                }

                int barrierNum = scanner.nextInt();

                boolean success;
                try {
                    success = attemptToPassBarrier(card, mockBarriers.get(barrierNum));
                } catch (IllegalParameterException e) {
                    System.out.println(e.getMessage());
                    continue;
                }

                if(success) {
                    System.out.println("Congrats, you made it through the barrier. " +
                            "Your card balance is now £" + card.getBalance() + "");
                } else {
                    System.out.println("Whoops, you didn't have enough money. Goodbye.");

                    return;
                }
            }

            System.out.println("Would you like to pass another barrier? (1) Yes. (2) No.");
            int again = scanner.nextInt();

            if(again != 1) {
                userWantsToTravel = false;
            }
        }
    }

    private boolean attemptToPassBarrier(Card card, Barrier barrier) throws IllegalParameterException {
        boolean wasSuccessful = true;

        try {
            barrierService.attemptToPassBarrier(barrier, card);
        } catch (InsufficientCardBalanceException e) {
            wasSuccessful = false;
        }

        return wasSuccessful;
    }

    private Set<Integer> generateZoneSetWithValues(Integer... values) {
        return new HashSet<>(Arrays.asList(values));
    }
}