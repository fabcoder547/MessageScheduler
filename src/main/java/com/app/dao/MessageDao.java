package com.app.dao;

import com.app.exceptions.SQLErrorException;
import com.app.pojo.Client;
import com.app.pojo.Message;
import com.app.pojo.Request;
import com.app.rowmappers.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class MessageDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void welcome() {
        System.out.println("welcome to the dao.");
    }


    public int insetMessage(Request requestBody, Client client) throws SQLErrorException {
        int result = 0;
        String query = "insert into message(message,scheduled_at,destination_phone_number,client_id,created_at,pending_status,scheduled_status) values (?,?,?,?,?,?,?)";
        try {
            result = jdbcTemplate.update(query, requestBody.getMessage(), requestBody.getScheduledTime(),
                    requestBody.getPhonenumber(), client.getClientId(), LocalDateTime.now(), true, true);
            return result;
        } catch (Exception e) {
            throw new SQLErrorException("error while doing sql operation", 1003);
        }

    }


    public List<Message> getAllMessagesInOneMinute() {
//        String query = "select * from message where pending_status = true and scheduled_at between now() and date_add(now(), interval 1 minute)";
		String query = "select * from message where pending_status = true and scheduled_at < date_add(now(),interval 1 minute)";

        List<Message> messages = Collections.emptyList();
        System.out.println("In polling function...at " + LocalDateTime.now());
        try {
            messages = jdbcTemplate.query(query, new MessageMapper());
            return messages;
        } catch (Exception e) {
            System.out.println("exception in executing query");
            return null;
        }
    }

    public int updateMessageStatus(Boolean pending_status, Boolean submited_status, String whatsAppMessageId, LocalDateTime submitted_at, Integer message_id) {
        System.out.println("in update function");
        String query = "UPDATE message set pending_status = ?, submitted_status=?, submitted_at=?,whatsapp_api_message_id=? where message_id = ?";
        int result = 0;
        try {
            result = jdbcTemplate.update(query, pending_status, submited_status, submitted_at, whatsAppMessageId, message_id);
            return result;
        } catch (Exception e) {
            return 0;
        }

    }


}






















