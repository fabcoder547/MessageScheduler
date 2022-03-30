package com.app.service;

import com.app.dao.MessageDao;
import com.app.exceptions.SQLErrorException;
import com.app.pojo.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class MessageserviceTest {


    @MockBean
    MessageDao messageDao;

    @Autowired
    Messageservice messageservice;


    @Test
    void saveMessage() throws SQLErrorException {
        when(messageDao.insetMessage(null, null)).thenReturn(1);
        int actualResult = messageservice.saveMessage(null, null);
        assertEquals(1, actualResult);

    }

    @Test
    void updateMessageStatus() {
        when(messageDao.updateMessageStatus(null, null, null, null, null)).thenReturn(1);
        int actualResult = messageservice.updateMessageStatus(null, null, null, null, null);
        assertEquals(1, actualResult);
    }

    @Test
    void pollMessagesFromDatabase() {
        List<Message> messageList = Collections.emptyList();
        when(messageDao.getAllMessagesInOneMinute()).thenReturn(messageList);
        List<Message> actualList = messageservice.pollMessagesFromDatabase();
        assertEquals(messageList.size(), actualList.size());
    }
}