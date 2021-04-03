package com.martijn.zentjens.mbdand_assessment.models;

public class Contact {
    private final String _id;
    private final String _name;

    public Contact(String id, String name) {
        this._name = name;
        this._id = id;
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
}
