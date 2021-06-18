package utils;

import org.apache.commons.lang.RandomStringUtils;

import java.util.Random;

public class RandomGenerator {

    public String generateString() {
        Random rand = new Random();
        int upperbound = 30;
        int int_random = rand.nextInt(upperbound);

        return RandomStringUtils.randomAlphabetic(int_random);
    }
}
