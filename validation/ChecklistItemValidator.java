package org.itmo.validation;

import org.itmo.domain.ChecklistItem;

public class ChecklistItemValidator {

    public static void validate(ChecklistItem item) {
        if (item == null) {
            throw new IllegalArgumentException("Пункт чек-листа не может быть null");
        }

        String text = item.getText();
        if (text == null || text.trim().isEmpty()) {
            throw new IllegalArgumentException("Текст пункта не может быть пустым");
        }
        if (text.length() > 256) {
            throw new IllegalArgumentException("Текст пункта слишком длинный (макс. 256 символов)");
        }

        if (item.getTaskId() <= 0) {
            throw new IllegalArgumentException("ID задачи должен быть положительным числом");
        }
    }
}