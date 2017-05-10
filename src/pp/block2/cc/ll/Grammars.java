/**
 * 
 */
package pp.block2.cc.ll;

import pp.block2.cc.NonTerm;
import pp.block2.cc.Symbol;
import pp.block2.cc.SymbolFactory;
import pp.block2.cc.Term;

/**
 * Class containing some example grammars.
 * @author Arend Rensink
 *
 */
public class Grammars {
	/** Returns a grammar for simple English sentences. */
	public static Grammar makeSentence() {
		// Define the non-terminals
		NonTerm sent = new NonTerm("Sentence");
		NonTerm subj = new NonTerm("Subject");
		NonTerm obj = new NonTerm("Object");
		NonTerm mod = new NonTerm("Modifier");
		// Define the terminals, using the Sentence.g4 lexer grammar
		// Make sure you take the token constantss from the right class!
		SymbolFactory fact = new SymbolFactory(Sentence.class);
		Term noun = fact.getTerminal(Sentence.NOUN);
		Term verb = fact.getTerminal(Sentence.VERB);
		Term adj = fact.getTerminal(Sentence.ADJECTIVE);
		Term end = fact.getTerminal(Sentence.ENDMARK);
		// Build the context free grammar
		Grammar g = new Grammar(sent);
		g.addRule(sent, subj, verb, obj, end);
		g.addRule(subj, noun);
		g.addRule(subj, mod, subj);
		g.addRule(obj, noun);
		g.addRule(obj, mod, obj);
		g.addRule(mod, adj);
		return g;
	}

	public static Grammar makeCC1() {
		// Define the non-terminals
		NonTerm stat = new NonTerm("Stat");
		NonTerm elsepart = new NonTerm("ElsePart");
		// Define the terminals, using the Sentence.g4 lexer grammar
		// Make sure you take the token constantss from the right class!
		SymbolFactory fact = new SymbolFactory(CC1.class);
		Term ift = fact.getTerminal(CC1.If);
		Term assign = fact.getTerminal(CC1.Assign);
		Term expr = fact.getTerminal(CC1.Expr);
		Term then = fact.getTerminal(CC1.Then);
        Term elset = fact.getTerminal(CC1.Else);

		// Build the context free grammar
		Grammar g = new Grammar(stat);
		g.addRule(stat, ift, expr, then, stat, elsepart);
		g.addRule(stat, assign);
		g.addRule(elsepart, elset, stat);
		g.addRule(elsepart, Symbol.EMPTY);
		return g;
	}

	public static Grammar makeCC2() {
		// Define the non-terminals
		NonTerm L = new NonTerm("L");
		NonTerm R = new NonTerm("R");
		NonTerm Rprime = new NonTerm("Rprime");
		NonTerm Q = new NonTerm("Q");
        NonTerm S = new NonTerm("S");
		// Define the terminals, using the Sentence.g4 lexer grammar
		// Make sure you take the token constantss from the right class!
		SymbolFactory fact = new SymbolFactory(CC2.class);
		Term a = fact.getTerminal(CC2.A);
		Term ba = fact.getTerminal(CC2.Ba);
		Term aba = fact.getTerminal(CC2.Aba);
		Term caba = fact.getTerminal(CC2.Caba);
        Term bc = fact.getTerminal(CC2.Bc);
        Term b = fact.getTerminal(CC2.B);
        Term c = fact.getTerminal(CC2.C);
		// Build the context free grammar
		Grammar g = new Grammar(L);
		g.addRule(L, R, a);
		g.addRule(L, Q, ba);
		g.addRule(R, aba, Rprime);
		g.addRule(R, caba, Rprime);
		g.addRule(Rprime, bc, Rprime);
		g.addRule(Rprime, Symbol.EMPTY);
        g.addRule(Q, b, S);
        g.addRule(S, bc);
        g.addRule(S, c);
		return g;
	}
}
