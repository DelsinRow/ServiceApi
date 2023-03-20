package org.orioninc;

import java.util.List;


public class QuestionsOutput {

    public static String Questions(List<Questions> list) {
        StringBuilder sb = new StringBuilder();

        for (Questions questions : list) {
            for (String oneQuestion : questions.title()) {
                sb.append(oneQuestion).append("\n");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
