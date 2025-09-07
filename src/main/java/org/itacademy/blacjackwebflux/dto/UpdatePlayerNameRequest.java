package org.itacademy.blacjackwebflux.dto;

public class UpdatePlayerNameRequest {
    private String newName;

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
}
