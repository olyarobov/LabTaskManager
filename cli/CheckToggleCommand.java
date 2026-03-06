package org.itmo.cli.commands;

import org.itmo.cli.Command;
import org.itmo.service.TaskService;
import java.util.Scanner;

public class CheckToggleCommand extends Command {

    public CheckToggleCommand(TaskService taskService) {
        super(taskService, "check_toggle", "Отметить пункт выполненным/невыполненным", 1);
    }

    @Override
    public void execute(String[] args, Scanner scanner) {
        Long taskId = null;
        while (taskId == null) {
            System.out.print("Введите ID задачи: ");
            try {
                taskId = Long.parseLong(scanner.nextLine().trim());
                taskService.getTaskById(taskId);
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
                taskId = null;
            }
        }

        Long itemId = null;
        while (itemId == null) {
            System.out.print("Введите ID пункта чек-листа: ");
            try {
                itemId = Long.parseLong(scanner.nextLine().trim());
                taskService.toggleChecklistItem(taskId, itemId);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: ID должен быть числом.");
                itemId = null;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                itemId = null;
            }
        }

        System.out.println("Статус пункта #" + itemId + " успешно изменен.");
    }
}
