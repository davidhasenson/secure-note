package service;

import model.User;
import repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {

    private final UserRepository userRepository = new UserRepository();

    public boolean register(String username, String password) {
        if (username == null || username.isBlank()) {
            System.out.println("Username is null or empty");
            return false;
        }
        if (password == null || password.isBlank()) {
            System.out.println("Password is null or empty");
            return false;
        }

        if (userRepository.existsByUsername(username)) {
            System.out.println("Username already exist");
            return false;
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        return userRepository.saveUser(username, hashedPassword, "USER");
    }

    public User login(String username, String password) {

        if (username == null || username.isBlank()) {
            System.out.println("Username is null or empty");
            return null;
        }
        if (password == null || password.isBlank()) {
            System.out.println("Password is null or empty");
            return null;
        }

        User user = userRepository.findByUsername(username);

        if (user == null) {
            System.out.println("User not found");
            return null;
        }

        boolean match = BCrypt.checkpw(password, user.getPassword());
        if (match) {
            return user;
        }
        System.out.println("Wrong username or password");
        return null;
    }

    public boolean changePassword(User user, String oldPassword, String newPassword1, String newPassword2) {
        if (oldPassword == null || oldPassword.isBlank()) {
            System.out.println("Old password is null or empty");
            return false;
        }
        if (newPassword1 == null || newPassword1.isBlank()) {
            System.out.println("New password is null or empty");
            return false;
        }
        if (!newPassword1.equals(newPassword2)) {
            System.out.println("New passwords do not match");
            return false;
        }
        if (newPassword1.length() < 4) {
            System.out.println("New passwords too short");
            return false;
        }
        //Kontrollera om användaren finns
        User userExists = userRepository.findByUsername(user.getUsername());
        if (userExists == null) {
            System.out.println("User not found");
            return false;
        }
        //kontrollera on oldPasswors stämmer med lösenordet i databasen.
        boolean match = BCrypt.checkpw(oldPassword, userExists.getPassword());
        if (match) {
            String hashedPassword = BCrypt.hashpw(newPassword1, BCrypt.gensalt());
            return userRepository.updatePassword(userExists.getId(), hashedPassword);
        }else {
            System.out.println("Wrong password");
            return false;
        }

    }
    // kontrollera rättigheter i note
}

