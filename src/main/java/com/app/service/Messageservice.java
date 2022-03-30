package com.app.service;


import com.app.dao.MessageDao;
import com.app.exceptions.SQLErrorException;
import com.app.pojo.Client;
import com.app.pojo.Message;
import com.app.pojo.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class Messageservice {

    @Autowired
    MessageDao messageDao;


    public int saveMessage(Request requestBody, Client client) throws SQLErrorException {
        return messageDao.insetMessage(requestBody,client);
    }

    public int updateMessageStatus(Boolean pending_status, Boolean submited_status, String whatsAppMessageId, LocalDateTime submitted_at, Integer message_id){

        return messageDao.updateMessageStatus(pending_status, submited_status, whatsAppMessageId, submitted_at, message_id);
    }


    public List<Message> pollMessagesFromDatabase(){
        return messageDao.getAllMessagesInOneMinute();
    }








}
