package com.example.taximetrie.Domain;

import java.util.Objects;

public class Persoana extends Entity<Long>{

    protected String username;

    protected String nume;

    public Persoana(String username, String nume) {
        this.username = username;
        this.nume = nume;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Persoana persoana = (Persoana) o;
        return Objects.equals(username, persoana.username) && Objects.equals(nume, persoana.nume);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), username, nume);
    }

    @Override
    public String toString() {
        return super.toString() + ' ' +
                "username='" + username + '\'' +
                ", nume='" + nume + '\'';
    }
}
