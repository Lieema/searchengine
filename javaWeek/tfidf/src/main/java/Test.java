import model.Document;
import service.Index;
import service.query.Query;

public class Test {
    public static void main(String... args) {
        Index index = new Index();

        Document doc = index.getDocumentFromLink("https://en.wikipedia.org/wiki/Rabbit");
        Document doc3 = index.getDocumentFromLink("https://en.wikipedia.org/wiki/Hare");
        Document doc4 = index.getDocumentFromLink("https://en.wikipedia.org/wiki/Forest");
        Document doc5 = index.getDocumentFromLink("https://en.wikipedia.org/wiki/Planet");
        Document doc6 = index.getDocumentFromLink("https://en.wikipedia.org/wiki/Nothing");
        Document doc7 = index.getDocumentFromLink("https://en.wikipedia.org/wiki/Water");
        System.out.println(index.processQuery("hare rabbit water car"));
    }
}
