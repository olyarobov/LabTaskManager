package org.itmo.cli.commands;

import org.itmo.cli.Command;
import org.itmo.domain.Task;
import org.itmo.domain.TaskStatus;
import org.itmo.service.TaskService;
import java.util.Scanner;

public class TaskUpdateCommand extends Command {

    public TaskUpdateCommand(TaskService taskService) {
        super(taskService, "task_update", "Обновить статус задачи по ID", 1);
    }

    @Override
    public void execute(String[] args, Scanner scanner) {
        Task task = null;

        // Цикл для поиска задачи по ID
        while (task == null) {
            System.out.print("Введите ID задачи для обновления: ");
            String idInput = scanner.nextLine().trim();
            try {
                long id = Long.parseLong(idInput);
              
                task = taskService.getTaskById(id);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: ID должен быть целым числом!");
            } catch (IllegalArgumentException e) {

                System.out.println(e.getMessage());
            }
        }

        // Цикл для ввода нового статуса
        TaskStatus newStatus = null;
        while (newStatus == null) {
            System.out.print("Введите новый статус (NEW, IN_PROGRESS, DONE): ");
            try {
                String statusInput = scanner.nextLine().trim().toUpperCase();
                newStatus = TaskStatus.valueOf(statusInput);
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: Неверный статус. Используйте: NEW, IN_PROGRESS или DONE.");
            }
        }

        task.setStatus(newStatus);
        taskService.updateTask(task.getId(), task);
        
        System.out.println("Статус задачи успешно обновлен!");
    }
}
