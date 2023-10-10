package pro.sky.telegrambot.model;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
@Entity
@Table(name="notifications")
public class NotificationTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "chat_id")
    private long chatId;
    @Column(name = "notificationtask")
    private String notificationTask;
    @Column(name = "tasktime")
    private LocalDateTime taskTime;

    public NotificationTask(long chatId, String notificationTask, LocalDateTime taskTime) {
        this.chatId = chatId;
        this.notificationTask = notificationTask;
        this.taskTime = taskTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationTask that = (NotificationTask) o;
        return chatId == that.chatId && Objects.equals(notificationTask, that.notificationTask) && Objects.equals(taskTime, that.taskTime);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public NotificationTask() {
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, notificationTask, taskTime);
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getNotificationTask() {
        return notificationTask;
    }

    public void setNotificationTask(String notificationTask) {
        this.notificationTask = notificationTask;
    }

    public LocalDateTime getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(LocalDateTime taskTime) {
        this.taskTime = taskTime;
    }

}
