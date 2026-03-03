package org.itmo.cli;

import org.itmo.domain.*;
import org.itmo.service.TaskService;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class ConsoleHandler {
    private TaskService taskService;
    private Scanner scanner;
    private boolean isRunning;

    public ConsoleHandler() {
        this.taskService = new TaskService();
        this.scanner = new Scanner(System.in);
        this.isRunning = true;
    }

    public void start() {
        System.out.println("Добро пожаловать в LabTaskManager!");
        System.out.println("Введите 'help' для списка команд.");

        while (isRunning) {
            System.out.print("\n> ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                continue;
            }

            String[] parts = input.split(" ");
            String command = parts[0].toLowerCase();

            // Обработка команд без аргументов
            if (command.equals("help")) {
                printHelp();
            }
            else if (command.equals("exit")) {
                isRunning = false;
                System.out.println("Программа завершена.");
            }
            // Команды с аргументами
            else if (command.equals("task_add")) {
                handleTaskAdd();
            }
            else if (command.equals("task_list")) {
                handleTaskList(parts);
            }
            else if (command.equals("task_show")) {
                handleTaskShow(parts);
            }
            else if (command.equals("task_update")) {
                handleTaskUpdate(parts);
            }
            else if (command.equals("task_done")) {
                handleTaskDone(parts);
            }
            else if (command.equals("task_assign")) {
                handleTaskAssign(parts);
            }
            else if (command.equals("task_delete")) {
                handleTaskDelete(parts);
            }
            else if (command.equals("check_add")) {
                handleCheckAdd(parts);
            }
            else if (command.equals("check_list")) {
                handleCheckList(parts);
            }
            else if (command.equals("check_toggle")) {
                handleCheckToggle(parts);
            }
            else {
                System.out.println("Неизвестная команда. Введите 'help'.");
            }
        }
    }

    private void printHelp() {
        System.out.println("\n=== Доступные команды ===");
        System.out.println("help                        - показать справку");
        System.out.println("exit                        - выход");
        System.out.println("task_add                     - создать задачу");
        System.out.println("task_list [--status СТАТУС] - список задач");
        System.out.println("task_show ID                 - показать задачу");
        System.out.println("task_update ID поле=значение - обновить задачу");
        System.out.println("task_done ID                 - отметить выполненной");
        System.out.println("task_assign ID ИМЯ           - назначить исполнителя");
        System.out.println("task_delete ID               - удалить задачу");
        System.out.println("check_add ID                 - добавить пункт в чек-лист");
        System.out.println("check_list ID                - показать чек-лист");
        System.out.println("check_toggle НОМЕР           - отметить пункт");
    }

    // 1. СОЗДАНИЕ ЗАДАЧИ
    private void handleTaskAdd() {
        System.out.println("Создание новой задачи");

        System.out.print("Текст задачи: ");
        String text = scanner.nextLine().trim();

        System.out.print("Приоритет (LOW/MEDIUM/HIGH): ");
        String priorityStr = scanner.nextLine().trim().toUpperCase();

        // Проверка приоритета
        try {
            TaskPriority priority = TaskPriority.valueOf(priorityStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: приоритет должен быть LOW, MEDIUM или HIGH");
            return;
        }

//        if (priorityStr.equals("LOW")) {
//            priority = TaskPriority.LOW;
//        } else if (priorityStr.equals("MEDIUM")) {
//            priority = TaskPriority.MEDIUM;
//        } else if (priorityStr.equals("HIGH")) {
//            priority = TaskPriority.HIGH;
//        } else {
//            System.out.println("Ошибка: приоритет должен быть LOW, MEDIUM или HIGH");
//            return;
//        }

        System.out.print("Дедлайн (YYYY-MM-DD, можно пропустить): ");
        String deadlineStr = scanner.nextLine().trim();

        Instant deadline = null;
        if (!deadlineStr.isEmpty()) {
            try {
                // Простой разбор даты
                String[] dateParts = deadlineStr.split("-");
                int year = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]);
                int day = Integer.parseInt(dateParts[2]);

                LocalDate date = LocalDate.of(year, month, day);
                deadline = date.atStartOfDay().toInstant(java.time.ZoneOffset.UTC);
            } catch (Exception e) {
                System.out.println("Ошибка: неверный формат даты. Используйте ГГГГ-ММ-ДД");
                return;
            }
        }

        // Создаем задачу
        Task task = new Task(text, priority, TaskStatus.NEW, deadline, null, "SYSTEM");

        try {
            Task created = taskService.addTask(task);
            System.out.println("OK task_id=" + created.getId());
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    // 2. СПИСОК ЗАДАЧ
    private void handleTaskList(String[] parts) {
        TaskStatus filterStatus = null;

        // Проверяем, есть ли status
        if (parts.length >= 3 && parts[1].equals("status")) {
            String statusStr = parts[2].toUpperCase();

            if (statusStr.equals("NEW")) {
                filterStatus = TaskStatus.NEW;
            } else if (statusStr.equals("IN_PROGRESS")) {
                filterStatus = TaskStatus.IN_PROGRESS;
            } else if (statusStr.equals("DONE")) {
                filterStatus = TaskStatus.DONE;
            } else {
                System.out.println("Ошибка: статус должен быть NEW, IN_PROGRESS или DONE");
                return;
            }
        }

        Collection<Task> tasks;
        if (filterStatus != null) {
            tasks = taskService.getTasksByStatus(filterStatus);
        } else {
            tasks = taskService.getAllTasks();
        }

        if (tasks.isEmpty()) {
            System.out.println("Задачи не найдены.");
            return;
        }

        System.out.println("\nID  | Статус       | Приоритет | Текст");

        for (Task task : tasks) {
            String id = String.valueOf(task.getId());
            String status = task.getStatus().toString();
            String priority = task.getPriority().toString();
            String text = task.getText();

            // Обрезаем длинный текст
            if (text.length() > 30) {
                text = text.substring(0, 27) + "...";
            }

            System.out.println(id + "   | " + status + "     | " + priority + "       | " + text);
        }
    }

    // 3. ПОКАЗАТЬ ЗАДАЧУ
    private void handleTaskShow(String[] parts) {
        if (parts.length < 2) {
            System.out.println("Ошибка: укажите ID задачи");
            return;
        }

        try {
            long id = Long.parseLong(parts[1]);
            Task task = taskService.getTaskById(id);

            if (task == null) {
                System.out.println("Ошибка: задача с id=" + id + " не найдена");
                return;
            }

            System.out.println("\nЗадача #" + task.getId());
            System.out.println("Текст: " + task.getText());
            System.out.println("Статус: " + task.getStatus());
            System.out.println("Приоритет: " + task.getPriority());
            System.out.println("Владелец: " + task.getOwnerUsername());

            if (task.getAssigneeUsername() != null) {
                System.out.println("Исполнитель: " + task.getAssigneeUsername());
            }

            if (task.getDeadlineAt() != null) {
                System.out.println("Дедлайн: " + task.getDeadlineAt());
            }

            // Показываем чек-лист, если есть
            List<ChecklistItem> items = task.getChecklistItems();
            if (!items.isEmpty()) {
                System.out.println("\nЧек-лист:");
                for (ChecklistItem item : items) {
                    String check = item.isDone() ? "[X]" : "[ ]";
                    System.out.println("  " + check + " " + item.getId() + ": " + item.getText());
                }
            }

        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID должен быть числом");
        }
    }

    // 4. ОБНОВИТЬ ЗАДАЧУ
    private void handleTaskUpdate(String[] parts) {
        if (parts.length < 3) {
            System.out.println("Ошибка: укажите ID задачи и поле=значение");
            System.out.println("Пример: task_update 1 text=новый_текст");
            return;
        }

        try {
            long id = Long.parseLong(parts[1]);
            Task task = taskService.getTaskById(id);

            if (task == null) {
                System.out.println("Ошибка: задача с id=" + id + " не найдена");
                return;
            }

            // Разбираем поле=значение
            String[] pair = parts[2].split("=");
            if (pair.length != 2) {
                System.out.println("Ошибка: используйте формат поле=значение");
                return;
            }

            String field = pair[0].toLowerCase();
            String value = pair[1];

            if (field.equals("text")) {
                task.setText(value);
                System.out.println("Текст обновлен");
            }
            else if (field.equals("priority")) {
                String upValue = value.toUpperCase();
                if (upValue.equals("LOW")) {
                    task.setPriority(TaskPriority.LOW);
                    System.out.println("Приоритет обновлен");
                } else if (upValue.equals("MEDIUM")) {
                    task.setPriority(TaskPriority.MEDIUM);
                    System.out.println("Приоритет обновлен");
                } else if (upValue.equals("HIGH")) {
                    task.setPriority(TaskPriority.HIGH);
                    System.out.println("Приоритет обновлен");
                } else {
                    System.out.println("Ошибка: приоритет должен быть LOW, MEDIUM или HIGH");
                    return;
                }
            }
            else if (field.equals("status")) {
                String upValue = value.toUpperCase();
                if (upValue.equals("NEW")) {
                    task.setStatus(TaskStatus.NEW);
                    System.out.println("Статус обновлен");
                } else if (upValue.equals("IN_PROGRESS")) {
                    task.setStatus(TaskStatus.IN_PROGRESS);
                    System.out.println("Статус обновлен");
                } else if (upValue.equals("DONE")) {
                    task.setStatus(TaskStatus.DONE);
                    System.out.println("Статус обновлен");
                } else {
                    System.out.println("Ошибка: статус должен быть NEW, IN_PROGRESS или DONE");
                    return;
                }
            }
            else {
                System.out.println("Ошибка: можно менять только text, priority, status");
                return;
            }

            taskService.updateTask(id, task);
            System.out.println("OK задача " + id + " обновлена");

        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID должен быть числом");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    // 5. ОТМЕТИТЬ ВЫПОЛНЕННОЙ
    private void handleTaskDone(String[] parts) {
        if (parts.length < 2) {
            System.out.println("Ошибка: укажите ID задачи");
            return;
        }

        try {
            long id = Long.parseLong(parts[1]);
            Task task = taskService.getTaskById(id);

            if (task == null) {
                System.out.println("Ошибка: задача с id=" + id + " не найдена");
                return;
            }

            task.setStatus(TaskStatus.DONE);
            taskService.updateTask(id, task);
            System.out.println("OK задача " + id + " отмечена как выполненная");

        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID должен быть числом");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    // 6. НАЗНАЧИТЬ ИСПОЛНИТЕЛЯ
    private void handleTaskAssign(String[] parts) {
        if (parts.length < 3) {
            System.out.println("Ошибка: укажите ID задачи и имя пользователя");
            return;
        }

        try {
            long id = Long.parseLong(parts[1]);
            String username = parts[2];

            Task task = taskService.getTaskById(id);
            if (task == null) {
                System.out.println("Ошибка: задача с id=" + id + " не найдена");
                return;
            }

            task.setAssigneeUsername(username);
            taskService.updateTask(id, task);
            System.out.println("OK задача " + id + " назначена на " + username);

        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID должен быть числом");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    // 7. УДАЛИТЬ ЗАДАЧУ
    private void handleTaskDelete(String[] parts) {
        if (parts.length < 2) {
            System.out.println("Ошибка: укажите ID задачи");
            return;
        }

        try {
            long id = Long.parseLong(parts[1]);
            taskService.deleteTask(id);
            System.out.println("OK задача " + id + " удалена");

        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID должен быть числом");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    // 8. ДОБАВИТЬ ПУНКТ В ЧЕК-ЛИСТ
    private void handleCheckAdd(String[] parts) {
        if (parts.length < 2) {
            System.out.println("Ошибка: укажите ID задачи");
            return;
        }

        try {
            long taskId = Long.parseLong(parts[1]);

            System.out.print("Текст пункта: ");
            String text = scanner.nextLine().trim();

            ChecklistItem item = new ChecklistItem(taskId, text);
            ChecklistItem created = taskService.addChecklistItem(taskId, item);
            System.out.println("OK item_id=" + created.getId());

        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID должен быть числом");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    // 9. ПОКАЗАТЬ ЧЕК-ЛИСТ
    private void handleCheckList(String[] parts) {
        if (parts.length < 2) {
            System.out.println("Ошибка: укажите ID задачи");
            return;
        }

        try {
            long taskId = Long.parseLong(parts[1]);
            List<ChecklistItem> items = taskService.getChecklistItems(taskId);

            if (items.isEmpty()) {
                System.out.println("Чек-лист пуст");
                return;
            }

            System.out.println("\nЧек-лист для задачи #" + taskId + ":");
            for (ChecklistItem item : items) {
                String check = item.isDone() ? "[X]" : "[ ]";
                System.out.println("  " + check + " " + item.getId() + ": " + item.getText());
            }

        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID должен быть числом");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    // 10. ПЕРЕКЛЮЧИТЬ ПУНКТ ЧЕК-ЛИСТА
    private void handleCheckToggle(String[] parts) {
        if (parts.length < 2) {
            System.out.println("Ошибка: укажите ID пункта");
            return;
        }

        try {
            long itemId = Long.parseLong(parts[1]);

            // Ищем пункт во всех задачах
            boolean found = false;
            for (Task task : taskService.getAllTasks()) {
                for (ChecklistItem item : task.getChecklistItems()) {
                    if (item.getId() == itemId) {
                        taskService.toggleChecklistItem(task.getId(), itemId);
                        System.out.println("OK пункт " + itemId + " переключен");
                        found = true;
                        break;
                    }
                }
                if (found) break;
            }

            if (!found) {
                System.out.println("Ошибка: пункт с id=" + itemId + " не найден");
            }

        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID должен быть числом");
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new ConsoleHandler().start();
    }
}