import io.javalin.Javalin;

public class server {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start();
    }
}
