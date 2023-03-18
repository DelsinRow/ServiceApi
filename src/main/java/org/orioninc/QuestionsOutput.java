package org.orioninc;

import java.util.List;

public class QuestionsOutput {
    //метод перерабатывающий массив Questions в построчную стрингу для передачи в Hastebin
    static String test(List<Questions> list) {
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
