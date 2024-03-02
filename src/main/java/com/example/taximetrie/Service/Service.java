package com.example.taximetrie.Service;

import com.example.taximetrie.Domain.*;
import com.example.taximetrie.Repository.Paging.ComandaPagingDBRepository;
import com.example.taximetrie.Repository.PagingUtils.PagingInformation;
import com.example.taximetrie.Repository.Repository;
import com.example.taximetrie.Utils.Observable;
import com.example.taximetrie.Utils.Observer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Service implements Observable {

    List<Observer> obs = new ArrayList<>();
    Repository<Long, Persoana> repo_persoane;
    Repository<Long, Sofer> repo_soferi;
    ComandaPagingDBRepository repo_comenzi;
    Repository<Long, Cerere> repo_cereri;
    Repository<Long, Oferta> repo_oferte;

    public Service(Repository<Long, Persoana> repo_persoane, Repository<Long, Sofer> repo_soferi, ComandaPagingDBRepository repo_comenzi, Repository<Long, Cerere> repo_cereri, Repository<Long, Oferta> repo_oferte) {
        this.repo_persoane = repo_persoane;
        this.repo_soferi = repo_soferi;
        this.repo_comenzi = repo_comenzi;
        this.repo_cereri = repo_cereri;
        this.repo_oferte = repo_oferte;
    }

    public List<Persoana> listaClientilor(PagingInformation pi, Sofer sofer){
        Comanda fake = new Comanda(null, sofer, null);

        return repo_comenzi.findAll(pi, fake).getContent()
                .map(Comanda::getPersoana)
                .distinct()
                .toList();
    }

    public Optional<Persoana> cautaPersoanaDupaUsername(String username){

        return StreamSupport.stream(repo_persoane.findALL().spliterator(), false)
                .filter(pers -> Objects.equals(pers.getUsername(), username))
                .findFirst();
    }

    public Optional<Sofer> cautaSoferDupaId(Long id){

        return StreamSupport.stream(repo_soferi.findALL().spliterator(), false)
                .filter(sofer -> Objects.equals(sofer.getId(), id))
                .findFirst();
    }

    public List<Comanda> comenziSoferDatDataData(Sofer sofer, LocalDate date){
        return StreamSupport.stream(repo_comenzi.findALL().spliterator(), false)
                .filter(cmd -> Objects.equals(cmd.getTaximetrist(), sofer) && cmd.getData().getDayOfYear() == date.getDayOfYear())
                .collect(Collectors.toList());
    }

    public float media(Sofer sofer){
        return (float) StreamSupport.stream(repo_comenzi.findALL().spliterator(), false)
                .filter(cmd -> cmd.getTaximetrist().equals(sofer) && cmd.getData().getYear() == LocalDate.now().getYear() && cmd.getData().getDayOfYear() >= LocalDate.now().getDayOfYear() - 89)
                .count() / 90;
    }

    public Persoana celMaiFidel(Sofer sofer){
        var ok =  StreamSupport.stream(repo_comenzi.findALL().spliterator(), false)
                .filter(cmd -> cmd.getTaximetrist().equals(sofer))
                .collect(Collectors.groupingBy(Comanda::getPersoana, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue());

        return ok.map(Map.Entry::getKey).orElse(null);
    }

    public void adaugaCerere(Persoana pers, String locatie){

        repo_cereri.save(new Cerere(pers, locatie));
        notifyObservers();
    }

    public Iterable<Cerere> getCereri(){
        return repo_cereri.findALL();
    }

    public void adaugaOferta(Sofer sofer, Persoana client, int asteptare){
        repo_oferte.save(new Oferta(sofer, client, asteptare));
        notifyObservers();
    }

    public void stergeCerere(Persoana client){
        repo_cereri.delete(client.getId());
        notifyObservers();
    }

    public Optional<Oferta> getOferta(Persoana client){

        return repo_oferte.findOne(client.getId());

    }

    public void stergeOferta(Persoana client){
        repo_oferte.delete(client.getId());
        notifyObservers();
    }

    public void adaugaComanda(Persoana client, Sofer sofer){
        repo_comenzi.save(new Comanda(client, sofer, LocalDateTime.now()));
        notifyObservers();
    }
    @Override
    public void addObserver(Observer o) {
        obs.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        obs.remove(o);
    }

    @Override
    public void notifyObservers() {
        obs.forEach(Observer::update);
    }
}
