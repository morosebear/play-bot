package com.playbot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

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

    private int guessCnt = 0;
    private List<String> secret;

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "play with Line's Messaging API";
    }

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        // System.out.println("event: " + event);
        // String reversedText = new StringBuffer(event.getMessage().getText()).reverse().toString();
        // return new TextMessage(reversedText);
        String res = null;
        String msg = event.getMessage().getText();
        if ("go".equalsIgnoreCase(msg)) {
            newGame();
            res = "猜數字！(請輸入4位不重複的數字)";
        }
        else {
            guessCnt++;
            int cntA = 0, cntB = 0;
            String s;
            for (int i = 0; i < 4; i++) {
                s = String.valueOf(msg.charAt(i));
                if (s.equals(secret.get(i))) {
                    cntA++;
                }
                else if (secret.contains(s)) {
                    cntB++;
                }
            }
            if (cntA == 4) {
                res = "第" + guessCnt + "次：狂喔～猜中惹！(surprise)(surprise)(surprise)";
                newGame();
            }
            else {
                res = "第" + guessCnt + "次：" + cntA + "A" + cntB + "B";
            }
        }
        return new TextMessage(res);
    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        System.out.println("event: " + event);
    }

    private void newGame() {
        guessCnt = 0;
        secret = randomFourDigi();
    }

    private List<String> randomFourDigi() {
        int rdn;
        Set<String> s = new HashSet<String>(4);
        Random r = new Random();
        for (int i = 0; i < 4; i++) {
            do {
                rdn = r.nextInt(10);
            }
            while (!s.add(String.valueOf(rdn)));
        }
        List<String> res = new ArrayList<String>();
        res.addAll(s);
        return res;
    }

    public static void main(String[] args) {
        System.setProperty("line.bot.channelToken",
                "Xsea4Shuu4mppQrCsvygtCfHCXFZOjeHYT7J/mTkxFwmdkoVIM4ZYF5CMZnSP5sX3z6p+a8bjBA0mZ2aMHH2bpWbv5e7sRfOTO1zc9LyzdN/MhU0kaibFb4hGhQJ7npsZIcf8AesPkHCfwhVD8TxsgdB04t89/1O/w1cDnyilFU=");
        System.setProperty("line.bot.channelSecret", "3ddbaba52ebbc09fa38defac7ef35516");

        SpringApplication.run(PlaybotApplication.class, args);
    }

}
