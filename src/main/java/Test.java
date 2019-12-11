import model.Document;
import service.Index;

public class Test {
    public static void main(String... args) {
        Index index = new Index();

        Document doc = index.getDocumentFromLink("https://en.wikipedia.org/wiki/Nothing");
        doc.getText();
    }
}
