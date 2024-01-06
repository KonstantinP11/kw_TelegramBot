package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            // Process your updates here
            String text = update.message().text();
            System.out.println(text);
            long chatId = update.message().chat().id();
            if (Objects.equals(text, "/start")) {
                // Send messages
                SendResponse response = telegramBot.execute(new SendMessage(chatId, "Hello! "
                        + update.message().chat().firstName()));
            }else {
                    SendResponse response = telegramBot.execute(new SendMessage(chatId, "Let's start with command /start, "
                            + update.message().chat().firstName()));
            }
            System.out.println(chatId);
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

}
