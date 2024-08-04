package com.driver.Repository;

import com.driver.model.Airport;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RepoLayer {
    private final HashMap<Integer, Passenger> passngrDb = new HashMap<>();
    private final HashMap<Integer, Flight> flightDb = new HashMap<>();
    private final HashMap<String, Airport> airportDb = new HashMap<>();
    private final HashMap<Integer, List<Integer>> ticketList = new HashMap<>();

    public String addPassngr(Passenger passenger) {
        int passngrID = passenger.getPassengerId();
        passngrDb.put(passngrID, passenger);
        return "SUCCESS";
    }

    public String addFlight(Flight flight) {
        int flightID = flight.getFlightId();
        flightDb.put(flightID, flight);
        return "SUCCESS";
    }

    public String addPort(Airport airport) {
        String key = airport.getAirportName();
        airportDb.put(key, airport);
        return "SUCCESS";
    }

    public List<Airport> getAllPorts() {
        return new ArrayList<>(airportDb.values());
    }

    public List<Flight> getAllFlights() {
        return new ArrayList<>(flightDb.values());
    }

    public Flight getFlightById(int flightId) {
        return flightDb.get(flightId);
    }

    public List<Passenger> getAllPassengers() {
        return new ArrayList<>(passngrDb.values());
    }

    public void updatePassenger(Passenger passenger) {
        passngrDb.put(passenger.getPassengerId(), passenger);
    }

    public Passenger getPassengerById(int passengerId) {
        return passngrDb.get(passengerId);
    }

    public void bookTickets(int flightId, int passengerId) {
        List<Integer> passengers = ticketList.getOrDefault(flightId, new ArrayList<>());
        passengers.add(passengerId);
        ticketList.put(flightId, passengers);
    }

    public List<Integer> getPassengerList(int flightId) {
        return ticketList.getOrDefault(flightId, new ArrayList<>());
    }

    public void cancelTicket(int flightId, int passengerId) {
        List<Integer> passengers = ticketList.get(flightId);
        if (passengers != null) {
            passengers.remove(Integer.valueOf(passengerId));
            if (passengers.isEmpty()) {
                ticketList.remove(flightId);
            } else {
                ticketList.put(flightId, passengers);
            }
        }
    }

    public List<Integer> getBookedFlightsByPassenger(int passengerId) {
        List<Integer> bookedFlights = new ArrayList<>();
        for (Map.Entry<Integer, List<Integer>> entry : ticketList.entrySet()) {
            if (entry.getValue().contains(passengerId)) {
                bookedFlights.add(entry.getKey());
            }
        }
        return bookedFlights;
    }
}
