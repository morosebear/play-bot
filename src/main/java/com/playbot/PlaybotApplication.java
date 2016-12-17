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

    private List<String> secret;
    private int guessCnt = 0;
    private StringBuilder guessHis;

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "play guess number game with a LINE bot.";
    }

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        String res = null;
        String msg = event.getMessage().getText();
        if ("go".equalsIgnoreCase(msg)) {
            newGame();
            res = "猜數字！(請輸入4位不重複的數字)";
        }
        else if (msg.matches("\\d{4}")) {
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
                res = "第" + guessCnt + "猜：猜中惹 94狂～ (surprise)(surprise)(surprise)";
                newGame();
            }
            else {
                guessHis = guessHis.append("第" + guessCnt + "猜：" + cntA + "A" + cntB + "B" + System.lineSeparator());
                res = guessHis.toString();
            }
        }
        return (res == null ? null : new TextMessage(res));
    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        System.out.println("event: " + event);
    }

    private void newGame() {
        secret = randomFourDigi();
        guessCnt = 0;
        guessHis = new StringBuilder("");
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
        SpringApplication.run(PlaybotApplication.class, args);
    }

}
