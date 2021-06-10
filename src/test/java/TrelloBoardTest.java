import beans.TrelloBoard;
import beans.TrelloCard;
import beans.TrelloList;
import constants.TestData;
import core.DataProviderForTrello;
import io.restassured.http.Method;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.PathCreator;

import java.util.*;

import static core.TrelloBoardServiceObj.*;
import static core.TrelloListServiceObj.*;
import static core.TrelloCardServiceObj.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TrelloBoardTest {

    private static String boardId;
    private static PathCreator pathCreator = new PathCreator();

    @BeforeMethod()
    public static void setUp() {
        String path = pathCreator.createPath("boards");

        TrelloBoard newBoard = getBoard(
                requestBuilderBoard()
                        .setName(TestData.BOARD_NAME)
                        .setMethod(Method.POST)
                        .buildRequest()
                        .sendRequest(path));
        boardId = newBoard.getId();

        Assert.assertNotNull(newBoard.getId());
    }

    @AfterMethod
    public static void tearDown() {
        String pathDelete = pathCreator.createPath("boards", boardId);
        String pathGet = pathCreator.createPath("boards");

        requestBuilderBoard()
                .setMethod(Method.DELETE)
                .buildRequest()
                .sendRequest(pathDelete);

        requestBuilderBoard()
                .setMethod(Method.GET)
                .buildRequest()
                .sendRequest(pathGet)
                .then().assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void checkBoardUpdate() {
        String path = pathCreator.createPath("boards", boardId);

        String newName = TestData.BOARD_NAME_UPDATE;
        String newDesc = TestData.BOARD_DESC;

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
                .sendRequest(path)
                .then().assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().response()
        );

        assertThat(updateBoard, equalTo(expectBoard));
    }

    @Test
    public void checkGetBoardById() {
        String path = pathCreator.createPath("boards", boardId);

        TrelloBoard expectBoard = new TrelloBoard();
        expectBoard.setId(boardId);
        expectBoard.setName(TestData.BOARD_NAME);
        expectBoard.setDesc("");
        expectBoard.setClosed(false);

        TrelloBoard getBoard = getBoard(
                requestBuilderBoard()
                .setId(boardId)
                .setMethod(Method.GET)
                .buildRequest().sendRequest(path)
                .then().assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().response()
        );

        Assert.assertEquals(getBoard, expectBoard);
    }

    @Test(dataProviderClass = DataProviderForTrello.class,
    dataProvider = "defaultNames")
    public void checkGetBoardDefaultLists(Object[] listNames) {

        String path = pathCreator.createPath("boards", boardId, "lists");

        List<TrelloList> getCards = getAnswers(
                requestBuilderList()
                        .setMethod(Method.GET)
                        .buildRequest().sendRequest(path)
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

    @Test
    public void checkListCreate() {
        String path = pathCreator.createPath("lists");

        TrelloList expectedList = new TrelloList();
        expectedList.setIdBoard(boardId);
        expectedList.setClosed(false);
        expectedList.setName(TestData.LIST_NAME);

        TrelloList newList = getList(
                requestBuilderList()
                .setName(TestData.LIST_NAME)
                .setIdBoard(boardId)
                .setClosed(false)
                .setMethod(Method.POST)
                .buildRequest().sendRequest(path)
                .then().assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().response()
        );

        String listId = newList.getId();
        expectedList.setId(listId);

        Assert.assertEquals(newList, expectedList);

    }

    @Test
    public void checkUpdateList() {
        String pathCreate = pathCreator.createPath("lists");

        TrelloList newList = getList(
                requestBuilderList()
                        .setName(TestData.LIST_NAME)
                        .setIdBoard(boardId)
                        .setClosed(false)
                        .setMethod(Method.POST)
                        .buildRequest().sendRequest(pathCreate)
                        .then().assertThat()
                        .statusCode(HttpStatus.SC_OK)
                        .extract().response()
        );

        String listId = newList.getId();

        String pathUpdate = pathCreator.createPath("lists", listId);

        TrelloList updateList = getList(
                requestBuilderList()
                .setName(TestData.LIST_NAME_UPDATE)
                .setMethod(Method.PUT)
                .buildRequest().sendRequest(pathUpdate)
                .then().assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().response()
        );

        assertThat(updateList.getName(), equalTo(TestData.LIST_NAME_UPDATE));
    }

    @Test
    public void checkUpdateCard() {

        String pathCreateList = pathCreator.createPath("lists");
        String pathCreateCard = pathCreator.createPath("cards");

        TrelloList newList = getList(
                requestBuilderList()
                        .setName(TestData.LIST_NAME)
                        .setIdBoard(boardId)
                        .setClosed(false)
                        .setMethod(Method.POST)
                        .buildRequest().sendRequest(pathCreateList)
                        .then().assertThat()
                        .statusCode(HttpStatus.SC_OK)
                        .extract().response()
        );

        String listId = newList.getId();

        TrelloCard newCard = getCard(
                requestBuilderCard()
                        .setIdBoard(boardId)
                        .setName(TestData.FIRST_CARD_NAME)
                        .setDesc(TestData.FIRST_CARD_DESC)
                        .setIdList(listId)
                        .setClosed(false)
                        .setSubscribed(false)
                        .setMethod(Method.POST)
                        .buildRequest().sendRequest(pathCreateCard)
                        .then().assertThat()
                        .statusCode(HttpStatus.SC_OK)
                        .extract().response()
        );

        String cardId = newCard.getId();
        String pathUpdateCard = pathCreator.createPath("cards", cardId);

        TrelloCard updatedCard = getCard(
                requestBuilderCard()
                .setName(TestData.SECOND_CARD_NAME)
                .setDesc(TestData.SECOND_CARD_DESC)
                .setMethod(Method.PUT)
                .buildRequest().sendRequest(pathUpdateCard)
                .then().assertThat()
                .statusCode(HttpStatus.SC_OK)
                .extract().response()
        );
        assertThat(updatedCard.getName(), equalTo(TestData.SECOND_CARD_NAME));
        assertThat(updatedCard.getDesc(), equalTo(TestData.SECOND_CARD_DESC));
        assertThat(updatedCard.getId(), equalTo(cardId));
    }

    /**
     * 1. Create list
     * 2. Create 2 cards
     * 3. Archive second card
     * 4. Get card by list id
     * 5. Assert that there only first card
     */
    @Test
    public void checkArchiveCard() {

        String pathCreateList = pathCreator.createPath("lists");
        String pathCreateCard = pathCreator.createPath("cards");

        TrelloList newList = getList(
                requestBuilderList()
                        .setName(TestData.LIST_NAME)
                        .setIdBoard(boardId)
                        .setClosed(false)
                        .setMethod(Method.POST)
                        .buildRequest().sendRequest(pathCreateList)
                        .then().assertThat()
                        .statusCode(HttpStatus.SC_OK)
                        .extract().response()
        );

        String listId = newList.getId();

        TrelloCard firstCard = getCard(
                requestBuilderCard()
                .setIdBoard(boardId)
                .setName(TestData.FIRST_CARD_NAME)
                .setDesc(TestData.FIRST_CARD_DESC)
                .setIdList(listId)
                .setClosed(false)
                .setSubscribed(false)
                .setMethod(Method.POST)
                .buildRequest().sendRequest(pathCreateCard)
                        .then().assertThat()
                        .statusCode(HttpStatus.SC_OK)
                        .extract().response()
        );

        TrelloCard secondCard = getCard(
                requestBuilderCard()
                        .setIdBoard(boardId)
                        .setName(TestData.SECOND_CARD_NAME)
                        .setDesc(TestData.SECOND_CARD_DESC)
                        .setIdList(listId)
                        .setClosed(false)
                        .setSubscribed(false)
                        .setMethod(Method.POST)
                        .buildRequest().sendRequest(pathCreateCard)
                        .then().assertThat()
                        .statusCode(HttpStatus.SC_OK)
                        .extract().response()
        );

        String pathUpdateCard = pathCreator.createPath("cards", secondCard.getId());

        TrelloCard archiveCard = getCard(
                requestBuilderCard()
                .setClosed(true)
                .setMethod(Method.PUT)
                .buildRequest().sendRequest(pathUpdateCard)
                        .then().assertThat()
                        .statusCode(HttpStatus.SC_OK)
                        .extract().response()
        );

        String pathGetCards = pathCreator.createPath("lists", listId, "cards");

        List<TrelloList> getCards = getAnswers(
                requestBuilderList()
                .setMethod(Method.GET)
                .buildRequest().sendRequest(pathGetCards)
                        .then().assertThat()
                        .statusCode(HttpStatus.SC_OK)
                        .extract().response()
        );

        assertThat(getCards, hasSize(1));
        assertThat(getCards.get(0).getId(), equalTo(firstCard.getId()));
        assertThat(getCards.get(0).getIdBoard(), equalTo(firstCard.getIdBoard()));
        assertThat(getCards.get(0).getName(), equalTo(firstCard.getName()));

    }

}
