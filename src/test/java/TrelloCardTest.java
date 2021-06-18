import actions.Actions;
import beans.TrelloCard;
import beans.TrelloList;
import constants.Endpoints;
import io.restassured.http.Method;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;
import utils.RandomGenerator;

import java.util.List;

import static core.TrelloCardServiceObj.getCard;
import static core.TrelloCardServiceObj.requestBuilderCard;
import static core.TrelloListServiceObj.*;
import static core.TrelloListServiceObj.requestBuilderList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

public class TrelloCardTest extends BaseTest {

    @Test
    public void checkUpdateCard() {
        Actions listActions = new Actions();
        Actions cardActions = new Actions();
        RandomGenerator randomGenerator = new RandomGenerator();

        TrelloList newList = listActions.createDefaultList(boardId);

        String listId = newList.getId();

        TrelloCard newCard = cardActions.createDefaultCard(boardId, listId);

        String cardId = newCard.getId();

        String randomName = randomGenerator.generateString();
        String randomDesc = randomGenerator.generateString();

        TrelloCard updatedCard = getCard(
                requestBuilderCard()
                        .setName(randomName)
                        .setDesc(randomDesc)
                        .setMethod(Method.PUT)
                        .buildRequest().sendRequest(Endpoints.CARDS + cardId)
                        .then().assertThat()
                        .statusCode(HttpStatus.SC_OK)
                        .extract().response()
        );
        assertThat(updatedCard.getName(), equalTo(randomName));
        assertThat(updatedCard.getDesc(), equalTo(randomDesc));
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
        Actions listActions = new Actions();
        Actions cardActions = new Actions();

        TrelloList newList = listActions.createDefaultList(boardId);

        String listId = newList.getId();

        TrelloCard firstCard = cardActions.createDefaultCard(boardId, listId);

        TrelloCard secondCard = cardActions.createDefaultCard(boardId, listId);

        TrelloCard archiveCard = getCard(
                requestBuilderCard()
                        .setClosed(true)
                        .setMethod(Method.PUT)
                        .buildRequest().sendRequest(Endpoints.CARDS + secondCard.getId())
                        .then().assertThat()
                        .statusCode(HttpStatus.SC_OK)
                        .extract().response()
        );

        List<TrelloList> getCards = getAnswers(
                requestBuilderList()
                        .setMethod(Method.GET)
                        .setListId(listId)
                        .buildRequest().sendRequest(Endpoints.LISTS_CARDS)
                        .then().assertThat()
                        .statusCode(HttpStatus.SC_OK)
                        .extract().response()
        );

        assertThat(getCards, hasSize(1));
        assertThat(getCards.get(0).getId(), equalTo(firstCard.getId()));
        assertThat(getCards.get(0).getIdBoard(), equalTo(firstCard.getIdBoard()));
        assertThat(getCards.get(0).getName(), equalTo(firstCard.getName()));
        assertThat(archiveCard.getClosed(), equalTo(true));
        assertThat(archiveCard.getId(), equalTo(secondCard.getId()));
    }
}
