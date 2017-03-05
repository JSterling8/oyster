# Oyster
  
A simple Oyster (London public transport) implementation.
  
## Build & Run
Build with maven (`mvn clean package`). Then run with Java: `java -jar oyster-0.0.1-SNAPSHOT.jar`  
  
## Journey Costs
  
### Bus 
All bus journeys cost £1.80.  
  
### Tube
  
The maximum possible fare is charged (£3.20) upon entering a tube station. That fare is then refunded, and the actual amount calculated upon leaving a station. When the fare is ambiguous due to a station being in two zones, the cheapest possible fare is given to the user.
   
Stations and Zones:  
Holborn - 1   
Earl's Court - 1, 2   
Wimbledon - 3  
Hammersmith - 2    
  
Prices:    
Across only Zone 1: £2.50  
Across any single zone, not including Zone 1: £2.00  
Across two zones including Zone 1: £3.00   
Across two zones, not including Zone 1: £2.25  
Across any three zones: £3.20  
