import model.Document;
import service.Index;
import service.query.Query;

public class Test {
    public static void main(String... args) {
        Index index = new Index();

        Document doc = index.getDocumentFromLink("https://en.wikipedia.org/wiki/Nothing");
        Document doc3 = index.getDocumentFromLink("https://en.wikipedia.org/wiki/Nothing");
        Document doc4 = index.getDocumentFromLink("https://en.wikipedia.org/wiki/Nothing");
        Document doc5 = index.getDocumentFromLink("https://en.wikipedia.org/wiki/Nothing");
        System.out.println(index.processQuery("nothing"));
    }
}
