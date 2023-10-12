package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.service.ScheduleTask;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    @Autowired
    private TelegramBot telegramBot;

    private static Pattern pattern = Pattern.compile("([0-9\\.:\s]{16})(\\s)(.+)");
    private static DateTimeFormatter dateTimeFormatter= DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private final ScheduleTask scheduleTask;

    public TelegramBotUpdatesListener(TelegramBot telegramBot, ScheduleTask scheduleTask) {
        this.telegramBot = telegramBot;
        this.scheduleTask = scheduleTask;
    }

    //method to sent welcome message if "/start" command was sent
    private void startMessageReceived(Long chatId, String userName){
        String responseMessage = "Welcome, " + userName + ". To save task notification, please enter the date, time and task name in format:  dd.MM.yyyy HH:mm 'Task name To DO'";
        sendMessage(chatId, responseMessage);
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    //method to send message
    private void sendMessage(Long chatId, String sendingMessage){
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), sendingMessage);
        telegramBot.execute(sendMessage);
    }

    //method to process message in dependence from sending text
    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            long chatId = update.message().chat().id();
            String message = update.message().text();
            if (message.equals(null)) {
                logger.info("Null message was sent", chatId);
                return;
            }
            if (message.equals("/start")) {
                logger.info("Start message", chatId);
                startMessageReceived(chatId, update.message().chat().firstName());
            }
            Matcher matcher = pattern.matcher(message);
            if (!matcher.matches()) {
                logger.info("Invalid format", chatId);
                sendMessage(chatId,"Invalid format");
                return;
            }
            String date = matcher.group(1);
            LocalDateTime taskTime = LocalDateTime.parse(date, dateTimeFormatter);
            if (taskTime.isBefore(LocalDateTime.now())) {
                sendMessage(chatId, "Your entering date is before date now");
                logger.info("Date is before now", chatId);
                return;
            }
            String notification = matcher.group(3);
            scheduleTask.saveNotificationToDb(chatId, notification, taskTime);
            logger.info("Notification was saved into DB", chatId);
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}
