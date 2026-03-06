package org.itmo.cli.commands;

import org.itmo.cli.Command;
import org.itmo.service.TaskService;
import java.util.Scanner;

public class TaskDeleteCommand extends Command {

    public TaskDeleteCommand(TaskService taskService) {
        super(taskService, "task_delete", "Удалить задачу по ID", 1);
    }

    @Override
    public void execute(String[] args, Scanner scanner) {
        Long id = null;
        while (id == null) {
            System.out.print("Введите ID задачи для удаления: ");
            try {
                id = Long.parseLong(scanner.nextLine().trim());
                taskService.getTaskById(id);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: Введите числовой ID.");
                id = null;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                id = null;
            }
        }

        taskService.deleteTask(id);
        System.out.println("Задача #" + id + " успешно удалена.");
    }
}
