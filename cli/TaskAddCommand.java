package org.itmo.cli.commands;

import org.itmo.cli.Command;
import org.itmo.domain.*;
import org.itmo.service.TaskService;
import java.time.Instant;
import java.util.Scanner;

public class TaskAddCommand extends Command {
    
    public TaskAddCommand(TaskService taskService) {
        super(taskService, "task_add", "Создать новую задачу с пошаговым вводом", 1);
    }

    @Override
    public void execute(String[] args, Scanner scanner) {
        System.out.println("--- Режим создания задачи ---");
        
        System.out.print("Введите текст задачи: ");
        String text = scanner.nextLine();

        TaskPriority priority = null;
        while (priority == null) {
            System.out.print("Введите приоритет (LOW, MEDIUM, HIGH): ");
            try {
                String input = scanner.nextLine().trim().toUpperCase();
                priority = TaskPriority.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: Такого приоритета не существует. Попробуйте снова.");
            }
        }

        Task newTask = new Task(
                text,      
                priority,      
                TaskStatus.NEW, 
                null,          
                null,           
                "STUDENT_ITMO" 
        );

        taskService.addTask(newTask);
        System.out.println("Задача успешно создана! Присвоен ID: " + newTask.getId());
    }
}
