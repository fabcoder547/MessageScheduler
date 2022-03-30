package com.app.dao;

import com.app.pojo.Client;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class ClientDaoTest {


    @Autowired
    ClientDao clientDao;


    @Test
    void getClientUsingToken() {
        String token = "dummytoken";
        Client expectedClient = new Client(101, "dummy", "dummytoken");
        Client actualResult = clientDao.getClientUsingToken(token);
        assertThat(actualResult.toString()).isEqualTo(expectedClient.toString());
    }
}