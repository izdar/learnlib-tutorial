package org.mealy;

import de.learnlib.api.oracle.MembershipOracle;
import de.learnlib.api.query.Query;
import de.learnlib.api.SUL;
import de.learnlib.oracle.membership.SULOracle;
import net.automatalib.words.Word;
import net.automatalib.words.WordBuilder;

import java.io.IOException;
import java.util.Collection;

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
            WordBuilder<String> parsedPrefixes = new WordBuilder<>(queries.toArray().length);
            WordBuilder<String> parsedSuffixes = new WordBuilder<>(queries.toArray().length);
            String pre = "";
            String suf = "";
            for (String q : query.getPrefix()) {
                if ((Object) q == Word.epsilon()) {
                    System.out.println("Null found...");
                }
                pre += q;
                parsedPrefixes.add(pre);

            }
            for (String q : query.getSuffix()) {
                if ((Object) q == Word.epsilon()) {
                    System.out.println("Null found...");
                }
                suf += q;
                parsedSuffixes.add(pre+suf);

            }
//            for (String q : query.getSuffix()) {
//                if ((Object) q == Word.epsilon()) {
//                    System.out.println("Null found...");
//                }
//                suf += q;
//                parsedQueries
//            }
//            System.out.println("Found query: " + query.getPrefix() + query.getSuffix());
            SULOracle<String, String> oracle = new SULOracle<>(cppSul);
//            WordBuilder<String> suffix = new WordBuilder<>(1);
//            suffix.add(suf);
//            Word<String> suffixWord = suffix.toWord();
//                System.out.println("Oracle answer: "+oracle.answerQuery(Word.epsilon(), suffixWord));
            query.answer(oracle.answerQuery(parsedPrefixes.toWord(), parsedSuffixes.toWord()));
        }
    }
}