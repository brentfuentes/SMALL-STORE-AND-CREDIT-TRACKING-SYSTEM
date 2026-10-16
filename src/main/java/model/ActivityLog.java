package model;

public record ActivityLog(int logId, int userId, String username, String userType,
                          String action, String logTime) {
}
