package com.app.timer_tasks;

import com.app.pojo.Message;
import com.app.service.Messageservice;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.dao.MessageDao;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.*;


@Component
public class Task extends TimerTask {

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
		try {
			result = postData.toString().getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public void run() {
		List<Message> messageList = messageservice.pollMessagesFromDatabase();
		if (messageList.isEmpty()) {
			System.out.println("messageList is empty");
			return;
		}

		System.out.println("Following are the messages to send................");
		Gson gson = new Gson();
		URL url = null;
		HttpURLConnection con = null;

		//Iterate over messagelist and send it to the destination using Gupshup Whatsapp API..
		for (Message ms : messageList) {
			System.out.println("Running for messageID- " + ms.getMessage_id());
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
				System.out.println("outputstream here... " + outputStream.toString());
				System.out.println(" response code here--> " + con.getResponseCode());

				if (con.getResponseCode() == HttpURLConnection.HTTP_ACCEPTED) {
					ObjectMapper objectMapper = new ObjectMapper();
					Map<String, String> response = objectMapper.readValue(con.getInputStream(), Map.class);
					System.out.println("MessageID is -->  " + response.get("messageId"));
					System.out.println(response.toString());
					int result = messageservice.updateMessageStatus(false,true, response.get("messageId"),LocalDateTime.now(),ms.getMessage_id());
					if(result <1){
						System.out.println("Error occured while updating status....");
					}else System.out.println("Status of meesges is updated--> "+ result);
				} else {
					//mark submitted_status as failed
					int result = messageservice.updateMessageStatus(false,false, null,null,ms.getMessage_id());
					System.out.println("Message sending failed fot mesageID " + ms.getMessage_id());
				}
			} catch (Exception e) {
				//mark submitted_status as failed
				int result = messageservice.updateMessageStatus(false,false, null,null,ms.getMessage_id());
				System.out.println(e.getMessage());
			}
		}
	}
}