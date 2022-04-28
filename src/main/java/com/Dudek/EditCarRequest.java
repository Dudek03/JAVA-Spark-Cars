package com.Dudek;

import java.util.UUID;

public class EditCarRequest {
    private UUID uuid;
    private String model;
    private int year;

    public EditCarRequest(UUID uuid, String model, int year) {
        this.uuid = uuid;
        this.model = model;
        this.year = year;
    }

    UUID getUUID() {
        return uuid;
    }

    String getModel() {
        return model;
    }

    int getYear() {
        return year;
    }
}
