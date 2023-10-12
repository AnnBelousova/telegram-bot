package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ScheduleTaskImpl implements ScheduleTask {
    private final TelegramBot telegramBot;
    private final NotificationTaskRepository notificationTaskRepository;

    public ScheduleTaskImpl(TelegramBot telegramBot, NotificationTaskRepository notificationTaskRepository) {
        this.telegramBot = telegramBot;
        this.notificationTaskRepository = notificationTaskRepository;
    }

    //method for finding task notification in the DB, and sending notification message to user
    @Scheduled(cron = "0 0/1 * * * *")
    @Override
    public void findNotificationFromDb() {
        LocalDateTime dateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        List<NotificationTask> tasks = notificationTaskRepository.findByTaskTime(dateTime);
        tasks.forEach(t -> {
            SendMessage sendNotification = new SendMessage(t.getChatId(),"The time has come: " + t.getNotificationTask().toUpperCase());
            telegramBot.execute(sendNotification);
        });
    }

    //method to save task into DB
    @Override
    public void saveNotificationToDb(long chatId, String notification, LocalDateTime date) {
        NotificationTask notificationTask = new NotificationTask(chatId, notification, date);
        notificationTaskRepository.save(notificationTask);
    }
}
