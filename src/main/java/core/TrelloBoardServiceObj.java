package core;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.restassured.RestAssured;
import io.restassured.http.Method;
import beans.TrelloBoard;
import io.restassured.response.Response;
import utils.ConfigReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrelloBoardServiceObj extends BaseServiceObj {

    private TrelloBoardServiceObj(Map<String, String> parameters, Method method, Map<String, String> path) {
        super(parameters, method, path);
    }

    public static ApiRequestBuilder requestBuilderBoard() {
        return new ApiRequestBuilder();
    }


    public static class ApiRequestBuilder {
        private Map<String, String> parameters = new HashMap<>();
        private Map<String, String> path = new HashMap<>();
        private Method requestMethod = Method.GET;

        public ApiRequestBuilder setMethod(Method method) {
            requestMethod = method;
            return this;
        }

        public ApiRequestBuilder setName(String name) {
            parameters.put("name", name);
            return this;
        }

        public ApiRequestBuilder setId(String boardId) {
            parameters.put("id", boardId);
            return this;
        }

        public ApiRequestBuilder setDesc(String desc) {
            parameters.put("desc", desc);
            return this;
        }

        public ApiRequestBuilder setClosed(Boolean bool) {
            parameters.put("closed", bool.toString());
            return this;
        }

        public ApiRequestBuilder setBoardId(String id) {
            path.put("id", id);
            return this;
        }

        public TrelloBoardServiceObj buildRequest() {
            return new TrelloBoardServiceObj(parameters, requestMethod, path);
        }

    }

    //ENDING OF BUILDER PATTERN

    public static TrelloBoard getBoard(Response response) {
        TrelloBoard answers = new Gson()
                .fromJson(response.asString().trim(), new TypeToken<TrelloBoard>() {
                }.getType());
        return answers;
    }

}
