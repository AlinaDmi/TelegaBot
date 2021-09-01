import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class Main {
    public static void main(String[] args) throws IOException, SQLException, InterruptedException {
        Model model = new Model();
        DbHandler dbHandler = null;
        try {
            dbHandler = DbHandler.getInstance();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        assert dbHandler != null;
        List<User> users = dbHandler.getAllUsers();

        ApiContextInitializer.init();

        TelegramBotsApi botsApi = new TelegramBotsApi();

        WeatherBot weatherBot = new WeatherBot();
        try {
            botsApi.registerBot(weatherBot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        weatherBot.users = users;
        weatherBot.Mailing(model);

    }
}