package com.driver.controllers;


import com.driver.model.Airport;
import com.driver.model.City;
import com.driver.model.Flight;
import com.driver.model.Passenger;
import com.driver.services.ServicesLayer;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;


import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Date;

@RestController
public class AirportController {

    ServicesLayer servicesLayer = new ServicesLayer();
//private final ServicesLayer servicesLayer;
//
//    @Autowired
//    public AirportController(ServicesLayer servicesLayer) {
//        this.servicesLayer = servicesLayer;
//    }
    @PostMapping("/add_airport")
    public String addAirport(@RequestBody Airport airport){

        //Simply add airport details to your database
        //Return a String message "SUCCESS"
        String response = servicesLayer.addPort(airport);
        return response;
    }

    @GetMapping("/get-largest-aiport")
    public String getLargestAirportName(){

        //Largest airport is in terms of terminals. 3 terminal airport is larger than 2 terminal airport
        //Incase of a tie return the Lexicographically smallest airportName

        String response = servicesLayer.getPort();
       return response;
    }

    @GetMapping("/get-shortest-time-travel-between-cities")
    public double getShortestDurationOfPossibleBetweenTwoCities(@RequestParam("fromCity") City fromCity, @RequestParam("toCity")City toCity){

        //Find the duration by finding the shortest flight that connects these 2 cities directly
        //If there is no direct flight between 2 cities return -1.
       double duration = servicesLayer.getShortestDuration(fromCity, toCity);
       return duration;
    }

    @GetMapping("/get-number-of-people-on-airport-on/{date}")
    public int getNumberOfPeopleOn(@PathVariable("date") Date date,@RequestParam("airportName")String airportName){

        //Calculate the total number of people who have flights on that day on a particular airport
        //This includes both the people who have come for a flight and who have landed on an airport after their flight
        int pssngrs = servicesLayer.getNumberOfPeopleOn(date,airportName);
        return pssngrs;
    }


    @GetMapping("/calculate-fare")
    public int calculateFlightFare(@RequestParam("flightId")Integer flightId){

        //Calculation of flight prices is a function of number of people who have booked the flight already.
        //Price for any flight will be : 3000 + noOfPeopleWhoHaveAlreadyBooked*50
        //Suppose if 2 people have booked the flight already : the price of flight for the third person will be 3000 + 2*50 = 3100
        //This will not include the current person who is trying to book, he might also be just checking price
        int cost = 0;
        cost = servicesLayer.calculateFlightFare(flightId);
       return cost;

    }


    @PostMapping("/book-a-ticket")
    public String bookATicket(@RequestParam("flightId")Integer flightId,@RequestParam("passengerId")Integer passengerId){

        //If the numberOfPassengers who have booked the flight is greater than : maxCapacity, in that case :
        //return a String "FAILURE"
        //Also if the passenger has already booked a flight then also return "FAILURE".
        //else if you are able to book a ticket then return "SUCCESS"
        String response = servicesLayer.bookTickets(flightId,passengerId);
        return response;
    }

    @PutMapping("/cancel-a-ticket")
    public String cancelATicket(@RequestParam("flightId")Integer flightId,@RequestParam("passengerId")Integer passengerId){

        //If the passenger has not booked a ticket for that flight or the flightId is invalid or in any other failure case
        // then return a "FAILURE" message
        // Otherwise return a "SUCCESS" message
        // and also cancel the ticket that passenger had booked earlier on the given flightId
        String response = servicesLayer.cancelATicket(flightId,passengerId);
        return response;
    }


    @GetMapping("/get-count-of-bookings-done-by-a-passenger/{passengerId}")
    public int countOfBookingsDoneByPassengerAllCombined(@PathVariable("passengerId")Integer passengerId){

        //Tell the count of flight bookings done by a passenger: This will tell the total count of flight bookings done by a passenger :
        int result = servicesLayer.countOfBookingsDoneByPassenger(passengerId);
       return result;
    }

    @PostMapping("/add-flight")
    public String addFlight(@RequestBody Flight flight){

        String response = servicesLayer.addFlight(flight);
        //Return a "SUCCESS" message string after adding a flight.
       return response;
    }


    @GetMapping("/get-airportName-from-flight-takeoff/{flightId}")
    public String getAirportNameFromFlightId(@PathVariable("flightId")Integer flightId){

        //We need to get the starting airportName from where the flight will be taking off (Hint think of City variable if that can be of some use)
        //return null incase the flightId is invalid or you are not able to find the airportName

        String airportName = servicesLayer.getTakeOffCity(flightId);
        return airportName;
    }


    @GetMapping("/calculate-revenue-collected/{flightId}")
    public int calculateRevenueOfAFlight(@PathVariable("flightId")Integer flightId){

        //Calculate the total revenue that a flight could have
        //That is of all the passengers that have booked a flight till now and then calculate the revenue
        //Revenue will also decrease if some passenger cancels the flight

        int result = servicesLayer.calculateRevenueOfAFlight(flightId);

        return result;
    }


    @PostMapping("/add-passenger")
    public String addPassenger(@RequestBody Passenger passenger){

        //Add a passenger to the database
        //And return a "SUCCESS" message if the passenger has been added successfully.
        String response = servicesLayer.addPassngr(passenger);

       return response;
    }


}
