package com.app.dao;

import com.app.exceptions.SQLErrorException;
import com.app.pojo.Client;
import com.app.pojo.Message;
import com.app.pojo.Request;
import com.app.rowmappers.MessageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Repository
public class MessageDao {
    Logger logger = LoggerFactory.getLogger(MessageDao.class);
    @Autowired
    JdbcTemplate jdbcTemplate;

    public void welcome() {
        System.out.println("welcome to the dao.");
    }


    //insert message to schedule in database..
    public int insertMessage(Request requestBody, Client client) throws SQLErrorException {
        int result = 0;
        String query = "insert into message(message,scheduled_at,destination_phone_number,client_id,created_at,pending_status,scheduled_status) values (?,?,?,?,?,?,?)";
        try {
            result = jdbcTemplate.update(query, requestBody.getMessage(), requestBody.getScheduledTime(),
                    requestBody.getPhonenumber(), client.getClientId(), LocalDateTime.now(), true, true);
            logger.info("query running--> " + query);
            return result;
        } catch (Exception e) {
            logger.warn(e.getMessage());
            throw new SQLErrorException("error while doing sql operation");
        }

    }


    //pull all messagees to se(PRIMARY KEY) - unique MessageID for every message
    public List<Message> getAllMessagesInOneMinute() throws SQLErrorException {
//        String query = "select * from message where pending_status = true and scheduled_at between now() and date_add(now(), interval 1 minute)";
        String query = "select * from message where pending_status = true and scheduled_at < date_add(now(),interval 1 minute)";

        List<Message> messages = Collections.emptyList();
        logger.info("polling messages at " + LocalDateTime.now());
        try {
            messages = jdbcTemplate.query(query, new MessageMapper());
            return messages;
        } catch (Exception e) {
            logger.warn(e.getMessage());
            throw new SQLErrorException("error while doing polling messages from DB");
        }
    }


    //after message is sent update its status accordingly...
    public int updateMessageStatus(Boolean pending_status, Boolean submited_status, String whatsAppMessageId, LocalDateTime submitted_at, Integer message_id) throws SQLErrorException {
        String query = "UPDATE message set pending_status = ?, submitted_status=?, submitted_at=?,whatsapp_api_message_id=? where message_id = ?";
        logger.info("updating message status for messageId " + message_id);
        int result = 0;
        try {
            result = jdbcTemplate.update(query, pending_status, submited_status, submitted_at, whatsAppMessageId, message_id);
            return result;
        } catch (Exception e) {
            logger.warn(e.getMessage());
           throw  new SQLErrorException("sql error while updating status for messageID "+message_id);
        }

    }


}






















