package app;

import utils.ConfigurationReader;

public class App {
    public static void main(String[] args) {
        String url = ConfigurationReader.getProperty("url");
        String sheetPath = ConfigurationReader.getProperty("sheet-path");
        int batchNumber = Integer.parseInt(ConfigurationReader.getProperty("batch-number"));
        String location = ConfigurationReader.getProperty("location");
        BatchLoader loader = new BatchLoader(batchNumber, location, url);
        loader.load(sheetPath);
    }
}
