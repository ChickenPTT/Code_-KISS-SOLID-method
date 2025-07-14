import service.TaskManager;

public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        System.out.println("--- BẮT ĐẦU CHƯƠNG TRÌNH ---");
        manager.printAllTasks();

        System.out.println("\n>>> Thêm nhiệm vụ hợp lệ:");
        manager.addNewTask(
                "Học bài Refactor",
                "Hiểu rõ các nguyên tắc KISS, DRY, YAGNI.",
                "2025-07-28",
                "Cao"
        );

        System.out.println("\n>>> Thêm nhiệm vụ trùng lặp:");
        manager.addNewTask(
                "Học bài Refactor",
                "Làm lại ví dụ.",
                "2025-07-28",
                "Trung bình"
        );

        System.out.println("\n>>> Thêm nhiệm vụ với tiêu đề rỗng:");
        manager.addNewTask(
                "",
                "Nhiệm vụ không có tiêu đề.",
                "2025-07-29",
                "Thấp"
        );
        
        System.out.println("\n>>> Thêm nhiệm vụ với ngày không hợp lệ:");
        manager.addNewTask(
                "Họp nhóm",
                "Báo cáo tiến độ.",
                "2025/07/30", // Sai định dạng
                "Cao"
        );

        manager.printAllTasks();
    }
}