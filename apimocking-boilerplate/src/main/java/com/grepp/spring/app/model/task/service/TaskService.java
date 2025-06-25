package com.grepp.spring.app.model.task.service;

import com.grepp.spring.app.model.schedule.domain.Schedule;
import com.grepp.spring.app.model.task.domain.Task;
import com.grepp.spring.app.model.schedule.domain.Schedule;
import com.grepp.spring.app.model.schedule.repos.ScheduleRepository;
import com.grepp.spring.app.model.task.domain.Task;
import com.grepp.spring.app.model.task.model.TaskDTO;
import com.grepp.spring.app.model.task.repos.TaskRepository;
import com.grepp.spring.infra.error.exceptions.CommonException;
import com.grepp.spring.infra.response.ResponseCode;
import com.grepp.spring.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ScheduleRepository scheduleRepository;

    public TaskService(final TaskRepository taskRepository,
            final ScheduleRepository scheduleRepository) {
        this.taskRepository = taskRepository;
        this.scheduleRepository = scheduleRepository;
    }

    public List<TaskDTO> findAll() {
        final List<Task> tasks = taskRepository.findAll(Sort.by("id"));
        return tasks.stream()
                .map(task -> mapToDTO(task, new TaskDTO()))
                .toList();
    }

    public TaskDTO get(final Long id) {
        return taskRepository.findById(id)
                .map(task -> mapToDTO(task, new TaskDTO()))
                .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));
    }

    public Long create(final TaskDTO taskDTO) {
        final Task task = new Task();
        mapToEntity(taskDTO, task);
        return taskRepository.save(task).getId();
    }

    public void update(final Long id, final TaskDTO taskDTO) {
        final Task task = taskRepository.findById(id)
                .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));
        mapToEntity(taskDTO, task);
        taskRepository.save(task);
    }

    public void delete(final Long id) {
        taskRepository.deleteById(id);
    }

    private TaskDTO mapToDTO(final Task task, final TaskDTO taskDTO) {
        taskDTO.setId(task.getId());
        taskDTO.setName(task.getName());
        taskDTO.setDescription(task.getDescription());
        taskDTO.setStartAt(task.getStartAt());
        taskDTO.setEndAt(task.getEndAt());
        taskDTO.setSchedule(task.getSchedule() == null ? null : task.getSchedule().getId());
        return taskDTO;
    }

    private Task mapToEntity(final TaskDTO taskDTO, final Task task) {
        task.setName(taskDTO.getName());
        task.setDescription(taskDTO.getDescription());
        task.setStartAt(taskDTO.getStartAt());
        task.setEndAt(taskDTO.getEndAt());
        final Schedule schedule = taskDTO.getSchedule() == null ? null : scheduleRepository.findById(taskDTO.getSchedule())
                .orElseThrow(() -> new NotFoundException("schedule not found"));
        task.setSchedule(schedule);
        return task;
    }

}
