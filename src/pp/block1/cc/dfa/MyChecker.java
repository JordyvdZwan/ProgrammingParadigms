package pp.block1.cc.dfa;

/**
 * Created by reinier on 26-4-2017.
 */
public class MyChecker implements Checker {

    @Override
    public boolean accepts(State start, String word) {
        for (int i = 0; i < word.length(); i++) {
            if (start.hasNext(word.charAt(i))) {
                start = start.getNext(word.charAt(i));
            } else {
                return false;
            }
        }
        return start.isAccepting();
    }
}
