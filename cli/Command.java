package org.itmo.cli;

import org.itmo.service.TaskService;
import java.util.Scanner;

public abstract class Command {
    protected final TaskService taskService;
    private final String name;
    private final String description;
    private final int minArgs; 
    public Command(TaskService taskService, String name, String description, int minArgs) {
        this.taskService = taskService;
        this.name = name;
        this.description = description;
        this.minArgs = minArgs;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }

    public void validateArgs(String[] args) {
        if (args.length < minArgs) {
            throw new IllegalArgumentException("Ошибка: Команде '" + name + "' недостаточно данных.");
        }
    }

    // Основной метод, который переопределяют дочерние классы
    public abstract void execute(String[] args, Scanner scanner);
}
