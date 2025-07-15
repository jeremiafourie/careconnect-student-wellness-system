package com.bc.shared.dto;

import java.util.Objects;

public class UserLoginDto {
    private String identifier;
    private String password;
    
    public UserLoginDto() {
    }
    
    public UserLoginDto(String identifier, String password) {
        this.identifier = identifier;
        this.password = password;
    }
    
    public String getIdentifier() {
        return identifier;
    }
    
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserLoginDto that = (UserLoginDto) o;
        return Objects.equals(identifier, that.identifier);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(identifier);
    }
    
    @Override
    public String toString() {
        return "UserLoginDto{" +
               "identifier='" + identifier + '\'' +
               '}';
    }
}