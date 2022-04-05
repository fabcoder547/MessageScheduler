package com.app.service;

import com.app.dao.ClientDao;
import com.app.exceptions.SQLErrorException;
import com.app.pojo.Client;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@SpringBootTest
class AuthServiceTest {


    @Autowired
    AuthService authService;


    @MockBean
    ClientDao clientDao;


    @Test
    void validateToken() throws SQLErrorException {
        Client testClient = new Client(10125, "dummy", "dummytoken");
        when(clientDao.getClientUsingToken("dummytoken")).thenReturn(testClient);
        assertThat(authService.validateToken("dummytoken")).isEqualTo(testClient);
    }


    @Test
    void validateTokenAsInvalid() throws SQLErrorException {
        when(clientDao.getClientUsingToken("Invalid Token")).thenReturn(null);
        assertThat(authService.validateToken("Invalid Token")).isNull();
    }
}