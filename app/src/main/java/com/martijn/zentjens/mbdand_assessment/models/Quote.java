package com.martijn.zentjens.mbdand_assessment.models;

public class Quote {
    private final String _tag;
    private final String _message;

    public Quote(String tag, String message) {
        this._message = message;
        this._tag = tag;
    }

    public String getMessage() {
        return _message;
    }

    public String getTag() {
        return _tag;
    }
}
