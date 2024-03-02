package com.example.taximetrie.Domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Comanda extends Entity<Long>{

    Persoana persoana;

    Sofer taximetrist;

    LocalDateTime data;

    public Comanda(Persoana persoana, Sofer taximetrist, LocalDateTime data) {
        this.persoana = persoana;
        this.taximetrist = taximetrist;
        this.data = data;
    }

    public Persoana getPersoana() {
        return persoana;
    }

    public void setPersoana(Persoana persoana) {
        this.persoana = persoana;
    }

    public Sofer getTaximetrist() {
        return taximetrist;
    }

    public void setTaximetrist(Sofer taximetrist) {
        this.taximetrist = taximetrist;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Comanda comanda = (Comanda) o;
        return Objects.equals(persoana, comanda.persoana) && Objects.equals(taximetrist, comanda.taximetrist) && Objects.equals(data, comanda.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), persoana, taximetrist, data);
    }

    @Override
    public String toString() {
        return super.toString() + " " +
                "persoana=" + persoana +
                ", taximetrist=" + taximetrist +
                ", data=" + data;
    }
}
