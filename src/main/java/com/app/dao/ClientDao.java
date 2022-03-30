package com.app.dao;


import com.app.pojo.Client;
import com.app.rowmappers.ClientMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ClientDao {

    @Autowired
    JdbcTemplate jdbcTemplate;


    public Client getClientUsingToken(String token)  {
        String query = "select * from client where token= ?";
        Client client = null;
        try {
            client = jdbcTemplate.queryForObject(query,new ClientMapper(),new Object[] {token});

            return client;
        } catch (Exception e) {
            return null;
        }

    }


    public boolean isClientTest(){
        return true;
    }
}
