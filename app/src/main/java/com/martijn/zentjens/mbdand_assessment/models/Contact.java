package com.martijn.zentjens.mbdand_assessment.models;

public class Contact {
    private final String name;

    private final String phonenumber;

    public Contact(String name, String phonenumber) {
        if(phonenumber == null){
            this.phonenumber = "0642406526";
        }
        else{
            this.phonenumber = phonenumber;
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getPhonenumber() {return phonenumber; }
}
