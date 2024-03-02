package com.example.taximetrie.Domain;

import java.util.Objects;

public class Cerere extends Entity<Long>{

    private Persoana client;
    private String locatie;

    public Cerere(Persoana client, String locatie) {
        this.client = client;
        this.locatie = locatie;
    }

    public Persoana getClient() {
        return client;
    }

    public void setClient(Persoana client) {
        this.client = client;
    }

    public String getLocatie() {
        return locatie;
    }

    public void setLocatie(String locatie) {
        this.locatie = locatie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Cerere cerere = (Cerere) o;
        return Objects.equals(client, cerere.client) && Objects.equals(locatie, cerere.locatie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), client, locatie);
    }

    @Override
    public String toString() {
        return super.toString() +
                "client=" + client +
                ", locatie='" + locatie;
    }
}
