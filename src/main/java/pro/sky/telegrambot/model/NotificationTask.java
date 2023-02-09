package pro.sky.telegrambot.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class NotificationTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Long chat_id;
    private String message_text;
    private LocalDateTime datetime;

    public NotificationTask() {
    }

    public NotificationTask(Long chatId, String message, LocalDateTime dateTime) {
        this.chat_id = chatId;
        this.message_text = message;
        this.datetime = dateTime;
    }

    public Long getChat_id() {
        return chat_id;
    }

    public void setChat_id(Long chatId) {
        this.chat_id = chatId;
    }

    public String getMessage_text() {
        return message_text;
    }

    public void setMessage_text(String message) {
        this.message_text = message;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime dateTime) {
        this.datetime = dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationTask that = (NotificationTask) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
