package repository;

import model.Priority;
import model.Task;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TaskRepository {
    private static final String DB_FILE_PATH = "tasks_refactored.json";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    public List<Task> loadAllTasks() {
        List<Task> tasks = new ArrayList<>();
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(DB_FILE_PATH)) {
            Object obj = parser.parse(reader);
            JSONArray taskList = (JSONArray) obj;

            for (Object taskObj : taskList) {
                tasks.add(parseTaskObject((JSONObject) taskObj));
            }
        } catch (IOException | ParseException e) {
            // Nếu file không tồn tại hoặc rỗng, trả về danh sách trống, đây không phải là lỗi
            System.err.println("Không thể tải file database (có thể do file chưa tồn tại): " + e.getMessage());
        }
        return tasks;
    }

    public void saveAllTasks(List<Task> tasks) {
        JSONArray taskList = new JSONArray();
        for (Task task : tasks) {
            taskList.add(createTaskJson(task));
        }

        try (FileWriter file = new FileWriter(DB_FILE_PATH)) {
            file.write(taskList.toJSONString());
            file.flush();
        } catch (IOException e) {
            System.err.println("Lỗi khi ghi vào file database: " + e.getMessage());
        }
    }

    private Task parseTaskObject(JSONObject jsonObject) {
        Task task = new Task();
        task.setId((String) jsonObject.get("id"));
        task.setTitle((String) jsonObject.get("title"));
        task.setDescription((String) jsonObject.get("description"));
        task.setDueDate(LocalDate.parse((String) jsonObject.get("due_date"), DATE_FORMATTER));
        task.setPriority(Priority.fromString((String) jsonObject.get("priority")));
        task.setStatus((String) jsonObject.get("status"));
        task.setCreatedAt(LocalDateTime.parse((String) jsonObject.get("created_at"), DATETIME_FORMATTER));
        task.setLastUpdatedAt(LocalDateTime.parse((String) jsonObject.get("last_updated_at"), DATETIME_FORMATTER));
        return task;
    }

    private JSONObject createTaskJson(Task task) {
        JSONObject taskDetails = new JSONObject();
        taskDetails.put("id", task.getId());
        taskDetails.put("title", task.getTitle());
        taskDetails.put("description", task.getDescription());
        taskDetails.put("due_date", task.getDueDate().format(DATE_FORMATTER));
        taskDetails.put("priority", task.getPriority().getDisplayName());
        taskDetails.put("status", task.getStatus());
        taskDetails.put("created_at", task.getCreatedAt().format(DATETIME_FORMATTER));
        taskDetails.put("last_updated_at", task.getLastUpdatedAt().format(DATETIME_FORMATTER));
        return taskDetails;
    }
}