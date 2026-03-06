package org.itmo.cli.commands;

import org.itmo.cli.Command;
import org.itmo.domain.ChecklistItem;
import org.itmo.service.TaskService;
import java.util.Scanner;

public class CheckAddCommand extends Command {

    public CheckAddCommand(TaskService taskService) {
        super(taskService, "check_add", "Добавить пункт в чек-лист задачи", 1);
    }

    @Override
    public void execute(String[] args, Scanner scanner) {
        Long taskId = null;
        while (taskId == null) {
            System.out.print("Введите ID задачи, для которой создаем пункт: ");
            try {
                taskId = Long.parseLong(scanner.nextLine().trim());
                taskService.getTaskById(taskId); // Проверка на существование задачи
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
                taskId = null;
            }
        }

        System.out.print("Введите текст пункта: ");
        String text = scanner.nextLine().trim();

        taskService.addChecklistItem(taskId, new ChecklistItem(taskId, text));
        System.out.println("Пункт добавлен в чек-лист задачи #" + taskId);
    }
}
