package core;

import constants.TestData;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.ConfigReader;

import java.util.Map;

public class BaseServiceObj {

    public static final String TRELLO_URI = TestData.TRELLO_URI;
    protected Method requestMethod;

    //BEGINNING OF BUILDER PATTERN
    protected Map<String, String> parameters;

    public BaseServiceObj(Map<String, String> parameters, Method method) {
        this.parameters = parameters;
        this.requestMethod = method;
    }

    public Response sendRequest(String map) {
        parameters.put("key", ConfigReader.getProperty("key"));
        parameters.put("token", ConfigReader.getProperty("token"));

        return RestAssured
                .given(requestSpecification()).log().all()
                .queryParams(parameters)
                .request(requestMethod ,TRELLO_URI + map)
                .prettyPeek();
    }

    public static RequestSpecification requestSpecification() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .setBaseUri(TRELLO_URI)
                .build();
    }


}
