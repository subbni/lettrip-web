package com.lettrip.lettripbackend.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    /* 웹소켓 서버의 엔드포인트와 클라이언트 origin 등록 */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        registry.addEndpoint("/ws/chat")
                .setAllowedOrigins("*"); //  (*: 모든 origin 허용)
        //TODO: 배포시 특정 origin만 등록하기
    }

    /* 어플리케이션 내부에서 사용할 path를 지정 */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/sub");
        // 클라이언트 사용자의 구독 경로 "/sub"
        registry.setApplicationDestinationPrefixes("/pub");
        // 메세지 발송 경로 "/pub"
        // /pub가 prefix로 붙은 메세지는 @MessageMapping이 붙은 메서드로 바운드

    }
}
