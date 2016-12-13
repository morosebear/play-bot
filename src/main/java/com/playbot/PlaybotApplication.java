package com.playbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

@SpringBootApplication
@LineMessageHandler
@Controller
@EnableAutoConfiguration
public class PlaybotApplication {

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "play with Line's Messaging API";
    }

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        System.out.println("event: " + event);
        String reversedText = new StringBuffer(event.getMessage().getText()).reverse().toString();
        return new TextMessage(reversedText);
    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        System.out.println("event: " + event);
    }

    public static void main(String[] args) {
        System.setProperty("line.bot.channelToken",
                "Xsea4Shuu4mppQrCsvygtCfHCXFZOjeHYT7J/mTkxFwmdkoVIM4ZYF5CMZnSP5sX3z6p+a8bjBA0mZ2aMHH2bpWbv5e7sRfOTO1zc9LyzdN/MhU0kaibFb4hGhQJ7npsZIcf8AesPkHCfwhVD8TxsgdB04t89/1O/w1cDnyilFU=");
        System.setProperty("line.bot.channelSecret", "3ddbaba52ebbc09fa38defac7ef35516");

        SpringApplication.run(PlaybotApplication.class, args);
    }

}
