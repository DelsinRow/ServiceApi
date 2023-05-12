package org.orioninc;

import java.io.IOException;
import java.util.List;

public interface QuestionsSubmitter {
    public String submitDocument(String allStringQuestions) throws IOException, InterruptedException ;
    public void printFinalLink(String key);
}
