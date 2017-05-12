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

        for (Rule rule : grammar.getRules()) {
            result.put(rule.getLHS(), new HashSet<Term>());
        }

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
                    while (((result.get(b) != null && b == Symbol.EMPTY) || (result.get(b) != null && result.get(b).contains(Symbol.EMPTY))) && i < symbols.size()) {
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
        Map<NonTerm, Set<Term>> result = new HashMap<NonTerm, Set<Term>>();
        Map<Symbol, Set<Term>> first = getFirst();

        for (Rule rule : grammar.getRules()) {
            result.put(rule.getLHS(), new HashSet<Term>());
        }
        result.get(grammar.getStart()).add(Symbol.EOF);

        boolean changing = true;
        while (changing) {
            changing = false;
            for (Rule p : grammar.getRules()) {
                Set<Term> trailer = new HashSet<>();
                trailer.addAll(result.get(p.getLHS()));
                List<Symbol> symbols = p.getRHS();
                int i = symbols.size() - 1;

                while (i >= 0) {
                    Symbol b = symbols.get(i--);
                    if (b instanceof NonTerm) {
                        if (result.get(b).addAll(trailer)) changing = true;
                        if (first.get(b) != null && first.get(b).contains(Symbol.EMPTY)) {
                            Set<Term> temp = first.get(b);
                            temp.remove(Symbol.EMPTY);
                            trailer.addAll(temp);
                        } else {
                            Set<Term> temp = first.get(b);
                            trailer.clear();
                            trailer.addAll(temp);
                        }
                    } else {
                        Set<Term> temp = first.get(b);
                        if (temp != null) {
                            trailer.clear();
                            trailer.addAll(temp);
                        } else {
                            trailer.clear();
                            trailer.add((Term) b);
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    public Map<Rule, Set<Term>> getFirstp() {
        Map<Rule, Set<Term>> result = new HashMap<Rule, Set<Term>>();
        Map<Symbol, Set<Term>> first = getFirst();
        Map<NonTerm, Set<Term>> follow = getFollow();

        for (Rule p : grammar.getRules()) {
            Set<Term> fb = null;
            if (p.getRHS().get(0) instanceof NonTerm) {
                fb = first.get(p.getRHS().get(0));
            } else {
                fb = new HashSet<Term>();
                fb.add((Term) p.getRHS().get(0));
            }
            if (fb != null && fb.contains(Symbol.EMPTY)) {
                Set<Term> temp = new HashSet<Term>();
                temp.addAll(fb);
                temp.addAll(follow.get(p.getLHS()));
                result.put(p, temp);
            } else {
                Set<Term> temp = new HashSet<Term>();
                if (fb != null && temp != null) {
                    temp.addAll(fb);
                    result.put(p, temp);
                }
            }
        }

        return result;
    }

    @Override
    public boolean isLL1() {
        Map<Rule, Set<Term>> firstp = getFirstp();
        for (NonTerm nonTerm : grammar.getNonterminals()) {
            List<Rule> rules = grammar.getRules(nonTerm);
            List<Symbol> nonTermList = new ArrayList<>();
            Set<Symbol> nonTermSet = new HashSet<>();
            for (Rule rule : rules) {
                nonTermList.addAll(firstp.get(rule));
                nonTermSet.addAll(firstp.get(rule));
            }
            if (nonTermList.size() != nonTermSet.size())
                return false;
        }
        return true;
    }

}