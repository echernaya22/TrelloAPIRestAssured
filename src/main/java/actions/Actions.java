package actions;

import beans.TrelloBoard;
import beans.TrelloCard;
import beans.TrelloList;
import constants.Endpoints;
import io.restassured.http.Method;
import org.apache.http.HttpStatus;
import utils.RandomGenerator;

import static core.TrelloBoardServiceObj.getBoard;
import static core.TrelloBoardServiceObj.requestBuilderBoard;
import static core.TrelloCardServiceObj.getCard;
import static core.TrelloCardServiceObj.requestBuilderCard;
import static core.TrelloListServiceObj.getList;
import static core.TrelloListServiceObj.requestBuilderList;

public class Actions {

    RandomGenerator randomGenerator = new RandomGenerator();

    public TrelloBoard createDefaultBoard() {
        return getBoard(
                requestBuilderBoard()
                        .setName(randomGenerator.generateString())
                        .setMethod(Method.POST)
                        .buildRequest()
                        .sendRequest(Endpoints.BOARDS));
    }

    public TrelloList createDefaultList(String boardId) {
        return getList(
                requestBuilderList()
                        .setName(randomGenerator.generateString())
                        .setIdBoard(boardId)
                        .setMethod(Method.POST)
                        .buildRequest().sendRequest(Endpoints.LISTS)
                        .then().assertThat()
                        .statusCode(HttpStatus.SC_OK)
                        .extract().response()
        );
    }

    public TrelloCard createDefaultCard(String boardId, String listId) {
        return getCard(
                requestBuilderCard()
                        .setIdBoard(boardId)
                        .setName(randomGenerator.generateString())
                        .setDesc(randomGenerator.generateString())
                        .setIdList(listId)
                        .setClosed(false)
                        .setSubscribed(false)
                        .setMethod(Method.POST)
                        .buildRequest().sendRequest(Endpoints.CARDS)
                        .then().assertThat()
                        .statusCode(HttpStatus.SC_OK)
                        .extract().response()
        );
    }
}
