package com.Dudek;

public class Airbag {

    private String description;
    private boolean value;

    public Airbag(String description, boolean isSet) {
        this.description = description;
        this.value = isSet;
    }

    public String getDescription() { return description; }

    public boolean isValue() { return value; }

    @Override
    public String toString() {
        return "Airbag{" +
                "description='" + description + '\'' +
                ", value=" + value +
                '}';
    }


}
