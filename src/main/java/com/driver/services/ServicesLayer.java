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

        for (Airport airport : airports) {
            if (largestAirport == null ||
                    airport.getNoOfTerminals() > largestAirport.getNoOfTerminals() ||
                    (airport.getNoOfTerminals() == largestAirport.getNoOfTerminals() && airport.getAirportName().compareTo(largestAirport.getAirportName()) < 0)) {
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
                    passngrs += flight.getTicketsBooked();
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
            filledseats = flight.getTicketsBooked();
            if(flight.getFlightId() == flightId){
                presntFair = 3000 + filledseats*50;
            }

        }
        return presntFair;
    }
    //If the numberOfPassengers who have booked the flight is greater than : maxCapacity, in that case :
    //return a String "FAILURE"
    //Also if the passenger has already booked a flight then also return "FAILURE".

    public String bookTickets(int flightId, int passengerId){
        Flight flight = repo.getFlightById(flightId);
        if(flight==null){
            return "FAILURE";
        }
        int tickId = flight.getTicketId();
        int ticktsAvailable = flight.getTicketsBooked();
        int totatlTickts = flight.getMaxCapacity();
        if(totatlTickts - ticktsAvailable <=0){
            return "FAILURE";
        }
        Passenger passenger0 = repo.getPassengerById(passengerId);
        if(passenger0 == null){
            Passenger passenger = new Passenger();
            passenger.setPassengerId(passengerId);
            passenger.setTicketId(tickId);
            repo.updatePassenger(passenger);
        }else{
            if(passenger0.getTicketId() == -1){
                System.out.println("passenger renewed");
                passenger0.setTicketId(tickId);
            }else if(passenger0.getTicketId() !=-1){
                return "FAILURE";
            }
        }
        Passenger passengertest = repo.getPassengerById(passengerId);
        System.out.println(passengertest.getTicketId() + " " + passengertest.getPassengerId());
        return "SUCCESS";
    }

    public String cancelATicket(int flightId, int passengerId){
        Flight flight = repo.getFlightById(flightId);
        if(flight==null) return "FAILURE";
        Passenger passenger = repo.getPassengerById(passengerId);
        if(passenger == null || passenger.getTicketId() == -1) return "FAILURE";

        passenger.setTicketId(-1);
        System.out.println(passenger.getTicketId());
        return "SUCCESS";
    }

    public int countOfBookingsDoneByPassenger(int passengerId){
        int bookings = 0;
        Passenger passenger = repo.getPassengerById(passengerId);
        if(passenger == null) return -1;
            if(passenger.getPassengerId() == passengerId){
                bookings = passenger.getTicktsBooked();
            }
          System.out.println(bookings);
        return bookings;
    }

    public int calculateRevenueOfAFlight(int flightId){
        Flight flight = repo.getFlightById(flightId);
        if(flight==null){
            return -1;
        }
        int count = 0; int revenue = 0;

        count = flight.getTicketsBooked();

        revenue = 3000 * count;
        return revenue;
    }
}
