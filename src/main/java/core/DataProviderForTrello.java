package core;

import constants.DefaultListData;
import org.testng.annotations.DataProvider;

public class DataProviderForTrello {

    @DataProvider
    public Object[] defaultNames() {
        return new Object[][] {
                {DefaultListData.DEFAULT_LIST_NAME_FIRST,
                        DefaultListData.DEFAULT_LIST_NAME_SECOND,
                        DefaultListData.DEFAULT_LIST_NAME_THIRD}
        };
    }
}
