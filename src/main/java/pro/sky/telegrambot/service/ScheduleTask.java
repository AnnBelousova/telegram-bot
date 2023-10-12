package pro.sky.telegrambot.service;

import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

public interface ScheduleTask {
    void findNotificationFromDb();
    void saveNotificationToDb(long chatId, String notification, LocalDateTime date);
}
