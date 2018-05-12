# cybertek-bnb-loader
load students to the cybertek-bnb-api from an excel sheet

## Data
follow the format as in src/main/resources/MOCK-DATA.xlsx 
### ! make sure all cells are empty after last record

## Configure
In configuratiom.properties specify url to api app,path to your excel sheet and batch number you'll add to

## Build
`mvn clean install assembly:assembly`
`java -cp target/cybertek-bnb-loader-1.0-SNAPSHOT-jar-with-dependencies.jar app.App`



 
