package com.app.dao;


import com.app.exceptions.SQLErrorException;
import com.app.pojo.Client;
import com.app.rowmappers.ClientMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ClientDao {
    Logger logger = LoggerFactory.getLogger(ClientDao.class);

    @Autowired
    JdbcTemplate jdbcTemplate;


    //will check is there any client in DB with this token or not
    public Client getClientUsingToken(String token) throws SQLErrorException {
        String query = "select * from client where token= ?";
        Client client = null;
        try {
            client = jdbcTemplate.queryForObject(query, new ClientMapper(), token);
            logger.info("query--> " + query);
            logger.info("query result--> " + client.toString());
            return client;
        } catch (Exception e) {
            logger.warn(e.getMessage());
            throw new SQLErrorException("sql error while validating client using token");
        }

    }

}
