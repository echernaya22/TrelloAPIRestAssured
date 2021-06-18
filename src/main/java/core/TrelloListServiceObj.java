package core;

import beans.TrelloList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import utils.ConfigReader;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TrelloListServiceObj extends BaseServiceObj {

    private TrelloListServiceObj(Map<String, String> parameters, Method method, Map<String, String> path) {
        super(parameters, method, path);
    }

    public static ApiRequestBuilder requestBuilderList() {
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

        public ApiRequestBuilder setId(String id) {
            parameters.put("id", id);
            return this;
        }

        public ApiRequestBuilder setClosed(Boolean bool) {
            parameters.put("closed", bool.toString());
            return this;
        }

        public ApiRequestBuilder setIdBoard(String boardId) {
            parameters.put("idBoard", boardId);
            return this;
        }

        public TrelloListServiceObj buildRequest() {
            return new TrelloListServiceObj(parameters, requestMethod, path);
        }

        public ApiRequestBuilder setBoardId(String boardId) {
            path.put("id", boardId);
            return this;
        }

        public ApiRequestBuilder setListId(String id) {
            path.put("id", id);
            return this;
        }
    }

    //ENDING OF BUILDER PATTERN

    public static TrelloList getList(Response response) {
        TrelloList answers = new Gson()
                .fromJson(response.asString().trim(), new TypeToken<TrelloList>() {
                }.getType());
        return answers;
    }

    public static List<TrelloList> getAnswers(Response response) {
        List<TrelloList> answers = new Gson()
                .fromJson(response.asString().trim(), new TypeToken<List<TrelloList>>() {
                }.getType());
        return answers;
    }

}
