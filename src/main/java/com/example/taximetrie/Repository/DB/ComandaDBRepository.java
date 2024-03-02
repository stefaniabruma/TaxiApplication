package com.example.taximetrie.Repository.DB;

import com.example.taximetrie.Domain.Comanda;
import com.example.taximetrie.Domain.Persoana;
import com.example.taximetrie.Domain.Sofer;
import com.example.taximetrie.Repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ComandaDBRepository implements Repository<Long, Comanda> {

    protected String url;
    protected String username;
    protected  String password;

    public ComandaDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    @Override
    public Optional<Comanda> findOne(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Iterable<Comanda> findALL() {
        Set<Comanda> comenzi = new HashSet<>();
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select c.id, c.id_persoana, p.username, p.nume, c.id_sofer, ps.username as username_sofer, ps.nume as nume_sofer, s.indicativ_masina, c.data from comenzi c " +
                    "inner join persoane p on p.id = c.id_persoana " +
                    "inner join persoane ps on ps.id = c. id_sofer " +
                    "inner join soferi s on s.id = c.id_sofer ")) {

            ResultSet result = statement.executeQuery();

            while(result.next()){

                Long id = result.getLong("id");

                Long id_persoana = result.getLong("id_persoana");
                String user_persoana = result.getString("username");
                String nume_pers = result.getString("nume");

                Long id_sofer = result.getLong("id_sofer");
                String user_sofer = result.getString("username_sofer");
                String nume_sofer = result.getString("nume_sofer");
                String indm = result.getString("indicativ_masina");

                LocalDateTime data  = result.getTimestamp("data").toLocalDateTime();


                Persoana p = new Persoana(user_persoana,nume_pers);
                p.setId(id_persoana);

                Sofer s = new Sofer(user_sofer, nume_sofer, indm);
                s.setId(id_sofer);

                Comanda cmd = new Comanda(p, s, data);
                cmd.setId(id);

                comenzi.add(cmd);
            }

            return comenzi;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Comanda> save(Comanda entity) {
        if(entity == null)
            throw new IllegalArgumentException("Entity must not bee null!");

        try(

                Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = connection.prepareStatement("insert into " +
                        "comenzi(id_persoana, id_sofer, data) " +
                        "values(?, ?, ?)")

        ){

            statement.setLong(1, entity.getPersoana().getId());
            statement.setLong(2, entity.getTaximetrist().getId());
            statement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));

            return statement.executeUpdate() == 0 ? Optional.empty() : Optional.of(entity);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Comanda> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<Comanda> update(Comanda entity) {
        return Optional.empty();
    }
}
