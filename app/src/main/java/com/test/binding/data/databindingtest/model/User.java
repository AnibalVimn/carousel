package com.test.binding.data.databindingtest.model;

public class User {

    private String firstName;
    private String lastName;
    private boolean isAdult;

    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.isAdult = true;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public boolean getIsAdult() {
        return this.isAdult;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setIsAdult(boolean isAdult) {
        this.isAdult = isAdult;
    }
}
