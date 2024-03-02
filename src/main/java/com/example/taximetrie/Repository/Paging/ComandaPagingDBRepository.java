package com.example.taximetrie.Repository.Paging;

import com.example.taximetrie.Domain.Comanda;
import com.example.taximetrie.Domain.Persoana;
import com.example.taximetrie.Domain.Sofer;
import com.example.taximetrie.Repository.DB.ComandaDBRepository;
import com.example.taximetrie.Repository.PagingRepository;
import com.example.taximetrie.Repository.PagingUtils.Page;
import com.example.taximetrie.Repository.PagingUtils.PageObject;
import com.example.taximetrie.Repository.PagingUtils.PagingInformation;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ComandaPagingDBRepository extends ComandaDBRepository implements PagingRepository<Long, Comanda> {
    public ComandaPagingDBRepository(String url, String username, String password) {
        super(url, username, password);
    }

    @Override
    public Page<Comanda> findAll(PagingInformation pagingInfo) {
        return null;
    }

    @Override
    public Page<Comanda> findAll(PagingInformation pagingInfo, Comanda entity) {

        List<Comanda> comenzi = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select distinct p.id, p.username, p.nume " +
                    "from comenzi c " +
                    "inner join persoane p on p.id = c.id_persoana " +
                    "inner join  soferi s on s.id = c.id_sofer " +
                    "where c.id_sofer = ? " +
                    "limit ? offset ?"))
        {

            statement.setLong(1, entity.getTaximetrist().getId());
            statement.setInt(2, pagingInfo.getPageSize());
            statement.setInt(3, (pagingInfo.getPageNumber() - 1) * pagingInfo.getPageSize());


            ResultSet result = statement.executeQuery();

            while(result.next())
            {
                Long id_persoana = result.getLong("id");
                String user = result.getString("username");
                String nume =  result.getString("nume");

                Persoana pers = new Persoana(user, nume);
                pers.setId(id_persoana);

                Sofer sofer = entity.getTaximetrist();

                Comanda cmd = new Comanda(pers, sofer, null);
                Long id = result.getLong("id");
                cmd.setId(id);

                comenzi.add(cmd);
            }

            return new PageObject<>(pagingInfo, comenzi.stream());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
