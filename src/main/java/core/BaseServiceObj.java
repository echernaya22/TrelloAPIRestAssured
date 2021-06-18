package core;

import constants.Endpoints;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.ConfigReader;

import java.util.Map;

public class BaseServiceObj {

    protected Method requestMethod;

    //BEGINNING OF BUILDER PATTERN
    protected Map<String, String> parameters;
    protected Map<String, String> path;

    public BaseServiceObj(Map<String, String> parameters, Method method, Map<String, String> path) {
        this.parameters = parameters;
        this.requestMethod = method;
        this.path = path;
    }

    public Response sendRequest(String endpoint) {
        parameters.put("key", ConfigReader.getProperty("key"));
        parameters.put("token", ConfigReader.getProperty("token"));

        return RestAssured
                .given(requestSpecification()).log().all()
                .pathParams(path)
                .queryParams(parameters)
                .request(requestMethod, endpoint)
                .prettyPeek();
    }

    public static RequestSpecification requestSpecification() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .setBaseUri(Endpoints.TRELLO_URI)
                .build();
    }


}
