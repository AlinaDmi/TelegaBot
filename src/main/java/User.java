import com.sun.org.apache.xpath.internal.operations.Bool;

public class User {
    // Поля класса
    public Integer id;
    public Integer chat_id;
    public Boolean sub;

    public String city;

    public User(Integer id, Integer chat_id, Boolean sub, String city) {
        this.id = id;
        this.sub = sub;
        this.city = city;
        this.chat_id =chat_id;
    }

    @Override
    public String toString() {
        return String.format("ID: %s | Chat id: %s |Подписка: %s | Город: %s |",
                this.id, this.chat_id, this.sub, this.city);
    }
}