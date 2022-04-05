package com.app.service;


import com.app.dao.ClientDao;
import com.app.exceptions.SQLErrorException;
import com.app.pojo.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    Logger logger = LoggerFactory.getLogger(AuthService.class);
    @Autowired
    ClientDao clientDao;


    public Client validateToken(String token) throws SQLErrorException {
        logger.info("in validate token service");
        Client client = clientDao.getClientUsingToken(token);
        return client;
    }


}
