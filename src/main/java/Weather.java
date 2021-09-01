import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


import java.net.URL;
import java.util.Scanner;
public class Weather {
    public static String getCurWeather(String city, Model model) throws IOException {
        String APPID = "&units=metric&APPID=0e5487b87b2c44ca076134725a5c7360";
        String API_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
        URL url = new URL(API_URL + city + APPID);
        Scanner in = new Scanner((InputStream) url.getContent());
        String result = "";
        while (in.hasNext()) {
            result += in.nextLine();
        }
        System.out.println(result);

        JSONObject object = new JSONObject(result);
        model.setName(object.getString("name"));

        JSONObject main = object.getJSONObject("main");
        model.setTemp(main.getDouble("temp"));

        JSONArray getArray = object.getJSONArray("weather");
        for (int i = 0; i < getArray.length(); i++) {
            JSONObject obj = getArray.getJSONObject(i);
            model.setIcon((String) obj.get("icon"));
            model.setDescription((String) obj.get("description"));
            model.setWeather((String) obj.get("main"));
        }


        return "City: " + model.getName() +"\nTemperature: "+model.getTemp()+
                "°C\nWeather: "+model.getWeather()+" - "+model.getDescription()+Icon(model.getIcon());
    }
    public static String Icon(String icon){
        String curic = "";
        switch (icon) {
            case "01d":
            case "01n":
                curic = "☀";
                break;
            case "02d":
            case "02n":
                curic = "⛅";
                break;
            case "03d":
            case "03n":
                curic = "☁";
                break;
            case "04d":
            case "04n":
                curic = "☁☁";
                break;
            case "09d":
            case "09n":
                curic = "\uD83C\uDF27";
                break;
            case "10d":
            case "10n":
                curic = "\uD83C\uDF26";
                break;
            case "11d":
            case "11n":
                curic = "\uD83C\uDF29";
                break;
            case "13d":
            case "13n":
                curic = "❄";
                break;
            case "50d":
            case "50n":
                curic = "\uD83C\uDF2B";
                break;
        }
        return curic;
    }
}
