import dao.UserDAO;

public class HashTool {
    public static void main(String[] args) {
        String password = "vicePrincipal@123";
        String pass = "admiN@1234";
        String hashed = UserDAO.hashPassword(password);
        String hash = UserDAO.hashPassword(pass);
        System.out.println("Hashed Password: " + hashed);
        System.out.println("Hashed Password: " + hash);
    }
}