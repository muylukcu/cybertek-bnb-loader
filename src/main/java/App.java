public class App {
    public static void main(String[] args) {
        String url = "http://localhost:4567";
        String filePath = "src/main/resources/MOCK_DATA.xlsx";
        int batchNumber = 11;
        BatchLoader loader = new BatchLoader(batchNumber, url);
        loader.load(filePath);
    }
}
