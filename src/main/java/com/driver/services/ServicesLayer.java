package com.driver.services;

import com.driver.Repository.RepoLayer;
import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Date;

@Service
public class ServicesLayer {

    RepoLayer repo = new RepoLayer();

    public String addPassngr(Passenger passenger){
        String response = repo.addPassngr(passenger);
        return response;
    }

    public String addFlight(Flight flight){
        String response = repo.addFlight(flight);
        return response;
    }

    public String addPort(Airport airport){
        String response = repo.addPort(airport);
        return response;
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
        City Default = City.DEF ;
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
        System.out.println(city);

        for(Flight flight : flights){
            if( flight.getFromCity() == city || flight.getToCity() == city ){
                if (date.equals(flight.getFlightDate())) {
                    System.out.println("Matched Date");
                    passngrs += flight.getMaxCapacity();
                }
            }
        }
        return passngrs;
    }

    public int calculateFlightFare(int flightId){
        List<Flight> flights = repo.getAllFlights();

        int presntFair = 0;
        int filledseats = 0;
        for(Flight flight : flights){
            filledseats = flight.getMaxCapacity();
            if(flight.getFlightId() == flightId){
                presntFair = 3000 + filledseats*50;
            }

        }
        return presntFair;
    }
    //If the numberOfPassengers who have booked the flight is greater than : maxCapacity, in that case :
    //return a String "FAILURE"
    //Also if the passenger has already booked a flight then also return "FAILURE".

    public String bookTickets(int flightId, int passengerId) {
        Flight flight = repo.getFlightById(flightId);
        if (flight == null) {
            return "FAILURE";
        }

        int ticketsAvailable = flight.getMaxCapacity();
        List<Integer> bookedPassengers = repo.getPassengerList(flightId);

        // Check available tickets
        if (bookedPassengers.size() >= ticketsAvailable) {
            return "FAILURE";
        }

        Passenger passenger = repo.getPassengerById(passengerId);
        if (passenger == null) {
            return "FAILURE";
        }

        // Check if the passenger has already booked the ticket
        if (bookedPassengers.contains(passengerId)) {
            return "FAILURE";
        } else {
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
        if (!bookedPassengers.contains(passengerId)) {
            return "FAILURE";
        }

        repo.cancelTicket(flightId, passengerId);
        return "SUCCESS";
    }

    public int countOfBookingsDoneByPassenger(int passengerId) {
        List<Integer> bookedFlights = repo.getBookedFlightsByPassenger(passengerId);
        return bookedFlights.size();
    }

    public int calculateRevenueOfAFlight(int flightId){
        Flight flight = repo.getFlightById(flightId);
        if(flight==null){
            return -1;
        }
        int count = 0; int revenue = 0;

        count = flight.getMaxCapacity();

        revenue = 3000 * count;
        return revenue;
    }
}
