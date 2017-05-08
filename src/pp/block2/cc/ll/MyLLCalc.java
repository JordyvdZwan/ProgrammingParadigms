package pp.block2.cc.ll;

import pp.block2.cc.NonTerm;
import pp.block2.cc.Symbol;
import pp.block2.cc.Term;

import java.util.*;

/**
 * Created by Jordy van der Zwan on 08-May-17.
 */
public class MyLLCalc implements LLCalc {

    Grammar grammar;

    public MyLLCalc(Grammar grammar) {
        this.grammar = grammar;
    }

    @Override
    public Map<Symbol, Set<Term>> getFirst() {
        Map<Symbol, Set<Term>> result = new HashMap<Symbol, Set<Term>>();

        boolean changing = true;
        while (changing) {
            changing = false;
            for (Rule p : grammar.getRules()) {
                int i = 0;
                Set<Term> rhs = new HashSet<>();
                List<Symbol> symbols = p.getRHS();
                if (symbols.size() > 0) {
                    Symbol b = symbols.get(0);
                    rhs = new HashSet<Term>();
                    if (b instanceof Term) {
                        if (b != Symbol.EMPTY) rhs.add((Term) b);
                    } else {
                        if (result.get(b) != null) rhs.addAll(result.get(b));
                        rhs.remove(Symbol.EMPTY);
                    }
                    i = 0;
                    b = symbols.get(0);
                    while (((result.get(b) == null && b == Symbol.EMPTY) || (result.get(b) != null && result.get(b).contains(Symbol.EMPTY))) && i < symbols.size()) {
                        rhs.addAll(result.get(b));
                        rhs.remove(Symbol.EMPTY);
                        b = symbols.get(i);
                    }
                }
                if ((i == (symbols.size() - 1)) && result.get(symbols.get(symbols.size() - 1)) != null && result.get(symbols.get(symbols.size() - 1)).contains(Symbol.EMPTY)) {
                    rhs.add(Symbol.EMPTY);
                }
                result.computeIfAbsent(p.getLHS(), k -> new HashSet<Term>());
                if (result.get(p.getLHS()).addAll(rhs))
                    changing = true;
            }
        }
        return result;
    }


    @Override
    public Map<NonTerm, Set<Term>> getFollow() {

        return null;
    }

    @Override
    public Map<Rule, Set<Term>> getFirstp() {
        return null;
    }

    @Override
    public boolean isLL1() {
        return false;
    }

}