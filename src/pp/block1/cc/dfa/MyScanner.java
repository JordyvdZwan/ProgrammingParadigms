package pp.block1.cc.dfa;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reinier on 26-4-2017.
 */
public class MyScanner implements Scanner {
    @Override
    public List<String> scan(State dfa, String text) {
        State base = dfa;
        int beginindex = 0;
        List<String> result = new ArrayList<>();
        for (int i = 0; i < text.length(); i++) {
            if (dfa.hasNext(text.charAt(i))) {
                dfa = dfa.getNext(text.charAt(i));
            }
            if (dfa.isAccepting()) {
                result.add(text.substring(beginindex, i + 1));
                beginindex = i + 1;
                dfa = base;
            }

        }
        return result;
    }
}
