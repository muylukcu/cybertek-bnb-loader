package app;

import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import utils.ConfigurationReader;

import static io.restassured.RestAssured.*;
import static java.lang.String.format;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class BatchLoader {
    public static final Logger logger = LogManager.getLogger();
    private String url;
    private String sign;
    private int batch;
    private String location;
    private FileInputStream inStream;

    public BatchLoader(int batch, String location, String url) {
        this.url = url;
        this.sign = sign();
        this.batch = batch;
        this.location = location;

    }

    public void load(String filePath) {
        Sheet worksheet = openSheet(filePath);
        Iterator<Row> iterator = worksheet.rowIterator();
        // skip head
        iterator.next();
        int counter = 0;
        logger.debug(format("Total records: %d", worksheet.getPhysicalNumberOfRows()));

        if (!batches().contains(batch))
            registerBatch();

        List<String> teams = teams();
        System.out.println(teams());
        while (iterator.hasNext()){
            Student student = parse(iterator.next());
            logger.debug(String.format("%d: %s", ++counter, student.toString()));

            if (!teams.contains(student.getTeam())){
                register(student.getTeam());
                teams.add(student.getTeam());
            }

            register(student);
        }

        closeSheet();
    }

    // excel
    private Sheet openSheet(String filePath) {
        try {
            inStream = new FileInputStream(filePath);
            Workbook workbook = WorkbookFactory.create(inStream);
            return workbook.getSheetAt(0);
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void closeSheet() {
        try {
            inStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Student parse(Row row) {
        // first_name | last_name | email | team name | role |
        //      0     |     1     |   2   |   3       |   4  |
        return new Student(
                row.getCell(0).toString(),
                row.getCell(1).toString(),
                row.getCell(2).toString(),
                row.getCell(3).toString().replace(" ", ""),
                row.getCell(4).toString()
        );
    }

    // api
    private String sign() {
        return given().
                    param("email", ConfigurationReader.getProperty("admin-email")).
                    param("password", ConfigurationReader.getProperty("admin-password")).
                when().
                    get(url + "/sign").
                    jsonPath().
                    get("accessToken");
    }

    private List<String> teams() {
        return given().
                    header("Authorization", sign).and().
                when().
                    get(url + "/api/batches").
                    jsonPath().getList(format("find{number=%d}.teams.name", batch));
    }

    private List<Integer> batches() {
        return given().
                header("Authorization", sign).and().
                when().
                get(url + "/api/batches").
                jsonPath().getList("number");
    }


    private void register(Student student) {
        // /api/students/student?
        // first-name= &last-name= &email=
        // &password= &role= &batch-number= &team-name=

        Response response = given().
                                header("Authorization", sign).and().
                                param("first-name", student.getFirstName()).
                                param("last-name", student.getLastName()).
                                param("email", student.getEmail()).
                                param("password", (student.getFirstName().toLowerCase() + student.getLastName().toLowerCase())
                                                        .replace(" ", "")).
                                param("role", student.getRole()).
                                param("batch-number", batch).
                                param("team-name", student.getTeam().replace(" ", "")).
                                param("campus-location", location).
                            when().
                                post(url + "/api/students/student");
        logger.debug(response.getBody().asString());

    }

    private void register(String team) {
        // /api/teams/team?
        // team-name= & batch-number=
        Response response = given().
                                header("Authorization", sign).and().
                                param("team-name", team).
                                param("batch-number", batch).
                                param("campus-location", location).
                            when().
                                post(url + "/api/teams/team");
        logger.debug(response.getBody().asString());
    }

    private void registerBatch() {
        // /api/teams/team?
        // team-name= & batch-number=
        Response response = given().
                                header("Authorization", sign).and().
                                param("batch-number", batch).
                            when().
                                post(url + "/api/batches/batch");
        logger.debug(response.getBody().asString());
    }
}
