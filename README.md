# Easy Savings Tutorial Application
This project showcases the use case of using both the Locations and Loyalty Offers APIs to retrieve easy savings offers.

## Frameworks Used
- Spring
- Thymeleaf
- OpenAPI Generator

## Requirements
- [Maven](https://maven.apache.org/download.cgi)

## Setup
1. Create an account at [Mastercard Developers](https://developer.mastercard.com).
2. Create a new project and add the `Locations` and `Loyalty Offers` APIs to your project. A `.p12` file is downloaded automatically.
3. Take note of the given consumer key, keyalias, and keystore password given upon the project creation.
4. Copy the downloaded `.p12` file to `/src/main/resources`.
5. Update the properties found under `/src/main/resources/application.properties`.
6. Run `mvn clean install` from the root of the project directory.
7. run `java -jar target/easy-savings-reference-app-1.0.0.jar` to start the project.
8. Navigate to `http://localhost:8080/` in your browser.

## Tutorial
A tutorial can be found [here](https://developer.mastercard.com/tutorial/locations-loyalty-offers-api-tutorial?lang=java#overview) 
for setting up and using this service.

## Loyalty Offers Client Library
The client library used to generate the API Calls and object models for the Loyalty Offers service can be seen in the pom.xml file
in the project's root directory.

    `<plugin>
         <groupId>org.openapitools</groupId>
         <artifactId>openapi-generator-maven-plugin</artifactId>
         <version>4.1.1</version>
         <executions>
             <execution>
                 <goals>
                     <goal>generate</goal>
                 </goals>
                 <configuration>
                     <inputSpec>loyalty-offers.yaml</inputSpec>
                     <generatorName>java</generatorName>
                     <library>okhttp-gson</library>
                     <generateApiTests>false</generateApiTests>
                     <generateModelTests>false</generateModelTests>
                     <generateApiTests>false</generateApiTests>
                     <configOptions>
                         <sourceFolder>src/gen/java/main</sourceFolder>
                     </configOptions>
                 </configuration>
             </execution>
         </executions>
     </plugin>`
    
For more information on how this client generator works please consult the official [Github repository](https://github.com/OpenAPITools/openapi-generator)

## Locations SDK
The Locations API is offered through an SDK. The dependency for this SDK can be seen in the pom.xml file.
    `<dependency>
        <groupId>com.mastercard.api</groupId>
        <artifactId>locations</artifactId>
        <version>1.0.4</version>
    </dependency>`

## Usage
- Fill in the required parameters and click submit.
- This will make a call to the Locations API and return the merchants associated with the parameters you filled out
- Click on 'JSON Response' to view the raw JSON returned by the API call
- Click on 'View Merchants' to see a list of all merchants returned
- Click on a merchant to make a call to the Loyalty Offers API for an expanded view of the selected merchant  
