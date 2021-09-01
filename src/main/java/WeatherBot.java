import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WeatherBot extends TelegramLongPollingBot {
    private static final String TOKEN = //токен бота;
    private static final String USERNAME = //название бота;
    public static boolean sub=false;
    public static Integer us_id=0;

    Model model = new Model();

    DbHandler dbHandler = new DbHandler();
    public List<User> users = null;

    public WeatherBot() throws SQLException {
    }

    public void onUpdateReceived(Update update) {
        users = dbHandler.getAllUsers();
        SendMessage message = new SendMessage();
        System.out.println(update.getMessage().getText());
        String command = update.getMessage().getText();
        Message message_m = update.getEditedMessage();

        for (User user : users) {
            System.out.println(user.toString());
        }

        if (command.equals("/start")) {
            dbHandler.addUser(update.getMessage().getFrom().getId(),update.getMessage().getFrom().getId() ,false,"none");
            message.setText(update.getMessage().getFrom().getFirstName()+" "+update.getMessage().getFrom().getId() + " just joined!");
        }
        else if (command.equals("/unsubscribe")) {
            dbHandler.UnSubscribe(update.getMessage().getFrom().getId(),users);
            message.setText( "you successfully unsubscribed! ");
        }
        else if (command.equals("/subscribe")) {
            message.setText( "write city name");
            sub=true;
            us_id=update.getMessage().getFrom().getId();
        }

        else if ((sub)&&(us_id.equals(update.getMessage().getFrom().getId()))){
            dbHandler.Subscribe(update.getMessage().getFrom().getId(),command,users);
            message.setText("you subscribed for "+command+"!");
            sub=false;
            us_id=0;
        }
        else {
            try {
                message.setText(Weather.getCurWeather(command, model));
            } catch (IOException e) {
                message.setText("City not found");
            }
        }

        message.setChatId(update.getMessage().getChatId());
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public String getBotUsername() {
        return USERNAME;
    }

    public String getBotToken() {
        return TOKEN;
    }

    public void Mailing(Model model){

        ScheduledExecutorService ses =
                Executors.newScheduledThreadPool(1);
        Runnable pinger = () -> {
            SendMessage message = new SendMessage();

            for (User user : users) {
                if (user.sub) {
                    try {
                        message.setText(Weather.getCurWeather(user.city, model));
                        message.setChatId(String.valueOf(user.chat_id));
                        execute(message);

                    } catch (IOException | TelegramApiException e) {
                        e.printStackTrace();
                    }

                }
            }

        };
        ses.scheduleAtFixedRate(pinger, 0, 5, TimeUnit.SECONDS);
    }

}
