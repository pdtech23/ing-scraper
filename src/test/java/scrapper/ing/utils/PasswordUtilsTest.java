package scrapper.ing.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PasswordUtilsTest {

    @Test
    public void shouldReturnCorrespondingIndexes() {
        // given
        String sampleMask = "*-*--*---*----*-----------------";
        List<Integer> indexesCorrespondingToStars = Arrays.asList(0, 2, 5, 9, 14);

        // when
        List<Integer> result = PasswordUtils.extractStarIndexesFromMask(sampleMask);

        // then
        Assert.assertEquals(indexesCorrespondingToStars, result);
    }
}