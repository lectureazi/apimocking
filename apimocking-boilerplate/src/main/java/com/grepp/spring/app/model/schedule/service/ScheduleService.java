package com.grepp.spring.app.model.schedule.service;

import com.grepp.spring.app.model.schedule.domain.Schedule;
import com.grepp.spring.app.model.schedule.domain.Schedule;
import com.grepp.spring.app.model.schedule.model.ScheduleDTO;
import com.grepp.spring.app.model.schedule.repos.ScheduleRepository;
import com.grepp.spring.app.model.task.domain.Task;
import com.grepp.spring.app.model.task.repos.TaskRepository;
import com.grepp.spring.infra.error.exceptions.CommonException;
import com.grepp.spring.infra.response.ResponseCode;
import com.grepp.spring.util.NotFoundException;
import com.grepp.spring.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final TaskRepository taskRepository;

    public ScheduleService(final ScheduleRepository scheduleRepository,
            final TaskRepository taskRepository) {
        this.scheduleRepository = scheduleRepository;
        this.taskRepository = taskRepository;
    }

    public List<ScheduleDTO> findAll() {
        final List<Schedule> schedules = scheduleRepository.findAll(Sort.by("id"));
        return schedules.stream()
                .map(schedule -> mapToDTO(schedule, new ScheduleDTO()))
                .toList();
    }

    public ScheduleDTO get(final Long id) {
        return scheduleRepository.findById(id)
                .map(schedule -> mapToDTO(schedule, new ScheduleDTO()))
                .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));
    }

    public Long create(final ScheduleDTO scheduleDTO) {
        final Schedule schedule = new Schedule();
        mapToEntity(scheduleDTO, schedule);
        return scheduleRepository.save(schedule).getId();
    }

    public void update(final Long id, final ScheduleDTO scheduleDTO) {
        final Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));
        mapToEntity(scheduleDTO, schedule);
        scheduleRepository.save(schedule);
    }

    public void delete(final Long id) {
        scheduleRepository.deleteById(id);
    }

    private ScheduleDTO mapToDTO(final Schedule schedule, final ScheduleDTO scheduleDTO) {
        scheduleDTO.setId(schedule.getId());
        scheduleDTO.setName(schedule.getName());
        scheduleDTO.setUsername(schedule.getUsername());
        scheduleDTO.setStartAt(schedule.getStartAt());
        scheduleDTO.setEndAt(schedule.getEndAt());
        return scheduleDTO;
    }

    private Schedule mapToEntity(final ScheduleDTO scheduleDTO, final Schedule schedule) {
        schedule.setName(scheduleDTO.getName());
        schedule.setUsername(scheduleDTO.getUsername());
        schedule.setStartAt(scheduleDTO.getStartAt());
        schedule.setEndAt(scheduleDTO.getEndAt());
        return schedule;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new CommonException(ResponseCode.NOT_FOUND));
        final Task scheduleTask = taskRepository.findFirstBySchedule(schedule);
        if (scheduleTask != null) {
            referencedWarning.setKey("schedule.task.schedule.referenced");
            referencedWarning.addParam(scheduleTask.getId());
            return referencedWarning;
        }
        return null;
    }

}
