package com.nonobank.testcase.component.ws;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@ServerEndpoint(value="/webSocket/{sessionId}")
@Component
public class WebSocket {
	
	public static Logger logger = LoggerFactory.getLogger(WebSocket.class);
	
    private static Map<String, WebSocket> clients = new ConcurrentHashMap<String, WebSocket>();  
    
    private static int onLineCount = 0;
    
    private Session session;
	
	@OnOpen
	public void onOpen(@PathParam("sessionId") String sessionId,Session session) throws IOException{
		logger.info("打开新webSocket连接, sessionId: " + sessionId);
		this.session = session;
		clients.put(sessionId, this);
		
//		for(int i=0;i<100;i++){
//			this.session.getBasicRemote().sendText("xxxxxxxxxxxxxxxxxxxxxxxxxxx");
//		}
	}

	@OnClose
	public void onClose(){
		logger.info("关闭webSocket连接...");
		clients.remove(this);
		subOnLineCount();
	}
	
	@OnMessage
	public void onMessage(String message){
		logger.info("收到客户端信息...");
//		sendMsgTo(message, this.session.toString());
	}
	
	@OnError 
    public void onError(Session session, Throwable error) {  
        error.printStackTrace();  
    }  
	
	public void sendMsgTo(int type, String msg, String to){
		
		JSONObject wsJsonObj = new JSONObject();
		
		wsJsonObj.put("type", type);
		
		wsJsonObj.put("value", msg);
		
		clients.forEach((k, v)->{
			if(k.equals(to)){
				try {
					v.session.getBasicRemote().sendText(wsJsonObj.toJSONString());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	public static synchronized int addOnLineCount(){
		return WebSocket.onLineCount++;
	}
	
	public static synchronized int subOnLineCount(){
		return WebSocket.onLineCount--;
	}
	
	public static int getOnLineCount(){
		return WebSocket.onLineCount;
	}
}
