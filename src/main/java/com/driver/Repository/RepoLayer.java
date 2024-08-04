package com.driver.Repository;

import com.driver.model.Airport;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

@Repository
public class RepoLayer {
    HashMap<Integer,Passenger> passngrDb = new HashMap<>();
    public String addPassngr(Passenger passenger){
        int passngrID = passenger.getPassengerId();
        passngrDb.put(passngrID,passenger);
        return "SUCCESS";
    }

    HashMap<Integer, Flight> flightDb = new HashMap<>();
    public String addFlight(Flight flight){
        int flightID = flight.getFlightId();
        flightDb.put(flightID,flight);
        return "SUCCESS";
    }

    HashMap<String, Airport> airportDb = new HashMap<>();
    public String addPort(Airport airport){
        String key = airport.getAirportName();
        airportDb.put(key,airport);
        return "SUCCESS";

    }

    public List<Airport> getAllPorts(){
        List<Airport> portlist = new ArrayList<>();
        for(Airport airport : airportDb.values()){
            portlist.add(airport);
        }
        return portlist;
    }
    public List<Flight> getAllFlights(){
        List<Flight> flightlist = new ArrayList<>();
        for(Flight flight : flightDb.values()){
            flightlist.add(flight);
        }
        return flightlist;
    }
    public Flight getFlightById(int flightId) {
        return flightDb.get(flightId);
    }
    public List<Passenger> getAllPassengers(){
        List<Passenger> passengerList = new ArrayList<>();
        for(Passenger passenger : passngrDb.values()){
            passengerList.add(passenger);
        }
        return passengerList;
    }

    public void updatePassenger(Passenger passenger) {
        passngrDb.put(passenger.getPassengerId(), passenger);
    }
    public Passenger getPassengerById(int passengerId) {
        return passngrDb.get(passengerId);
    }

    HashMap<Integer, List<Integer>> ticketList = new HashMap<>();
    public void bookTickets(int flightId, int passengerId){
        List<Integer> passengers = new ArrayList<>();
        passengers.add(passengerId);
        ticketList.put(flightId, passengers);
    }
    public List<Integer> getPassengerList(int flightId){
        return ticketList.get(flightId);
    }

    public void cancelTicket(int flightId, int passengerId){
        for(int flight : ticketList.keySet()){
            if(flight==flightId){
                List<Integer> passengers = ticketList.get(flight);
                passengers.remove(passengerId);
                ticketList.put(flight,passengers);
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
