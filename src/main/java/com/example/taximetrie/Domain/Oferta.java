package com.example.taximetrie.Domain;

import java.util.Objects;

public class Oferta extends Entity<Long>{

    private Sofer sofer;
    private Persoana client;
    private int asteptare;

    public Oferta(Sofer sofer, Persoana client, int asteptare) {
        this.sofer = sofer;
        this.client = client;
        this.asteptare = asteptare;
    }

    public Sofer getSofer() {
        return sofer;
    }

    public void setSofer(Sofer sofer) {
        this.sofer = sofer;
    }

    public int getAsteptare() {
        return asteptare;
    }

    public void setAsteptare(int asteptare) {
        this.asteptare = asteptare;
    }

    public Persoana getClient() {
        return client;
    }

    public void setClient(Persoana client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Oferta oferta = (Oferta) o;
        return asteptare == oferta.asteptare && Objects.equals(sofer, oferta.sofer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), sofer, asteptare);
    }

    @Override
    public String
    toString() {
        return super.toString() + " " +
                "sofer=" + sofer +
                ", asteptare=" + asteptare;
    }
}
