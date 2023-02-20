package org.example;

//import de.learnlib.api.query.SymbolQuery;

import de.learnlib.api.SUL;
import de.learnlib.api.oracle.MembershipOracle;
import de.learnlib.api.query.Query;
import de.learnlib.oracle.membership.SULOracle;
import de.learnlib.oracle.membership.SULSymbolQueryOracle;
import net.automatalib.automata.fsa.DFA;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;
import net.automatalib.words.WordBuilder;

import java.io.IOException;
import java.util.Collection;

public class CppProgramMembershipOracle implements MembershipOracle.DFAMembershipOracle<String> {

    private final SULOracle<String, Boolean> sulOracle;
    private final SUL<String, Boolean> cppSul;
    public CppProgramMembershipOracle() throws IOException {
        this.cppSul = new CppProgramSUL("/Users/pirwani/Desktop/Security/learnlib/moore");
        this.sulOracle = new SULOracle<>(new CppProgramSUL("/Users/pirwani/Desktop/Security/learnlib/moore"));
    }

    @Override
    public void processQueries(Collection<? extends Query<String, Boolean>> queries) {
        for (Query<String, Boolean> query : queries) {
            System.out.println(query.getInput());
            String p = "";
            for(String q : query.getInput()) {
                p += q;
            }
            System.out.println("Found query: "+p);
            SULSymbolQueryOracle<String, Boolean> oracle = new SULSymbolQueryOracle<>(cppSul);
//            System.out.println(oracle.answerQuery(query.getInput()));
            WordBuilder<String> w = new WordBuilder<String>(p.length());
            w.add(p);
            Word<String> obj = w.toWord();
            query.answer(oracle.answerQuery(obj).getSymbol(0));
        }
    }

//    @Override
    public boolean isCounterexample(Word<String> word, Boolean output, DFA<?, String> hypothesis, Alphabet<String> alphabet) {
        return !hypothesis.accepts(word) == output;
    }

}
