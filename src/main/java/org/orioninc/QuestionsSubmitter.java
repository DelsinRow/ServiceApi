package org.orioninc;

import java.io.IOException;
import java.util.List;

public interface QuestionsSubmitter {
    public String submitDocument(List<Questions> allQuestionsList) throws IOException, InterruptedException ;
    public void printFinalLink(String key);
}
