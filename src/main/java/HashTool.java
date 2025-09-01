import dao.UserDAO;

public class HashTool {
    public static void main(String[] args) {
        String password = "viceprincipal123";
        String pass = "admin123";
        String hashed = UserDAO.hashPassword(password);
        String hash = UserDAO.hashPassword(pass);
        System.out.println("Hashed Password: " + hashed);
        System.out.println("Hashed Password: " + hash);
    }
}