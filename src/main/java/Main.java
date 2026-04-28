import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ui.ConsoleMenu;

public class Main {
    private static final Logger logger  = LogManager.getLogger(Main.class);


    public static void main(String[] args) {
        logger.info("Starting program");
        ConsoleMenu menu = new ConsoleMenu();
        menu.start();
    }
}
