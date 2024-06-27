package com.driver.model;

import java.time.LocalDate;

public class Flight {

    private int flightId; //This is a unique key for a flight

    private City fromCity;

    private City toCity;

    private int maxCapacity;

    private LocalDate flightDate;

    private double duration;
    private int ticketsBooked;

    private int ticketId;



    public Flight(int flightId, City fromCity, City toCity, int maxCapacity, int ticketsBooked, int ticketId, LocalDate flightDate, double duration) {
        this.flightId = flightId;
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.maxCapacity = maxCapacity;
        this.ticketsBooked = ticketsBooked;
        this.ticketId = ticketId;
        this.flightDate = flightDate;
        this.duration = duration;
    }

    public int getTicketsBooked() {
        return ticketsBooked;
    }

    public void setTicketsBooked(int ticketsBooked) {
        this.ticketsBooked = ticketsBooked;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    public City getFromCity() {
        return fromCity;
    }

    public void setFromCity(City fromCity) {
        this.fromCity = fromCity;
    }

    public City getToCity() {
        return toCity;
    }

    public void setToCity(City toCity) {
        this.toCity = toCity;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public LocalDate getFlightDate() {
        return flightDate;
    }

    public void setFlightDate(LocalDate flightDate) {
        this.flightDate = flightDate;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

}
