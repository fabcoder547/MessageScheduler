package com.app.timer_tasks;

import com.app.exceptions.SQLErrorException;
import com.app.pojo.Message;
import com.app.service.Messageservice;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;


//this task will run in every one minute and call gupshup whatsapp API.....
@Component
public class Task extends TimerTask {
    Logger logger = LoggerFactory.getLogger(Task.class);
    @Autowired
    Messageservice messageservice;

    public static String encodeParam(String data) {
        String result = "";
        try {
            result = URLEncoder.encode(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static byte[] getParamsByte(Map<String, Object> params) {
        byte[] result = null;
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0) {
                postData.append('&');
            }
            postData.append(encodeParam(param.getKey()));
            postData.append('=');
            postData.append(encodeParam(String.valueOf(param.getValue())));
        }
        result = postData.toString().getBytes(StandardCharsets.UTF_8);
        return result;
    }

    @Override
    public void run() {
        List<Message> messageList = null;

        try {
            messageList = messageservice.pollMessagesFromDatabase();
        } catch (SQLErrorException e) {
            logger.info(e.getMessage());
            return;
        }

        if (messageList.isEmpty()) {
            logger.info("messagelist is empty");
            return;
        }

        logger.info("Following are the messages to send................");
        Gson gson = new Gson();
        URL url = null;
        HttpURLConnection con = null;

        //Iterate over messagelist and send it to the destination using Gupshup Whatsapp API..
        for (Message ms : messageList) {
            logger.info("Running for messageID- " + ms.getMessage_id());
            try {
                url = new URL("https://api.gupshup.io/sm/api/v1/msg");
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setUseCaches(false);
                con.setDoOutput(true);
                con.setDoInput(true);

                //set header to the request.............
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                con.setRequestProperty("apikey", "mxpfkxmtyckyvbh0t605x06glopo2gac");
                con.setRequestProperty("Accept", "application/json");

                OutputStream outputStream = con.getOutputStream();

                //form the message{type:"{type of the message want to send}" ,text:"{message to send}"}
                HashMap<String, String> message = new HashMap<String, String>();
                message.put("type", "text");
                message.put("text", ms.getMessage());

                //convert message to json object.........
                String jsonString = gson.toJson(message);
                JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
                //form request body....
                Map<String, Object> body = new HashMap<>();
                body.put("channel", "whatsapp");
                body.put("source", "917834811114");
                body.put("destination", ms.getDestination_phone_number());
                body.put("message", jsonObject);
                body.put("src.name", "BankApp");

                //write body to the stream
                outputStream.write(getParamsByte(body));
                logger.info("outputstream here--> " + outputStream);
                logger.info("response code here--> " + con.getResponseCode());

                if (con.getResponseCode() == HttpURLConnection.HTTP_ACCEPTED) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    Map<String, String> response = objectMapper.readValue(con.getInputStream(), Map.class);
                    logger.info("response--> " + response.toString());
                    int result = messageservice.updateMessageStatus(false, true, response.get("messageId"), LocalDateTime.now(), ms.getMessage_id());
                    if (result < 1) {
                        logger.info("Error occured while updating status..");
                    } else logger.info("Status of message is updated--> " + result);
                } else {
                    //mark submitted_status as failed
                    int result = messageservice.updateMessageStatus(false, false, null, null, ms.getMessage_id());
                    logger.info("Message sending failed fot mesageID " + ms.getMessage_id());
                }
            }
            catch (Exception e) {
                //exception occured during above process -  means app failed at some point
               logger.info("exception occured during sending messages through gupshup API");
               e.printStackTrace();
            }
        }
    }
}