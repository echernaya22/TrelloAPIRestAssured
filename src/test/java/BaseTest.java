import actions.Actions;
import beans.TrelloBoard;
import constants.Endpoints;
import io.restassured.http.Method;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import static core.TrelloBoardServiceObj.requestBuilderBoard;

public class BaseTest {

    protected static String boardId;
    protected static TrelloBoard newBoard;

    @BeforeMethod(alwaysRun = true)
    public static void setUp() {
        Actions defaultAction = new Actions();
        newBoard = defaultAction.createDefaultBoard();
        boardId = newBoard.getId();

        Assert.assertNotNull(newBoard.getId());
    }

    @AfterMethod(alwaysRun = true)
    public static void tearDown() {

        requestBuilderBoard()
                .setMethod(Method.DELETE)
                .buildRequest()
                .sendRequest(Endpoints.BOARDS + boardId);

        requestBuilderBoard()
                .setMethod(Method.GET)
                .buildRequest()
                .sendRequest(Endpoints.BOARDS + boardId)
                .then().assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }
}
