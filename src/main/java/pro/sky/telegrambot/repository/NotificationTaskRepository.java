package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pro.sky.telegrambot.model.NotificationTask;

import java.time.LocalDateTime;
import java.util.Collection;

public interface NotificationTaskRepository extends JpaRepository<NotificationTask,Integer> {

    @Query(value = "SELECT * FROM notification_task WHERE datetime = :datetime", nativeQuery = true)
    Collection<NotificationTask> getNotificationTaskByDatetime(@Param("datetime")LocalDateTime dateTime);

}
