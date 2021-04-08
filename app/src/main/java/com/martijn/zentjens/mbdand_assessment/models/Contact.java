package com.martijn.zentjens.mbdand_assessment.models;

public class Contact {
    private final String _id;
    private String _name;
    private String _phoneNumber;

    public Contact(String id) {
        this._id = id;
    }

    public void setName(String name) {
        _name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        _phoneNumber = phoneNumber;
    }

    public String getName() {
        return _name;
    }

    public String getId() {
        return _id;
    }

    public String getFirstLetter() {
        return _name.substring(0, 1);
    }

    public String getPhonenumber() {
        return _phoneNumber.length() > 0 ? _phoneNumber : "0617337565";
    }
}
