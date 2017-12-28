package com.example.naveed.contentprovider;

/**
 * Created by Naveed on 12/28/2017.
 */

public class Contact {

    @SerializedName("name")
    String name;

    @SerializedName("number")
    String number;

    public Contact(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
