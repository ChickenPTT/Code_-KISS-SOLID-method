package service;

import model.Priority;
import model.Task;
import repository.TaskRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class TaskManager {
    private final TaskRepository repository;
    private List<Task> tasks;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String STATUS_DEFAULT = "Chưa hoàn thành";

    public TaskManager() {
        this.repository = new TaskRepository();
        this.tasks = this.repository.loadAllTasks();
    }

    public Task addNewTask(String title, String description, String dueDateStr, String priorityStr) {
        try {
            // Bước 1: Validate và chuyển đổi đầu vào
            validateInput(title, dueDateStr, priorityStr);
            LocalDate dueDate = LocalDate.parse(dueDateStr, DATE_FORMATTER);
            Priority priority = Priority.fromString(priorityStr);

            // Bước 2: Kiểm tra logic nghiệp vụ
            checkForDuplicates(title, dueDate);

            // Bước 3: Tạo đối tượng Task mới
            Task newTask = createTask(title, description, dueDate, priority);

            // Bước 4: Cập nhật và lưu trữ
            this.tasks.add(newTask);
            this.repository.saveAllTasks(this.tasks);

            System.out.println("Đã thêm nhiệm vụ mới thành công: " + newTask.getTitle());
            return newTask;
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.err.println("Lỗi khi thêm nhiệm vụ: " + e.getMessage());
            return null;
        }
    }

    private void validateInput(String title, String dueDateStr, String priorityStr) throws IllegalArgumentException {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Tiêu đề không được để trống.");
        }
        try {
            LocalDate.parse(dueDateStr, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Ngày đến hạn không hợp lệ. Vui lòng sử dụng định dạng YYYY-MM-DD.");
        }
        // Việc kiểm tra priority được xử lý bởi Priority.fromString(), nếu sai sẽ ném ra exception
        Priority.fromString(priorityStr);
    }

    private void checkForDuplicates(String title, LocalDate dueDate) throws IllegalStateException {
        for (Task existingTask : tasks) {
            if (existingTask.getTitle().equalsIgnoreCase(title) && existingTask.getDueDate().equals(dueDate)) {
                throw new IllegalStateException(String.format("Nhiệm vụ '%s' đã tồn tại với cùng ngày đến hạn.", title));
            }
        }
    }

    private Task createTask(String title, String description, LocalDate dueDate, Priority priority) {
        Task newTask = new Task();
        // ID đơn giản hơn, dựa trên kích thước danh sách hiện tại
        newTask.setId(String.valueOf(tasks.size() + 1));
        newTask.setTitle(title);
        newTask.setDescription(description);
        newTask.setDueDate(dueDate);
        newTask.setPriority(priority);
        newTask.setStatus(STATUS_DEFAULT);
        LocalDateTime now = LocalDateTime.now();
        newTask.setCreatedAt(now);
        newTask.setLastUpdatedAt(now);
        return newTask;
    }

    public void printAllTasks() {
        System.out.println("\n--- DANH SÁCH NHIỆM VỤ HIỆN TẠI ---");
        if (tasks.isEmpty()) {
            System.out.println("Không có nhiệm vụ nào.");
        } else {
            for (Task task : tasks) {
                System.out.println(task);
            }
        }
        System.out.println("------------------------------------");
    }
}