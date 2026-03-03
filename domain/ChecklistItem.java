package org.itmo.domain;

import java.time.Instant;

public class ChecklistItem {
    private long id;
    private long taskId;           // к какой задаче относится
    private String text;
    private boolean done;
    private Instant createdAt;
    private Instant updatedAt;

    // Конструктор
    public ChecklistItem(long taskId, String text) {
        this.taskId = taskId;
        this.text = text;
        this.done = false;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    // Геттеры и сеттеры
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
        this.updatedAt = Instant.now();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        this.updatedAt = Instant.now();
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
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
}