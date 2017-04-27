package pp.block1.cc.dfa;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reinier on 26-4-2017.
 */
public class MyScanner implements Scanner {
    @Override
    public List<String> scan(State dfa, String text) {
        State start = dfa;
        List<String> result = new ArrayList<>();

        /**
         * Keeps track of what you have read so far.
         */
        String history = "";

        /**
         * Keeps track of what the last accepted token was.
         */
        String lastToken = null;

        /**
         * Keeps track of where you should start looking from again after getting stuck.
         */
        int lastTokenEndIndex = 0;

        /**
         * Execute this once for every character plus once more to go through the handled data from the last character.
         * It may be so that the index is reset during the for loop if you got stuck.
         *
         * The scanner always tries to look for lalalali.
         * If you have for example lalalalalali
         * If it is found you have gone over the letters once.
         * If it is not found you have to go back untill lala
         * Therefore you will repeat the la you had extra once
         * Because la is accepted you will not have to go over that again.
         * This means the algorithm has O(2n) as you at most go over the letters twice which remains linear
         */
        for (int i = 0; i <= text.length(); i++) {
            //During the extra cycle you should add the last word and if not in an accepting state go back to the last index of the last token.
            //If this is not possible the text would have not been accepted by the checker.
            if (i == text.length()) {
                if (dfa.isAccepting())
                    lastToken = history;
                if (lastToken != null && lastTokenEndIndex == text.length() - 1)
                    result.add(lastToken);
                if (lastTokenEndIndex != text.length() - 1)
                    i = lastTokenEndIndex;
                continue;
            }

            if (!dfa.hasNext(text.charAt(i))) {
                //When you have nowhere to go check whether you already have a word, add it and search again from there.
                //And always reset the bookkeeping and begin again in the DFA.
                if (lastToken != null) {
                    result.add(lastToken);
                    i = lastTokenEndIndex;
                }
                lastToken = null;
                history = "";
                dfa = start;
            } else {
                //when you do have a next node, go there and add the character to the history.
                //If you have an accepting node, you should record the token in the lasttoken variable and record the index.
                dfa = dfa.getNext(text.charAt(i));
                history = history + text.charAt(i);
                if (dfa.isAccepting()) {
                    lastToken = history;
                    lastTokenEndIndex = i;
                }
            }
        }
        return result;
    }
}


//    State base = dfa;
//    int beginindex = 0;
//    List<String> result = new ArrayList<>();
//        for (int i = 0; i < text.length(); i++) {
//        if (dfa.hasNext(text.charAt(i))) {
//        dfa = dfa.getNext(text.charAt(i));
//        }
//        if (dfa.isAccepting()) {
//        result.add(text.substring(beginindex, i + 1));
//        beginindex = i + 1;
//        dfa = base;
//        }
//
//        }