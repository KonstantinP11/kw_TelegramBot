package pro.sky.telegrambot.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "notification_task")
public class NotificationTask {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;
    @Column(name = "chat_id")
    private final Long chatId;
    @Column(name = "notification")
    private final String notification;
    @Column(name = "alarm_date")
    private final LocalDateTime alarmDate;

    public NotificationTask(Long chatId, String notification, LocalDateTime alarmDate) {
        this.chatId = chatId;
        this.notification = notification;
        this.alarmDate = alarmDate;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationTask that = (NotificationTask) o;
        return Objects.equals(id, that.id) && Objects.equals(chatId, that.chatId) && Objects.equals(notification, that.notification) && Objects.equals(alarmDate, that.alarmDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatId, notification, alarmDate);
    }
}