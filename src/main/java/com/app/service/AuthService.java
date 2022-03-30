package com.app.service;


import com.app.dao.ClientDao;
import com.app.dao.MessageDao;
import com.app.pojo.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {


    @Autowired
    ClientDao clientDao;


    public Client validateToken(String token){
        Client client = clientDao.getClientUsingToken(token);
        return client;
    }


}
