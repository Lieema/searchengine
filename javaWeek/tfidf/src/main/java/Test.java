import model.Document;
import service.indexService.DefaultIndexerService;
import service.indexService.Indexer;

public class Test {
    public static void main(String... args) {
        Indexer indexer = new DefaultIndexerService();

        Document doc = indexer.getDocumentFromLink("https://en.wikipedia.org/wiki/Rabbit");
        Document doc3 = indexer.getDocumentFromLink("https://en.wikipedia.org/wiki/Hare");
        Document doc4 = indexer.getDocumentFromLink("https://en.wikipedia.org/wiki/Forest");
        Document doc5 = indexer.getDocumentFromLink("https://en.wikipedia.org/wiki/Planet");
        Document doc6 = indexer.getDocumentFromLink("https://en.wikipedia.org/wiki/Nothing");
        Document doc7 = indexer.getDocumentFromLink("https://en.wikipedia.org/wiki/Water");
        System.out.println(indexer.processQuery("hare rabbit water car hare hare"));
    }
}
