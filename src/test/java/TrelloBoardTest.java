import actions.Actions;
import beans.TrelloBoard;
import beans.TrelloList;
import constants.Endpoints;
import core.DataProviderForTrello;
import io.restassured.http.Method;
import utils.RandomGenerator;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.*;

import static core.TrelloBoardServiceObj.*;
import static core.TrelloListServiceObj.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class TrelloBoardTest extends BaseTest{

    @Test
    public void checkBoardUpdate() {
        RandomGenerator randomGenerator = new RandomGenerator();

        String newName = randomGenerator.generateString();
        String newDesc = randomGenerator.generateString();

        TrelloBoard expectBoard = new TrelloBoard();
        expectBoard.setId(boardId);
        expectBoard.setName(newName);
        expectBoard.setDesc(newDesc);
        expectBoard.setClosed(false);

        TrelloBoard updateBoard = getBoard(
                requestBuilderBoard()
                .setName(newName)
                .setDesc(newDesc)
                .setMethod(Method.PUT)
                .buildRequest()
                .sendRequest(Endpoints.BOARDS + boardId)
                .then().assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().response()
        );

        assertThat(updateBoard, equalTo(expectBoard));
    }

    @Test
    public void checkGetBoardById() {

        TrelloBoard expectBoard = new TrelloBoard();
        expectBoard.setId(boardId);
        expectBoard.setName(newBoard.getName());
        expectBoard.setDesc(newBoard.getDesc());
        expectBoard.setClosed(false);

        TrelloBoard getBoard = getBoard(
                requestBuilderBoard()
                .setId(boardId)
                .setMethod(Method.GET)
                .buildRequest().sendRequest(Endpoints.BOARDS + boardId)
                .then().assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().response()
        );

        Assert.assertEquals(getBoard, expectBoard);
    }

    @Test(dataProviderClass = DataProviderForTrello.class,
    dataProvider = "defaultNames")
    public void checkGetBoardDefaultLists(Object[] listNames) {

        List<TrelloList> getCards = getAnswers(
                requestBuilderList()
                        .setMethod(Method.GET)
                        .setBoardId(boardId)
                        .buildRequest().sendRequest(Endpoints.BOARDS_LISTS)
                        .then().assertThat()
                        .statusCode(HttpStatus.SC_OK)
                        .extract().response()
        );

        List<String> actualList = new LinkedList<>();
        for (TrelloList card : getCards) {
            actualList.add(card.getName());
        }

        List<String> expectList = new LinkedList<String>();
        for (Object s : listNames) {
            expectList.add(s.toString());
        }

        Assert.assertEquals(actualList, expectList);
    }

}
