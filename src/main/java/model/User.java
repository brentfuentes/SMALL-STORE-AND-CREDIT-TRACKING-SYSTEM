package model;

public record User(int userId, String fullName, String username, String password,
                   String userType, String status) {

    public boolean isAdmin() {
        return userType.equalsIgnoreCase("ADMIN");
    }

    public boolean isSuperAdmin() {
        return userType.equalsIgnoreCase("SUPERADMIN");
    }
}
