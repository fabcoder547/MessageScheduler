package com.app.dao;

import com.app.exceptions.SQLErrorException;
import com.app.pojo.Client;
import com.app.pojo.Message;
import com.app.pojo.Request;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class MessageDaoTest {

    @Autowired
    MessageDao messageDao;


    @Test
    void insetMessage() throws SQLErrorException {
        Client dummyclient = new Client(101,"dummy","dummytoken");
        Request request = new Request("dummy message for testing purpose", "7972757302", "2022-03-30T18:05:20");
        int actualResult = messageDao.insetMessage(request,dummyclient);
        assertEquals(1,actualResult);
    }


    @Test
    void getAllMessagesInOneMinute(){
        List<Message> messageList= messageDao.getAllMessagesInOneMinute();
        assertThat(messageList).isNotNull();
    }

    @Test
    void updateMessageStatus(){
       int actualResult =  messageDao.updateMessageStatus(false,true,"dummy_whatsapp_Id", LocalDateTime.now(),23);
       assertEquals(1,actualResult);
    }
}