package org.itmo.cli.commands;

import org.itmo.domain.Task;
import org.itmo.domain.TaskPriority;
import org.itmo.domain.TaskStatus;
import org.itmo.service.TaskService;
import java.util.Scanner;

public class TaskUpdateCommand extends Command {

    public TaskUpdateCommand(TaskService taskService) {
        super(taskService, "task_update", "Редактировать задачу (текст, приоритет, статус)", 1);
    }

    @Override
    public void execute(String[] args, Scanner scanner) {
        System.out.print("Введите ID задачи для редактирования: ");
        String idInput = scanner.nextLine().trim();
        long id;
        try {
            id = Long.parseLong(idInput);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID должен быть числом.");
            return;
        }

        Task task = taskService.getTaskById(id);
        if (task == null) {
            System.out.println("Ошибка: Задача с таким ID не найдена.");
            return;
        }

        System.out.println("Редактирование задачи #" + id + " (оставьте поле пустым, чтобы не менять)");

        // 1. Редактируем текст
        System.out.print("Новый текст [" + task.getText() + "]: ");
        String newText = scanner.nextLine().trim();
        if (!newText.isEmpty()) {
            task.setText(newText);
        }

        // 2. Редактируем приоритет
        System.out.print("Новый приоритет (LOW, MEDIUM, HIGH) [" + task.getPriority() + "]: ");
        String priorityInput = scanner.nextLine().trim().toUpperCase();
        if (!priorityInput.isEmpty()) {
            try {
                task.setPriority(TaskPriority.valueOf(priorityInput));
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: Неверный приоритет. Оставлен прежний.");
            }
        }

        // 3. Редактируем статус
        System.out.print("Новый статус (NEW, IN_PROGRESS, DONE) [" + task.getStatus() + "]: ");
        String statusInput = scanner.nextLine().trim().toUpperCase();
        if (!statusInput.isEmpty()) {
            try {
                task.setStatus(TaskStatus.valueOf(statusInput));
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: Неверный статус. Оставлен прежний.");
            }
        }

        taskService.updateTask(id, task);
        System.out.println("Задача успешно обновлена!");
    }
}
