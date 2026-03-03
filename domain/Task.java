package org.itmo.domain;

import java.time.Instant;
import java.util.List;
import java.util.ArrayList;

public class Task {
    private long id;
    private String text;
    private TaskPriority priority;
    private TaskStatus status;
    private Instant deadlineAt;           // может быть null
    private String assigneeUsername;      // может быть null
    private String ownerUsername;
    private Instant createdAt;
    private Instant updatedAt;

    // Дополнительное поле для хранения пунктов чек-листа
    private List<ChecklistItem> checklistItems;

    // Конструктор
    public Task(String text, TaskPriority priority, TaskStatus status,
                Instant deadlineAt, String assigneeUsername, String ownerUsername) {
        this.text = text;
        this.priority = priority;
        this.status = status;
        this.deadlineAt = deadlineAt;
        this.assigneeUsername = assigneeUsername;
        this.ownerUsername = ownerUsername;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.checklistItems = new ArrayList<>();
    }

    // Геттеры и сеттеры для всех полей
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        this.updatedAt = Instant.now();   // обновляем время при изменении
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
        this.updatedAt = Instant.now();
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
        this.updatedAt = Instant.now();
    }

    public Instant getDeadlineAt() {
        return deadlineAt;
    }

    public void setDeadlineAt(Instant deadlineAt) {
        this.deadlineAt = deadlineAt;
        this.updatedAt = Instant.now();
    }

    public String getAssigneeUsername() {
        return assigneeUsername;
    }

    public void setAssigneeUsername(String assigneeUsername) {
        this.assigneeUsername = assigneeUsername;
        this.updatedAt = Instant.now();
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
        this.updatedAt = Instant.now();
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<ChecklistItem> getChecklistItems() {
        return checklistItems;
    }

    public void setChecklistItems(List<ChecklistItem> checklistItems) {
        this.checklistItems = checklistItems;
        this.updatedAt = Instant.now();
    }
}