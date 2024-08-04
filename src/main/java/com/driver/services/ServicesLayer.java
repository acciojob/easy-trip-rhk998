package com.driver.services;

import com.driver.Repository.RepoLayer;
import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;

import java.util.Date;
import java.util.List;

public class ServicesLayer {

    private final RepoLayer repo;

    public ServicesLayer() {
        this.repo = new RepoLayer(); // Manual instantiation
    }

    public String addPassngr(Passenger passenger) {
        return repo.addPassngr(passenger);
    }

    public String addFlight(Flight flight) {
        return repo.addFlight(flight);
    }

    public String addPort(Airport airport) {
        return repo.addPort(airport);
    }

    public String getPort() {
        List<Airport> airportList = repo.getAllPorts();
        Airport largestAirport = null;

        for (Airport airport : airportList) {
            if (largestAirport == null ||
                    airport.getNoOfTerminals() > largestAirport.getNoOfTerminals() ||
                    (airport.getNoOfTerminals() == largestAirport.getNoOfTerminals() &&
                            airport.getAirportName().compareTo(largestAirport.getAirportName()) < 0)) {
                largestAirport = airport;
            }
        }

        return largestAirport != null ? largestAirport.getAirportName() : "No airports available";
    }

    public String getTakeOffCity(int flightId) {
        Flight flight = repo.getFlightById(flightId);
        if (flight == null) return "Flight not found";

        City fromCity = flight.getFromCity();
        for (Airport airport : repo.getAllPorts()) {
            if (airport.getCity() == fromCity) {
                return airport.getAirportName();
            }
        }
        return "Airport not found";
    }

    public double getShortestDuration(City fromCity, City toCity) {
        double duration = Double.MAX_VALUE;
        boolean flightFound = false;
        for (Flight flight : repo.getAllFlights()) {
            if (flight.getFromCity() == fromCity && flight.getToCity() == toCity) {
                flightFound = true;
                duration = Math.min(duration, flight.getDuration());
            }
        }
        return flightFound ? duration : -1;
    }

    public int getNumberOfPeopleOn(Date date, String airportName) {
        City city = null;
        for (Airport port : repo.getAllPorts()) {
            if (port.getAirportName().equals(airportName)) {
                city = port.getCity();
                break;
            }
        }

        if (city == null) return 0;

        int passengers = 0;
        for (Flight flight : repo.getAllFlights()) {
            if ((flight.getFromCity() == city || flight.getToCity() == city) && date.equals(flight.getFlightDate())) {
                passengers += repo.getPassengerList(flight.getFlightId()).size();
            }
        }
        return passengers;
    }

    public int calculateFlightFare(int flightId) {
        List<Integer> bookedPassengers = repo.getPassengerList(flightId);
        if (bookedPassengers == null) return 0;
        int filledSeats = bookedPassengers.size();
        return 3000 + filledSeats * 50;
    }

    public String bookTickets(int flightId, int passengerId) {
        Flight flight = repo.getFlightById(flightId);
        if (flight == null) {
            return "FAILURE";
        }

        int ticketsAvailable = flight.getMaxCapacity();
        List<Integer> bookedPassengers = repo.getPassengerList(flightId);

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

    public int calculateRevenueOfAFlight(int flightId) {
        Flight flight = repo.getFlightById(flightId);
        if (flight == null) {
            return -1;
        }

        List<Integer> bookedPassengers = repo.getPassengerList(flightId);
        int count = bookedPassengers.size();
        int revenue = 3000 + (count - 1) * 50;
        return revenue;
    }
}
