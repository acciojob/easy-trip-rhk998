package com.driver.model;

public class Passenger {

    private int passengerId; //This is a unique key for Passenger model :

    private int ticketId;

    private int ticktsBooked;
    private String email;

    private String name;

    private int age;



    public Passenger(int passengerId,int ticketId, int ticktsBooked, String email, String name, int age) {
        this.passengerId = passengerId;
        this.ticketId = ticketId;
        this.ticktsBooked = ticktsBooked;
        this.email = email;
        this.name = name;
        this.age = age;
    }

    public int getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(int passengerId) {
        this.passengerId = passengerId;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public int getTicktsBooked() {
        return ticktsBooked;
    }

    public void setTicktsBooked(int ticktsBooked) {
        this.ticktsBooked = ticktsBooked;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
