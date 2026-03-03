package org.itmo.validation;

import org.itmo.domain.Task;

public class TaskValidator {

    public static void validate(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Задача не может быть null");
        }

        String text = task.getText();
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Текст задачи не может быть пустым");
        }
        if (text.length() > 256) {
            throw new IllegalArgumentException("Текст задачи слишком длинный (макс. 256 символов)");
        }

        if (task.getPriority() == null) {
            throw new IllegalArgumentException("Приоритет должен быть указан");
        }

        if (task.getStatus() == null) {
            throw new IllegalArgumentException("Статус должен быть указан");
        }

        String owner = task.getOwnerUsername();
        if (owner == null || owner.trim().isEmpty()) {
            throw new IllegalArgumentException("Владелец задачи не может быть пустым");
        }

    }
}