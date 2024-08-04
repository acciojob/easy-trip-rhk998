package com.driver.services;

import com.driver.Repository.RepoLayer;
import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

@Service
public class ServicesLayer {

    @Autowired
    private RepoLayer repo;

    public String addPassngr(Passenger passenger){
        return repo.addPassngr(passenger);
    }

    public String addFlight(Flight flight){
        return repo.addFlight(flight);
    }

    public String addPort(Airport airport){
        return repo.addPort(airport);
    }

    public String getPort(){
        List<Airport> Airportlist = repo.getAllPorts();
        Airport largestAirport = null;

        for (Airport airport : Airportlist) {
            if (largestAirport == null ||
                    airport.getNoOfTerminals() > largestAirport.getNoOfTerminals() ||
                    (airport.getNoOfTerminals() == largestAirport.getNoOfTerminals() &&
                            airport.getAirportName().compareTo(largestAirport.getAirportName()) < 0)) {
                largestAirport = airport;
            }
        }

        return largestAirport != null ? largestAirport.getAirportName() : "No airports available";
    }

    public String getTakeOffCity(int flightId){
        List<Flight> flightlist = repo.getAllFlights();
        List<Airport> Airportlist = repo.getAllPorts();

        String takeOffAirport = "";
        City Default = City.DEF;
        for(Flight flight : flightlist){
            if(flight.getFlightId() == flightId){
                Default = flight.getFromCity();
            }
        }
        for(Airport airport : Airportlist){
            if(airport.getCity() == Default){
                takeOffAirport = airport.getAirportName();
            }
        }
        return takeOffAirport;
    }

    public double getShortestDuration(City fromCity, City toCity){
        List<Flight> flightlist = repo.getAllFlights();
        double durtn = Double.MAX_VALUE;
        boolean flightfound = false;
        for(Flight flight : flightlist){
            if ( (flight.getFromCity() == fromCity) && (flight.getToCity() == toCity) ){
                flightfound = true;
                durtn =  Math.min(durtn, flight.getDuration());
            }
        }
        if(!flightfound) return -1;
        return durtn;
    }

    public int getNumberOfPeopleOn(Date date, String airportName){
        List<Flight> flights = repo.getAllFlights();
        List<Airport> ports = repo.getAllPorts();
        City city = City.DEF;
        int passngrs = 0;
        for(Airport port: ports ){
            if(port.getAirportName().equals(airportName)){
                city = port.getCity();
            }
        }
        for(Flight flight : flights){
            if( flight.getFromCity() == city || flight.getToCity() == city ){
                if (date.equals(flight.getFlightDate())) {
                    passngrs += flight.getMaxCapacity();
                }
            }
        }
        return passngrs;
    }

    public int calculateFlightFare(int flightId){
        Flight flight = repo.getFlightById(flightId);
        if (flight == null) {
            return 0;
        }

        int filledSeats = repo.getPassengerList(flightId).size();
        return 3000 + filledSeats * 50;
    }

    public String bookTickets(int flightId, int passengerId) {
        Flight flight = repo.getFlightById(flightId);
        if (flight == null) {
            return "FAILURE";
        }

        List<Integer> bookedPassengers = repo.getPassengerList(flightId);
        if (bookedPassengers == null) {
            bookedPassengers = new ArrayList<>();
        }

        int ticketsAvailable = flight.getMaxCapacity();

        if (bookedPassengers.size() >= ticketsAvailable) {
            return "FAILURE";
        }

        Passenger passenger = repo.getPassengerById(passengerId);
        if (passenger == null) {
            return "FAILURE";
        }

        if (bookedPassengers.contains(passengerId)) {
            return "FAILURE";
        } else {
            bookedPassengers.add(passengerId);
            repo.bookTickets(flightId, passengerId);
        }

        return "SUCCESS";
    }

    public String cancelATicket(int flightId, int passengerId) {
        Flight flight = repo.getFlightById(flightId);
        if (flight == null) return "FAILURE";

        Passenger passenger = repo.getPassengerById(passengerId);
        if (passenger == null) return "FAILURE";

        List<Integer> bookedPassengers = repo.getPassengerList(flightId);
        if (bookedPassengers == null || !bookedPassengers.contains(passengerId)) {
            return "FAILURE";
        }

        repo.cancelTicket(flightId, passengerId);
        return "SUCCESS";
    }

    public int countOfBookingsDoneByPassenger(int passengerId) {
        List<Integer> bookedFlights = repo.getBookedFlightsByPassenger(passengerId);
        return bookedFlights != null ? bookedFlights.size() : 0;
    }

    public int calculateRevenueOfAFlight(int flightId){
        Flight flight = repo.getFlightById(flightId);
        if(flight==null){
            return -1;
        }

        List<Integer> bookedPassengers = repo.getPassengerList(flightId);
        int count = bookedPassengers != null ? bookedPassengers.size() : 0;

        return 3000 * count + (count * (count + 1) / 2) * 50;
    }
}
