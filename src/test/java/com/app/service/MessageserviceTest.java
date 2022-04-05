package com.app.service;

import com.app.dao.MessageDao;
import com.app.exceptions.SQLErrorException;
import com.app.pojo.Client;
import com.app.pojo.Message;
import com.app.pojo.Request;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class MessageserviceTest {


    @MockBean
    MessageDao messageDao;

    @Autowired
    Messageservice messageservice;

    Logger logger = LoggerFactory.getLogger(MessageserviceTest.class);

    @Test
    void saveMessage() throws SQLErrorException {
        Client dummyclient = new Client(101, "dummy", "dummytoken");
        Request request = new Request("test message", "7972757302", "2022-04-30T15:45:20");
        when(messageDao.insertMessage(request, dummyclient)).thenReturn(1);
        int actualResult = messageservice.saveMessage(request, dummyclient);
        assertThat(actualResult).isEqualTo(1);

    }

    @Test
    void saveMessageAsNull() {
        String expectedMessage = "sql error while inserting message";
        String actualMessage = "";


        Client dummyclient = new Client(101, "dummy", "dummytoken");
        Request request = new Request("test message", "7972757302", "2022-04-30T15:45:20");
        try {
            when(messageDao.insertMessage(any(), any())).thenThrow(new SQLErrorException("sql error while inserting message"));
            int actualResult = messageservice.saveMessage(null, null);
            assertEquals(1, actualResult);
        } catch (SQLErrorException e) {
            actualMessage = e.getMessage();

        }
        assertThat(actualMessage).isEqualTo(expectedMessage);

    }


    @Test
    void updateMessageStatus() throws SQLErrorException {
        when(messageDao.updateMessageStatus(any(), any(), any(), any(), any())).thenReturn(1);
        int actualResult = messageservice.updateMessageStatus(any(), any(), any(), any(), any());
        assertEquals(1, actualResult);
    }

    @Test
    void pollMessagesFromDatabase() throws SQLErrorException {
        List<Message> messageList = Collections.emptyList();
        when(messageDao.getAllMessagesInOneMinute()).thenReturn(messageList);
        List<Message> actualList = messageservice.pollMessagesFromDatabase();
        assertEquals(messageList.size(), actualList.size());
    }
}