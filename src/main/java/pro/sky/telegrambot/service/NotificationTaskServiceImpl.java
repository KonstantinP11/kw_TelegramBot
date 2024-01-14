package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class NotificationTaskServiceImpl implements NotificationTaskService {
    private TelegramBot telegramBot;
    private NotificationTaskRepository repository;
    private static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private static Pattern MESSAGE_PATTERN = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");
    private Logger logger = LoggerFactory.getLogger(NotificationTaskServiceImpl.class);

    @Override
    public void process(Update update) {
        if (update.message() == null) {
            logger.info("User sent empty message");
            return;
        }
        String clientMessage = update.message().text();
        long chatId = update.message().chat().id();
        if (clientMessage == null) {
            telegramBot.execute(new SendMessage(chatId, "Для начала работы с ботом отправьте /stsrt"));
            return;
        }
        if (clientMessage.equals("/start")) {
            telegramBot.execute(new SendMessage(chatId, "Hello! "
                    + update.message().chat().firstName() + "! Напоминание добавляется в формате 'dd.MM.yyyy HH:mm текст напоминания'"));
            return;
        }

        Matcher matcher = MESSAGE_PATTERN.matcher(clientMessage);
        if (matcher.find()) {
            LocalDateTime alarmDate = LocalDateTime.parse(matcher.group(1), DATE_TIME_FORMATTER);
            String notification = matcher.group(3);
            NotificationTask notificationTask = new NotificationTask(chatId, notification, alarmDate);
            repository.save(notificationTask);
            telegramBot.execute(new SendMessage(chatId, "Напоминание сохранено " + notificationTask));
        } else {
            telegramBot.execute(new SendMessage(chatId, "Напоминание добавляется в формате 'dd.MM.yyyy HH:mm текст напоминания'"));
        }
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void fetchDatabaseRecords() {
        List<NotificationTask> records =
                repository.findByAlarmDate(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        records.forEach(record -> {
            logger.info("Notification was sent");
            telegramBot.execute(new SendMessage(record.getChatId(), String.format("Напоминание: \n%s" +
                    " ,в %s", record.getNotification(), record.getAlarmDate())));
        });
    }
}