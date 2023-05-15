package org.orioninc;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class QuestionsOutputTest {

    @Test
    public void stringQuestionsListIsNotEmpty() {
        String testString;
        List<Questions> testList = new ArrayList<>();
        for(int i = 1; i < 3; i++){
            testList.add(new Questions(new ArrayList<>(), "test"));
        }
        testString = QuestionsOutput.questions(testList);
        Assert.assertNotNull(testString);
    }

}
