package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final TelegramBot telegramBot;
    private final NotificationTaskRepository repository;
    private static Pattern pattern;
    private static Matcher matcher;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, NotificationTaskRepository repository) {
        this.telegramBot = telegramBot;
        this.repository = repository;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            String incomingMessage = update.message().text();
            Long chatId = update.message().chat().id();
            if (incomingMessage.equals("/start")) {
                sendMessage("Ожидаю сообщения формата: 01.01.2022 20:00 Сделать домашнюю работу", chatId);
            } else {
                pattern = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");
                matcher = pattern.matcher(incomingMessage);
                if (matcher.matches()) {
                    String date = matcher.group(1);
                    String item = matcher.group(3);
                    LocalDateTime localDateTime = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
                    NotificationTask notificationTask = new NotificationTask(chatId, item, localDateTime);
                    repository.save(notificationTask);
                    logger.info("saved notification", notificationTask);
                    sendMessage("Напомним!", chatId);
                }

            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Scheduled(cron = "0 0/1 * * * *") // runs at 00 sec on every minute
    public void sendNotificationTasks() {
        //Collection<NotificationTask> currentTasks = repository.getNotificationTaskByDatetime(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        Collection<NotificationTask> currentTasks = repository.findNotificationTaskByDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
        currentTasks.forEach(task ->
                sendMessage(task.getMessageText(), task.getChatId()));
    }

    private void sendMessage(String message, Long chatId) {
        SendMessage sendMessage = new SendMessage(chatId, message);
        SendResponse response = telegramBot.execute(sendMessage);
        if (!response.isOk()) {
            logger.warn("Сообщение не отправлено: {}, error code: {}", message, response.errorCode());
        }else {
            logger.info("Сообщение отправлено: " + message);
        }
    }
}
