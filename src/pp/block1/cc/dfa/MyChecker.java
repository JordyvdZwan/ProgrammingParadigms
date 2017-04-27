package pp.block1.cc.dfa;

/**
 * Created by reinier on 26-4-2017.
 */
public class MyChecker implements Checker {

    @Override
    public boolean accepts(State start, String word) {
        /*
         * For loop goes from 0 to the length of list - 1 therefore n and does not change the i which means it is linear with the list size.
         * The efficiency is O(n)
         */
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
