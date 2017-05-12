package pp.block2.cc.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import pp.block2.cc.NonTerm;
import pp.block2.cc.Symbol;
import pp.block2.cc.Term;
import pp.block2.cc.ll.*;

public class LLCalcTest {
	Grammar sentenceG = Grammars.makeSentence();
	// Define the non-terminals
	NonTerm subj = sentenceG.getNonterminal("Subject");
	NonTerm obj = sentenceG.getNonterminal("Object");
	NonTerm sent = sentenceG.getNonterminal("Sentence");
	NonTerm mod = sentenceG.getNonterminal("Modifier");
	// Define the terminals
	Term adj = sentenceG.getTerminal(Sentence.ADJECTIVE);
	Term noun = sentenceG.getTerminal(Sentence.NOUN);
	Term verb = sentenceG.getTerminal(Sentence.VERB);
	Term end = sentenceG.getTerminal(Sentence.ENDMARK);
	// Now add the last rule, causing the grammar to fail
	Grammar sentenceXG = Grammars.makeSentence();
	{    sentenceXG.addRule(mod, mod, mod);
	}
	LLCalc sentenceXLL = createCalc(sentenceXG);

	//----------------------------------------------------------//
	//                            CC1                           //
	//----------------------------------------------------------//
	Grammar cc1 = Grammars.makeCC1();
	LLCalc CC1LL = createCalc (cc1);
	NonTerm stat = cc1.getNonterminal ("Stat" );
	NonTerm elsePart = cc1.getNonterminal ("ElsePart" );
	Term ift = cc1.getTerminal( CC1.If );
	Term assign = cc1.getTerminal( CC1.Assign );
	Term elest = cc1.getTerminal( CC1.Else);


	//----------------------------------------------------------//
	//                            CC2                           //
	//----------------------------------------------------------//
	Grammar cc2 = Grammars.makeCC2(); // to be defined (Ex. 2-CC.4.1)
	LLCalc CC2LL = createCalc ( cc2 );
	NonTerm L = cc2.getNonterminal ("L" );
	NonTerm R = cc2.getNonterminal ("R" );
	NonTerm Rprime = cc2.getNonterminal ("Rprime" );
	NonTerm Q = cc2.getNonterminal ("Q" );
	NonTerm S = cc2.getNonterminal ("S" );

	Term a = cc2.getTerminal( CC2.A );
	Term b = cc2.getTerminal( CC2.B );
	Term c = cc2.getTerminal( CC2.C );

	//----------------------------------------------------------//
	//                       CC1 Tests                          //
	//----------------------------------------------------------//
	@Test
	public void testCC1First () {
		Map<Symbol, Set<Term>> first = CC1LL.getFirst();
		assertEquals (set(assign, ift), first.get(stat));
	}
	@Test
	public void testCC1Follow () {
		Map<NonTerm , Set<Term>> follow = CC1LL.getFollow ();
		assertEquals (set(Symbol.EOF, elest), follow.get(stat));
	}
	@Test
	public void testCC1FirstPlus () {
		Map<Rule, Set<Term>> firstp = CC1LL.getFirstp();
		List<Rule> elseRules = cc1.getRules(elsePart);
		assertEquals (set(elest), firstp.get(elseRules.get(0)));
	}

	@Test
	public void testCC1LL () {
		System.out.println(CC1LL.getFirstp());
		assertFalse ( CC1LL.isLL1());
	}

