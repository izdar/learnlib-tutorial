package org.mealy;

//import de.learnlib.api.query.SymbolQuery;

import de.learnlib.api.SUL;
import de.learnlib.api.oracle.MembershipOracle;
import de.learnlib.api.query.Query;
import de.learnlib.oracle.membership.SULOracle;
import net.automatalib.automata.fsa.DFA;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;
import net.automatalib.words.WordBuilder;

import java.io.IOException;
import java.util.Collection;

public class CppProgramMealyMembershipOracle implements MembershipOracle.MealyMembershipOracle<String, String> {

    private final SULOracle<String, String> sulOracle;
    private final SUL<String, String> cppSul;
    public CppProgramMealyMembershipOracle() throws IOException {
        this.cppSul = new CppMealyProgramSUL("/Users/pirwani/Desktop/Security/learnlib/mealy");
        this.sulOracle = new SULOracle<>(new CppMealyProgramSUL("/Users/pirwani/Desktop/Security/learnlib/mealy"));
    }
    @Override
    public void processQueries(Collection<? extends Query<String, Word<String>>> queries) {
        for (Query<String, Word<String>> query : queries) {
            System.out.println(query.getInput());
            String p = "";
            for(String q : query.getInput()) {
                p += q;
            }
            System.out.println("Found query: "+p);
            SULOracle<String, String> oracle = new SULOracle<>(cppSul);
//            System.out.println(oracle.answerQuery(query.getInput()));
            WordBuilder<String> w = new WordBuilder<String>(p.length());
            w.add(p);
            Word<String> obj = w.toWord();
            System.out.println("Answer: "+oracle.answerQuery(obj));
            query.answer(oracle.answerQuery(obj));
        }
    }

    //    @Override
    public boolean isCounterexample(Word<String> word, Boolean output, DFA<?, String> hypothesis, Alphabet<String> alphabet) {
        return !hypothesis.accepts(word) == output;
    }

}
