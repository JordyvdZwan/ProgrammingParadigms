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



//	// Define the non-terminals
//	NonTerm stat = ifG.getNonterminal ("Stat" );
//	NonTerm elsePart = ifG.getNonterminal ("ElsePart" );
//	// Define the terminals (take from the right lexer grammar!)
//	Term ifT = ifG.getTerminal( If.IF );



	// (other terminals you need in the tests)
	Grammar ifG = Grammars.makeCC1(); // to be defined (Ex. 2-CC.4.1)
	LLCalc CC1LL = createCalc ( ifG );

	Grammar ifF = Grammars.makeCC2(); // to be defined (Ex. 2-CC.4.1)
	LLCalc CC2LL = createCalc ( ifF );
//	@Test
//	public void testIfFirst () {
//		Map<Symbol, Set<Term>> first = ifLL.getFirst();
//		assertEquals (/* see 2-CC.1 */, first.get(stat));
//		// (insert other tests)
//	}
//	@Test
//	public void testIfFollow () {
//		Map<NonTerm , Set<Term>> follow = ifLL.getFollow ();
//		assertEquals (/* see 2-CC.1 */, follow.get(stat));
//		// (insert other tests)
//	}
//	@Test
//	public void testIfFirstPlus () {
//		Map<Rule, Set<Term>> firstp = ifLL.getFirstp();
//		List<Rule> elseRules = ifG.getRules(elsePart);
//		assertEquals (/* see 2-CC.1 */, firstp.get(elseRules.get(0)));
//		// (insert other tests)
//	}
	@Test
	public void testCC1LL () {
		assertTrue ( CC1LL.isLL1());
	}

	@Test
	public void testCC2LL () {
		assertTrue ( CC2LL.isLL1());
	}


	/** Tests the LL-calculator for the Sentence grammar. */
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
