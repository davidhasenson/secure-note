package service;

import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repository.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {

    private final UserRepository userRepository = new UserRepository();
    private static final Logger logger = LogManager.getLogger(AuthService.class);

    public boolean register(String username, String password) {
        logger.info("Registering user");
        if (username == null || username.isBlank()) {
            logger.warn("Username is null or empty");
            return false;
        }
        if (password == null || password.isBlank()) {
            logger.warn("Password is null or empty");
            return false;
        }
        if (userRepository.existsByUsername(username)) {
            logger.warn("Username already exist");
            return false;
        }
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        return userRepository.saveUser(username, hashedPassword, "USER");
    }

    public User login(String username, String password) {
        logger.info("Logging in");
        if (username == null || username.isBlank()) {
            logger.warn("Username is null or empty");
            return null;
        }
        if (password == null || password.isBlank()) {
            logger.warn("Password is null or empty");
            return null;
        }
        User user = userRepository.findByUsername(username);
        if (user == null) {
            logger.warn("User not found");
            return null;
        }
        boolean match = BCrypt.checkpw(password, user.getPassword());
        if (match) {
            return user;
        }
        System.out.println("Wrong username or password");
        logger.info("Wrong username or password");
        return null;
    }

    public boolean changePassword(User user, String oldPassword, String newPassword1, String newPassword2) {
        logger.info("Change password");
        if (oldPassword == null || oldPassword.isBlank()) {
            logger.warn("Old password is null or empty");
            return false;
        }
        if (newPassword1 == null || newPassword1.isBlank()) {
            logger.warn("New password is null or empty");
            return false;
        }
        if (!newPassword1.equals(newPassword2)) {
            logger.warn("New passwords do not match");
            return false;
        }
//        if (newPassword1.length() < 8) {
//            logger.warn("New passwords too short");
//            return false;
//        }
        User userExists = userRepository.findByUsername(user.getUsername());
        if (userExists == null) {
            System.out.println("User not found");
            logger.warn("User not found");
            return false;
        }
        boolean match = BCrypt.checkpw(oldPassword, userExists.getPassword());
        if (match) {
            String hashedPassword = BCrypt.hashpw(newPassword1, BCrypt.gensalt());
            return userRepository.updatePassword(userExists.getId(), hashedPassword);
        } else {
            System.out.println("Wrong password");
            logger.info("Wrong password");
            return false;
        }
    }
}

