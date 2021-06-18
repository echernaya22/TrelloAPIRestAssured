import actions.Actions;
import beans.TrelloList;
import constants.Endpoints;
import io.restassured.http.Method;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.RandomGenerator;

import static core.TrelloListServiceObj.getList;
import static core.TrelloListServiceObj.requestBuilderList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class TrelloListTest extends BaseTest {

    @Test
    public void checkListCreate() {
        Actions listAction = new Actions();
        TrelloList newList = listAction.createDefaultList(boardId);

        String listName = newList.getName();

        TrelloList expectedList = new TrelloList();
        expectedList.setIdBoard(boardId);
        expectedList.setClosed(false);
        expectedList.setName(listName);

        String listId = newList.getId();
        expectedList.setId(listId);

        Assert.assertEquals(newList, expectedList);

    }

    @Test
    public void checkUpdateListName() {
        Actions listAction = new Actions();
        RandomGenerator randomGenerator = new RandomGenerator();

        TrelloList newList = listAction.createDefaultList(boardId);

        String listId = newList.getId();

        String randomName = randomGenerator.generateString();

        TrelloList updateList = getList(
                requestBuilderList()
                        .setName(randomName)
                        .setMethod(Method.PUT)
                        .buildRequest().sendRequest(Endpoints.LISTS + listId)
                        .then().assertThat()
                        .statusCode(HttpStatus.SC_OK)
                        .extract().response()
        );

        assertThat(updateList.getName(), equalTo(randomName));
    }
}
