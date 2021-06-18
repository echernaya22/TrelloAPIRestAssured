package core;

import beans.TrelloCard;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import utils.ConfigReader;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class TrelloCardServiceObj extends BaseServiceObj {

    private TrelloCardServiceObj(Map<String, String> parameters, Method method, Map<String, String> path) {
        super(parameters, method, path);
    }

    public static ApiRequestBuilder requestBuilderCard() {
        return new ApiRequestBuilder();
    }

    public static class ApiRequestBuilder {
        private Map<String, String> parameters = new HashMap<>();
        private Map<String, String> path = new LinkedHashMap<>();
        private Method requestMethod = Method.GET;

        public ApiRequestBuilder setMethod(Method method) {
            requestMethod = method;
            return this;
        }

        public ApiRequestBuilder setName(String name) {
            parameters.put("name", name);
            return this;
        }

        public ApiRequestBuilder setDesc(String desc) {
            parameters.put("desc", desc);
            return this;
        }

        public ApiRequestBuilder setId(String id) {
            parameters.put("id", id);
            return this;
        }

        public ApiRequestBuilder setClosed(Boolean bool) {
            parameters.put("closed", bool.toString());
            return this;
        }

        public ApiRequestBuilder setSubscribed(Boolean bool) {
            parameters.put("subscribed", bool.toString());
            return this;
        }

        public ApiRequestBuilder setIdBoard(String boardId) {
            parameters.put("idBoard", boardId);
            return this;
        }

        public ApiRequestBuilder setIdList(String listId) {
            parameters.put("idList", listId);
            return this;
        }

        public TrelloCardServiceObj buildRequest() {
            return new TrelloCardServiceObj(parameters, requestMethod, path);
        }

    }

    //ENDING OF BUILDER PATTERN

    public static TrelloCard getCard(Response response) {
        TrelloCard answers = new Gson()
                .fromJson(response.asString().trim(), new TypeToken<TrelloCard>() {
                }.getType());
        return answers;
    }

}
