package utils;

import org.apache.commons.lang.text.StrBuilder;

public class PathCreator {

    public String createPath(String... str) {
        StrBuilder newStr = new StrBuilder();

        for (String s : str) {
            newStr.append("/").append(s);
        }
        return newStr.toString();
    }
}
