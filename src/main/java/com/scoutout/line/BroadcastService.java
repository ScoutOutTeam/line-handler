package com.scoutout.line;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.template.ButtonsTemplate;
import com.linecorp.bot.model.message.template.ImageCarouselColumn;
import com.linecorp.bot.model.message.template.ImageCarouselTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
public class BroadcastService {

    private static final Logger logger = LoggerFactory
            .getLogger(Application.class);

    @Autowired
    private LineMessagingClient lineMessagingClient;

    @GetMapping("/send/campaign/{mid}")
    public ResponseEntity<?> sendMessage(@PathVariable String mid) {
        logger.debug("Send message to mid {}", mid);
        ImageCarouselTemplate imageCarouselTemplate = new ImageCarouselTemplate(
                Arrays.asList(
                        new ImageCarouselColumn("https://d32u7mh5s41ab0.cloudfront.net/cam2.jpg",
                                new URIAction(null,
                                        "https://www.scoutout.net")
                        )
                ));
        TemplateMessage templateMessage = new TemplateMessage("ImageCarousel alt text", imageCarouselTemplate);
        PushMessage pushMessage = new PushMessage(mid, templateMessage);
        lineMessagingClient.pushMessage(pushMessage);
        return new ResponseEntity<>(pushMessage.toString(), HttpStatus.OK);
    }

    @GetMapping("/send/job/{mid}")
    public ResponseEntity<?> sendMessage2(@PathVariable String mid) {
        logger.debug("Send message to mid {}", mid);
        ButtonsTemplate buttonsTemplate = new ButtonsTemplate(
                "https://d32u7mh5s41ab0.cloudfront.net/job1.jpg",
                "Jojoe แนะนำงานนี้ให้คุณ",
                "Product Owner ที่ ScoutOut (25k-30k)",
                Arrays.asList(
                        new URIAction("ดูรายละเอียดเพิ่มเติม",
                                "https://www.scoutout.net"),
                        new PostbackAction("มันใช่อ่ะ! สมัครเลย!",
                                "ScoutOut Hackaton 2017"),
                        new PostbackAction("แชร์ต่อให้เพื่อน",
                                "ScoutOut Hackaton 2017",
                                "ScoutOut Hackaton 2017"),
                        new MessageAction("บันทึกงานนี้ไว้",
                                "เราได้บันทึกงานนี้ไว้ให้คุณแล้ว")
                ));
        TemplateMessage templateMessage = new TemplateMessage("Button alt text", buttonsTemplate);
        PushMessage pushMessage = new PushMessage(mid, templateMessage);
        lineMessagingClient.pushMessage(pushMessage);
        return new ResponseEntity<>(pushMessage.toString(), HttpStatus.OK);
    }

}
