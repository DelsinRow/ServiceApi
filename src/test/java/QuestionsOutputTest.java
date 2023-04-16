import org.junit.Assert;
import org.junit.Test;
import org.orioninc.Questions;
import org.orioninc.QuestionsOutput;

import java.util.ArrayList;
import java.util.List;

public class QuestionsOutputTest {

    @Test
    public void stringQuestionsListIsNotEmpty() {
        String testString;
        List<Questions> testList = new ArrayList<>();
        for(int i = 1; i < 3; i++){
            testList.add(new Questions(new ArrayList<>()));
        }
        testString = QuestionsOutput.questions(testList);
        Assert.assertNotNull(testString);
    }

}
