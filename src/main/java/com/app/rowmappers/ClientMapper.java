package com.app.rowmappers;

import com.app.pojo.Client;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


//Row mapper for client...
public class ClientMapper implements RowMapper<Client> {

    @Override
    public Client mapRow(ResultSet rs, int rowNum) throws SQLException {

        Client client = new Client();
        client.setClientId(rs.getInt("client_id"));
        client.setClientName(rs.getString("client_name"));
        client.setToken(rs.getString("token"));
        return client;
    }

}
