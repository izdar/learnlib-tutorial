package org.mealy;


import com.google.errorprone.annotations.OverridingMethodsMustInvokeSuper;
import de.learnlib.api.SUL;
import de.learnlib.api.oracle.MembershipOracle;
import de.learnlib.api.query.DefaultQuery;
import de.learnlib.api.query.Query;
import de.learnlib.oracle.membership.SULOracle;
//import jdk.internal.util.xml.impl.Input;
import net.automatalib.automata.transducers.MealyMachine;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;
import net.automatalib.words.WordBuilder;

import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CppProgramMealyMembershipOracle implements MembershipOracle.MealyMembershipOracle<String, String> {

    //    private final SULOracle<String, String> sulOracle;
    private final SUL<String, String> cppSul;

    public CppProgramMealyMembershipOracle() throws IOException {
        this.cppSul = new CppMealyProgramSUL("/Users/pirwani/Desktop/Security/learnlib/mealy");
//        this.sulOracle = new SULOracle<>(new CppMealyProgramSUL("/Users/pirwani/Desktop/Security/learnlib/mealy"));
    }

    @Override
    public void processQueries(Collection<? extends Query<String, Word<String>>> queries) {
        for (Query<String, Word<String>> query : queries) {
//            System.out.println(query.getInput());
            System.out.println("Query: " + query);
            String pre = "";
            String suf = "";
            for (String q : query.getPrefix()) {
                if ((Object) q == Word.epsilon()) {
                    System.out.println("Null found...");
                }
                pre += q;
            }
            for (String q : query.getSuffix()) {
                if ((Object) q == Word.epsilon()) {
                    System.out.println("Null found...");
                }
                suf += q;
            }
            System.out.println("Found query: " + query.getPrefix() + query.getSuffix());
            SULOracle<String, String> oracle = new SULOracle<>(cppSul);
            if ((Object) query.getPrefix() == Word.epsilon()) {
                WordBuilder<String> suffix = new WordBuilder<>(1);
                suffix.add(suf);
                Word<String> suffixWord = suffix.toWord();
//                System.out.println("Oracle answer: "+oracle.answerQuery(Word.epsilon(), suffixWord));
                query.answer(oracle.answerQuery(Word.epsilon(), suffixWord));
            } else if ((Object) query.getSuffix() == Word.epsilon()) {
                WordBuilder<String> prefix = new WordBuilder<>(1);
                prefix.add(pre);
                Word<String> prefixWord = prefix.toWord();
//                System.out.println("Oracle answer: "+oracle.answerQuery(prefixWord, Word.epsilon()));
                query.answer(oracle.answerQuery(prefixWord, Word.epsilon()));
            } else {
                WordBuilder<String> prefix = new WordBuilder<>(1);
                prefix.add(pre);
                Word<String> prefixWord = prefix.toWord();
                WordBuilder<String> suffix = new WordBuilder<>(1);
                suffix.add(suf);
                Word<String> suffixWord = suffix.toWord();
//                System.out.println("Oracle answer: "+oracle.answerQuery(prefixWord, suffixWord));
                query.answer(oracle.answerQuery(prefixWord, suffixWord));
            }
        }
    }
}
//    @Override
//    public Word<String> findCounterExample(MealyMachine<?, String, ?, String> hypothesis, Collection<? extends Word<String>> inputs) {
//        System.out.println(hypothesis);
//        for (Word<String> input : inputs) {
//            Word<String> targetOutput = answerQuery(input);
//            Word<String> hypothesisOutput = hypothesis.computeOutput(input);
//            if (!targetOutput.equals(hypothesisOutput)) {
//                System.out.println("Counterexample found: "+input.toString());
//                return input;
//            }
//        }
//        return null;
//    }
//}