	//----------------------------------------------------------//
	//                       CC2 Tests                          //
	//----------------------------------------------------------//
	@Test
	public void testCC2First () {
		Map<Symbol, Set<Term>> first = CC2LL.getFirst();
		assertEquals (set(a, b, c), first.get(L));
		assertEquals (set(a, c), first.get(R));
		assertEquals (set(b), first.get(Rprime));
		assertEquals (set(b), first.get(Q));
		assertEquals (set(b, c), first.get(S));
	}
	@Test
	public void testCC2Follow () {
		Map<NonTerm , Set<Term>> follow = CC2LL.getFollow ();
		assertEquals (set(Symbol.EOF), follow.get(L));
		assertEquals (set(a), follow.get(R));
		assertEquals (set(a), follow.get(Rprime));
		assertEquals (set(b), follow.get(Q));
		assertEquals (set(b), follow.get(S));
	}
	@Test
	public void testCC2FirstPlus () {
		Map<Rule, Set<Term>> firstp = CC2LL.getFirstp();
		List<Rule> LRules = cc2.getRules(L);
		List<Rule> RRules = cc2.getRules(R);
		List<Rule> RprimeRules = cc2.getRules(Rprime);
		List<Rule> QRules = cc2.getRules(Q);
		List<Rule> SRules = cc2.getRules(S);
		assertEquals (set(a, c), firstp.get(LRules.get(0)));
		assertEquals (set(b), firstp.get(LRules.get(1)));
		assertEquals (set(a), firstp.get(RRules.get(0)));
		assertEquals (set(c), firstp.get(RRules.get(1)));
		assertEquals (set(b), firstp.get(RprimeRules.get(0)));
		assertEquals (set(a, Symbol.EMPTY), firstp.get(RprimeRules.get(1)));
		assertEquals (set(b), firstp.get(QRules.get(0)));
		assertEquals (set(b), firstp.get(SRules.get(0)));
		assertEquals (set(c), firstp.get(SRules.get(1)));
	}


	@Test
	public void testCC2LL () {
		System.out.println(CC2LL.getFirstp());
		assertTrue ( CC2LL.isLL1());
	}


	//----------------------------------------------------------//
	//                       Sentence Tests                     //
	//----------------------------------------------------------//
	@Test
	public void testSentenceOrigLL1() {
		// Without the last (recursive) rule, the grammar is LL-1
		assertTrue(createCalc(sentenceG).isLL1());
	}

	@Test
	public void testSentenceXFirst() {
		Map<Symbol, Set<Term>> first = sentenceXLL.getFirst();
		assertEquals(set(adj, noun), first.get(sent));
		assertEquals(set(adj, noun), first.get(subj));
		assertEquals(set(adj, noun), first.get(obj));
		assertEquals(set(adj), first.get(mod));
	}

	@Test
	public void testSentenceXFollow() {
		// FOLLOW sets
		Map<NonTerm, Set<Term>> follow = sentenceXLL.getFollow();
		System.out.println(follow);
		assertEquals(set(Symbol.EOF), follow.get(sent));
		assertEquals(set(verb), follow.get(subj));
		assertEquals(set(end), follow.get(obj));
		assertEquals(set(noun, adj), follow.get(mod));
	}

	@Test
	public void testSentenceXFirstPlus() {
		// Test per rule
		Map<Rule, Set<Term>> firstp = sentenceXLL.getFirstp();
		List<Rule> subjRules = sentenceXG.getRules(subj);
		assertEquals(set(noun), firstp.get(subjRules.get(0)));
		assertEquals(set(adj), firstp.get(subjRules.get(1)));
	}

	@Test
	public void testSentenceXLL1() {
		assertFalse(sentenceXLL.isLL1());
	}

	@Test
	public void testCC1() {
		// Without the last (recursive) rule, the grammar is LL-1
		assertTrue(createCalc(sentenceG).isLL1());
	}

	@Test
	public void testCC2() {
		// Without the last (recursive) rule, the grammar is LL-1
		assertTrue(createCalc(sentenceG).isLL1());
	}

	/** Creates an LL1-calculator for a given grammar. */
	private LLCalc createCalc(Grammar g) {
		return new MyLLCalc(g);
	}

	@SuppressWarnings("unchecked")
	private <T> Set<T> set(T... elements) {
		return new HashSet<>(Arrays.asList(elements));
	}
}
