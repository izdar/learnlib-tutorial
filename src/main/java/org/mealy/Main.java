package org.mealy;

import de.learnlib.algorithms.lstar.mealy.ExtensibleLStarMealy;
import de.learnlib.algorithms.lstar.mealy.ExtensibleLStarMealyBuilder;
import de.learnlib.algorithms.rivestschapire.RivestSchapireMealy;
import de.learnlib.algorithms.rivestschapire.RivestSchapireMealyBuilder;
import de.learnlib.api.oracle.EquivalenceOracle;

import de.learnlib.api.oracle.MembershipOracle;
import de.learnlib.api.query.DefaultQuery;
import de.learnlib.datastructure.observationtable.OTUtils;
import de.learnlib.datastructure.observationtable.writer.ObservationTableASCIIWriter;
import de.learnlib.filter.statistic.Counter;
import de.learnlib.oracle.equivalence.MealyRandomWordsEQOracle;
import de.learnlib.oracle.equivalence.MealyWMethodEQOracle;
import de.learnlib.oracle.equivalence.MealyWpMethodEQOracle;
import de.learnlib.util.statistics.SimpleProfiler;
import net.automatalib.automata.transducers.MealyMachine;
import net.automatalib.automata.transducers.impl.compact.CompactMealy;
import net.automatalib.serialization.dot.GraphDOT;
import net.automatalib.visualization.Visualization;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;
import net.automatalib.words.WordBuilder;
import net.automatalib.words.impl.Alphabets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) throws IOException {
        // create a new SUL for the C++ program
        CppMealyProgramSUL cppSul = new CppMealyProgramSUL("/Users/pirwani/Desktop/Security/learnlib/mealy");

        // define the alphabet of the inputs
        List<String> list = new ArrayList<>();
        list.add("0");
        list.add("1");
        List<String> list2 = new ArrayList<>();
        list2.add("0");
        list2.add("1");
        list2.add("2");
        list2.add("3");
        list2.add("4");
        Alphabet<String> outputs = Alphabets.fromList(list2);
        Alphabet<String> inputs = Alphabets.fromList(list);
//      create a LearnLib membership oracle using the SUL


        MembershipOracle.MealyMembershipOracle<String, String> membershipOracle = new CppProgramMealyMembershipOracle();
//        DFACounterOracle<Character> mqOracle = new DFACounterOracle<>(membershipOracle, "membership queries");
        WordBuilder<String> suffixes = new WordBuilder<>(2);
        suffixes.add("0");
        suffixes.add("1");
        Word<String> suffixesWord = suffixes.toWord();
        List<Word<String>> t = new ArrayList<>(1);
        t.add(suffixesWord);
        //      create a LearnLib L* DFA learner
        ExtensibleLStarMealy<String, String> learner = new ExtensibleLStarMealyBuilder<String, String>().withAlphabet(inputs).withOracle(membershipOracle).create();
        EquivalenceOracle<MealyMachine<?, String, ?, String>, String, Word<String>> eqOracle = new MealyWMethodEQOracle<>(membershipOracle, 0, 5, 100);

//        Experiment.MealyExperiment<String, String> experiment = new Experiment.MealyExperiment<String, String>(learner, eqOracle,inputs);
        // turn on time profiling
        //------------------------------------------------
        boolean learning = true;
        Counter round = new Counter("Rounds", "");

        round.increment();
        learner.startLearning();

        MealyMachine<?, String, ?, String> hypothesis = learner.getHypothesisModel();

        while (learning) {
            // Write outputs
            // Search counter-example
            SimpleProfiler.start("Searching for counter-example");
            DefaultQuery<String, Word<String>> counterExample = eqOracle.findCounterExample(hypothesis, inputs);
            System.out.println(counterExample);
            System.out.println(round);
            SimpleProfiler.stop("Searching for counter-example");

            if (counterExample == null) {
                // No counter-example found, so done learning
                learning = false;

                // Write outputs
                //writeAutModel(hypothesis, alphabet, config.output_dir + "/learnedModel.aut");
            } else {
                // Counter example found, update hypothesis and continue learning

                round.increment();
//                System.out.println("Counterexample: "+counterExample.getInput());
//                System.out.println("Hypothesis Model: "+learner.getHypothesisModel().toString());
//                SimpleProfiler.start("Learning");
//                System.out.println("Learned: "+learner.refineHypothesis(counterExample));
//                SimpleProfiler.stop("Learning");
                learner.refineHypothesis(counterExample);
                hypothesis = learner.getHypothesisModel();
            }
        }
        // get learned model
        MealyMachine<?, String, ?, String> result = learner.getHypothesisModel();

        // report results
        System.out.println("-------------------------------------------------------");

        // profiling
        System.out.println(SimpleProfiler.getResults());

        // learning statistics
        //        System.out.println(learner.getRounds().getSummary());
        //        System.out.println(mqOracle.getStatisticalData().getSummary());

        // model statistics
        System.out.println("States: " + result.size());
        System.out.println("Sigma: " + inputs.size());

        // show model
        System.out.println();
        System.out.println("Model: ");
        GraphDOT.write(result, inputs, System.out); // may throw IOException!

        Visualization.visualize(result, inputs);
        new ObservationTableASCIIWriter<>().write(learner.getObservationTable(), System.out);

        //------------------------------------------------


    }
}


