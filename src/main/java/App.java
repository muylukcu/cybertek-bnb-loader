public class App {
    public static void main(String[] args) {
        String url = ConfigurationReader.getProperty("url");
        String sheetPath = ConfigurationReader.getProperty("sheet-path");
        int batchNumber = Integer.parseInt(ConfigurationReader.getProperty("batch-number"));
        BatchLoader loader = new BatchLoader(batchNumber, url);
        loader.load(sheetPath);
    }
}
