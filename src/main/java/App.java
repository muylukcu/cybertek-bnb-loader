public class App {
    public static void main(String[] args) {
        String url = "http://localhost:4567";
        String filePath = "src/main/resources/MOCK_DATA.xlsx";
        BatchLoader loader = new BatchLoader(7, url);
        loader.load(filePath);
    }
}
