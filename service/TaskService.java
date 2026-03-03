package org.itmo.service;

import org.itmo.domain.Task;
import org.itmo.domain.TaskStatus;
import org.itmo.domain.ChecklistItem;
import org.itmo.validation.TaskValidator;
import org.itmo.validation.ChecklistItemValidator;

import java.util.*;

public class TaskService {
    private Map<Long, Task> tasks = new TreeMap<>();

    private long nextTaskId = 1;
    private long nextChecklistItemId = 1;

    //МЕТОДЫ ДЛЯ ЗАДАЧ

    public Task addTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Задача не может быть null");
        }

        TaskValidator.validate(task);
        task.setId(nextTaskId++);
        tasks.put(task.getId(), task);

        return task;
    }

    public Task getTaskById(long id) {
        return tasks.get(id);
    }

    public Collection<Task> getAllTasks() {
        return tasks.values();
    }

    //Stream API
    public List<Task> getTasksByStatus(TaskStatus status) {
        List<Task> result = new ArrayList<>();
        for (Task task : tasks.values()) {
            if (task.getStatus() == status) {
                result.add(task);
            }
        }
        return result;
    }

    public void updateTask(long id, Task updatedTask) {
        if (!tasks.containsKey(id)) {
            throw new IllegalArgumentException("Задача с id=" + id + " не найдена");
        }

        TaskValidator.validate(updatedTask);
        updatedTask.setId(id);
        tasks.put(id, updatedTask);
    }

    public void deleteTask(long id) {
        if (!tasks.containsKey(id)) {
            throw new IllegalArgumentException("Задача с id=" + id + " не найдена");
        }
        tasks.remove(id);
    }

    //МЕТОДЫ ДЛЯ ЧЕК-ЛИСТОВ

    public ChecklistItem addChecklistItem(long taskId, ChecklistItem item) {
        Task task = tasks.get(taskId);
        if (task == null) {
            throw new IllegalArgumentException("Задача с id=" + taskId + " не найдена");
        }

        item.setTaskId(taskId);
        ChecklistItemValidator.validate(item);
        item.setId(nextChecklistItemId++);
        task.getChecklistItems().add(item);

        return item;
    }

    public void toggleChecklistItem(long taskId, long itemId) {
        Task task = tasks.get(taskId);
        if (task == null) {
            throw new IllegalArgumentException("Задача с id=" + taskId + " не найдена");
        }

        ChecklistItem foundItem = null;
        for (ChecklistItem item : task.getChecklistItems()) {
            if (item.getId() == itemId) {
                foundItem = item;
                break;
            }
        }

        if (foundItem == null) {
            throw new IllegalArgumentException("Пункт с id=" + itemId + " не найден в задаче " + taskId);
        }

        foundItem.setDone(!foundItem.isDone());
    }

    public List<ChecklistItem> getChecklistItems(long taskId) {
        Task task = tasks.get(taskId);
        if (task == null) {
            throw new IllegalArgumentException("Задача с id=" + taskId + " не найдена");
        }
        // TODO unmod. collections
        return new ArrayList<>(task.getChecklistItems());
    }

    public void deleteChecklistItem(long taskId, long itemId) {
        Task task = tasks.get(taskId);
        if (task == null) {
            throw new IllegalArgumentException("Задача с id=" + taskId + " не найдена");
        }

        //TODO lambda
        boolean removed = task.getChecklistItems().removeIf(item -> item.getId() == itemId);

        if (!removed) {
            throw new IllegalArgumentException("Пункт с id=" + itemId + " не найден в задаче " + taskId);
        }
    }
}