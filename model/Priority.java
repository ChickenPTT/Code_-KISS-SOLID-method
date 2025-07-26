package model;

public enum Priority {
    THAP("Thấp"),
    TRUNG_BINH("Trung bình"),
    CAO("Cao");

    private final String displayName;

    Priority(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Priority fromString(String text) {
        for (Priority b : Priority.values()) {
            if (b.displayName.equalsIgnoreCase(text)) {
                return b;
            }
        }
        // Trả về null hoặc ném ra một ngoại lệ nếu không tìm thấy
        throw new IllegalArgumentException("Không tìm thấy mức độ ưu tiên: " + text);
    }
}