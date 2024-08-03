package com.chatapp.config;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.chatapp.model.ChatMessage;
import com.chatapp.model.MessageType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {
	
	private final SimpMessageSendingOperations messageTemplate;
	
	@EventListener
	public void handleWebSocketDisconnetListener(SessionDisconnectEvent event)
	{
		StompHeaderAccessor headerAccessor=StompHeaderAccessor.wrap(event.getMessage());
		
		String username=headerAccessor.getSessionAttributes().get("username").toString();
		
		if(username!=null)
		{
			log.info("user disconnected "+username);
			var chatMessage=ChatMessage.builder()
					.type(MessageType.LEAVE)
					.sender(username)
					.build();
			
			messageTemplate.convertAndSend("/topic/public",chatMessage);
		}
	}

}
