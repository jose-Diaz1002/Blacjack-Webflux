package org.itacademy.blacjackwebflux.dto;

import jakarta.validation.constraints.NotBlank;


public class UpdatePlayerNameRequest {
    @NotBlank(message = "El nuevo nombre no puede estar vacío.")
    private String newName;

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
}
