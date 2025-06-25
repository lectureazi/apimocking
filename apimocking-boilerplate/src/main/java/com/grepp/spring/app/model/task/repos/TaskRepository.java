package com.grepp.spring.app.model.task.repos;

import com.grepp.spring.app.model.schedule.domain.Schedule;
import com.grepp.spring.app.model.task.domain.Task;
import com.grepp.spring.app.model.schedule.domain.Schedule;
import com.grepp.spring.app.model.task.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TaskRepository extends JpaRepository<Task, Long> {

    Task findFirstBySchedule(Schedule schedule);

}
