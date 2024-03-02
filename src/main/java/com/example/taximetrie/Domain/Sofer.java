package com.example.taximetrie.Domain;

import java.util.Objects;

public class Sofer extends Persoana{

    private String indicativMasina;

    public Sofer(String username, String nume, String indicativMasina) {
        super(username, nume);
        this.indicativMasina = indicativMasina;
    }

    public String getIndicativMasina() {
        return indicativMasina;
    }

    public void setIndicativMasina(String indicativMasina) {
        this.indicativMasina = indicativMasina;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Sofer sofer = (Sofer) o;
        return Objects.equals(indicativMasina, sofer.indicativMasina);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), indicativMasina);
    }

    @Override
    public String toString() {
        return super.toString() + ' ' +
                "indicativMasina='" + indicativMasina + '\'';
    }
}
