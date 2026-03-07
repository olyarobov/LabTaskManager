package org.itmo.cli.commands;

import org.itmo.cli.Command;
import org.itmo.domain.Task;
import org.itmo.service.TaskService;
import java.util.Collection;
import java.util.Scanner;

public class TaskListCommand extends Command {

    public TaskListCommand(TaskService taskService) {
        super(taskService, "task_list", "Показать все существующие задачи", 1);
    }

    @Override
    public void execute(String[] args, Scanner scanner) {
        Collection<Task> tasks = taskService.getAllTasks();

        if (tasks.isEmpty()) {
            System.out.println("Список задач пока пуст.");
        }

        System.out.println("Список задач");
        for (Task task : tasks) {
    System.out.printf("[%d] %s | Приоритет: %s | Статус: %s%n",
            task.getId(),
            task.getText(),
            task.getPriority(),
            task.getStatus());
    
    // Вывод подзадач (пунктов чек-листа)
    List<ChecklistItem> items = task.getChecklistItems();
    if (!items.isEmpty()) {
        for (ChecklistItem item : items) {
            String statusChar = item.isDone() ? "[x]" : "[ ]";
            System.out.printf("    %s %d: %s%n", statusChar, item.getId(), item.getText());
        }
    }
}
    }
}
