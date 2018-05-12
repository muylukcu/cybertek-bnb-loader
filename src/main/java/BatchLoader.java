import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

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
    private FileInputStream inStream;

    public BatchLoader(int batch, String url) {
        this.url = url;
        this.sign = sign();
        this.batch = batch;

    }

    public void load(String filePath) {
        Sheet worksheet = openSheet(filePath);

        List<String> teams = teams();
        Iterator<Row> iterator = worksheet.rowIterator();
        // skip head
        iterator.next();
        int counter = 0;
        logger.debug(format("Total records: %d", worksheet.getPhysicalNumberOfRows()));
        while (iterator.hasNext()){
            Student student = parse(iterator.next());
            logger.debug(format("%d: %s", ++counter, student.toString()));

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
        // first_name | last_name | programs | email | phone | city | state | team | role
        //      0     |     1     |     2    |   3   |   4   |   5  |   6   |  7   |  8
        return new Student(
                row.getCell(0).toString(),
                row.getCell(1).toString(),
                row.getCell(2).toString(),
                row.getCell(3).toString(),
                row.getCell(4).toString().replace("-",  ""),
                row.getCell(7).toString(),
                row.getCell(8).toString()
        );
    }

    // api
    private String sign() {
        return given().
                    param("email", "admin@mail.com").
                    param("password", "1111").
                when().
                    get(url + "/sign").
                    jsonPath().
                    get("accessToken");
    }

    private List<String> teams() {
        return given().
                    header("Authorization", sign).and().
                when().
                    get(url + "/api/teams").
                    jsonPath().getList("name");
    }

    private void register(Student student) {
        // /api/students/student?
        // first-name= &last-name= &email=
        // &password= &role= &batch-number= &team-name=
        if (student.isLocal()){
            Response response = given().
                                    header("Authorization", sign).and().
                                    param("first-name", student.getFirstName()).
                                    param("last-name", student.getLastName()).
                                    param("email", student.getEmail()).
                                    param("password", student.getPhone()).
                                    param("role", student.getRole()).
                                    param("batch-number", batch).
                                    param("team-name", student.getTeam()).
                                when().
                                    post(url + "/api/students/student");
            logger.debug(response.getBody().asString());
        }
    }

    private void register(String team) {
        // /api/teams/team?
        // team-name= & batch-number=
        Response response = given().
                                header("Authorization", sign).and().
                                param("team-name", team).
                                param("batch-number", batch).
                            when().
                                post(url + "/api/teams/team");
        logger.debug(response.getBody().asString());
    }
}
