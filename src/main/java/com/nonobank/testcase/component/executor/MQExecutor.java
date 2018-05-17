package com.nonobank.testcase.component.executor;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;

@Component
public class MQExecutor {
	 public enum SendKey {
	        STATUS, RESULT, groupName, topic, tag, body, key
	    }

	    private String resultMap(boolean status, String result) {
	        Map<String, Object> resultMap = new HashMap<>();
	        resultMap.put(SendKey.STATUS.toString(), status);
	        resultMap.put(SendKey.RESULT.toString(), result);
	        return JSON.toJSONString(resultMap);
	    }


	    public String sendMessage(String address, JSONObject jsonObject) {
	        DefaultMQProducer producer = null;
	        try {
	            producer = new DefaultMQProducer(String.valueOf(jsonObject.get(SendKey.groupName.toString())));
	            producer.setNamesrvAddr(address);
	            producer.start();
	            String msg = JSONObject.toJSONString(jsonObject.get(SendKey.body.toString()));
	            byte[] body = msg.getBytes("UTF-8");
	            Message sendMessage = new Message(
	                    String.valueOf(jsonObject.get(SendKey.topic.toString())),
	                    String.valueOf(jsonObject.get(SendKey.tag.toString())),
	                    String.valueOf(jsonObject.get(SendKey.key.toString())),
	                    body);
	            SendResult result = producer.send(sendMessage);
	            return this.resultMap(true, String.valueOf(result));
	        } catch (Exception e) {
	            return this.resultMap(false, String.valueOf(e.toString()));
	        } finally {
	            if (producer != null) {
	                producer.shutdown();
	            }
	        }
	    }
}
