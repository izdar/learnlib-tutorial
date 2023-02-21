package org.mealy;

import de.learnlib.algorithms.lstar.ce.ObservationTableCEXHandlers;
import de.learnlib.algorithms.lstar.mealy.ClassicLStarMealy;

import de.learnlib.api.algorithm.LearningAlgorithm;
import de.learnlib.api.oracle.EquivalenceOracle;

import de.learnlib.api.oracle.MembershipOracle;
import de.learnlib.datastructure.observationtable.OTUtils;
import de.learnlib.datastructure.observationtable.writer.ObservationTableASCIIWriter;
import de.learnlib.oracle.equivalence.MealyWpMethodEQOracle;
import de.learnlib.oracle.equivalence.RandomWordsEQOracle;
import de.learnlib.oracle.equivalence.WpMethodEQOracle;
import de.learnlib.oracle.membership.SULOracle;
import de.learnlib.util.Experiment;
import de.learnlib.util.statistics.SimpleProfiler;
import net.automatalib.automata.transducers.MealyMachine;
import net.automatalib.serialization.dot.GraphDOT;
import net.automatalib.visualization.Visualization;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;
import net.automatalib.words.impl.Alphabets;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.example.CppProgramMembershipOracle;
import org.example.CppProgramSUL;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static de.learnlib.algorithms.lstar.closing.ClosingStrategies.CLOSE_FIRST;
import static de.learnlib.algorithms.lstar.closing.ClosingStrategies.CLOSE_LEX_MIN;


public class Main {
    public static void main(String[] args) throws IOException {
        // create a new SUL for the C++ program
        CppMealyProgramSUL cppSul = new CppMealyProgramSUL("/Users/pirwani/Desktop/Security/learnlib/mealy");

        // define the alphabet of the inputs
        List<Object> list = new ArrayList<>();
        list.add("0");
        list.add("1");
        List<Object> list2 = new ArrayList<>();
        list2.add("0");
        list2.add("1");
        list2.add("2");
        list2.add("3");
        list2.add("4");
        Alphabet<Object> outputs = Alphabets.fromList(list2);
        Alphabet<Object> inputs = Alphabets.fromList(list);
//      create a LearnLib membership oracle using the SUL


        MembershipOracle.MealyMembershipOracle membershipOracle = new CppProgramMealyMembershipOracle();
//        DFACounterOracle<Character> mqOracle = new DFACounterOracle<>(membershipOracle, "membership queries");

//      create a LearnLib L* DFA learner
        ClassicLStarMealy learner = new ClassicLStarMealy<Object,String>(inputs,membershipOracle, ObservationTableCEXHandlers.SUFFIX1BY1, CLOSE_FIRST);

        EquivalenceOracle.MealyEquivalenceOracle eqOracle = new MealyWpMethodEQOracle(membershipOracle,5,10,100);

        Experiment.MealyExperiment<Object, String> experiment = new Experiment.MealyExperiment<Object, String>(learner, eqOracle,inputs);
        // turn on time profiling
        experiment.setProfile(true);

        // enable logging of models
        experiment.setLogModels(true);
        // run experiment
        experiment.run();

        // get learned model
        MealyMachine<?, Object, ?, String> result = experiment.getFinalHypothesis();

        // report results
        System.out.println("-------------------------------------------------------");

        // profiling
        System.out.println(SimpleProfiler.getResults());

        // learning statistics
        System.out.println(experiment.getRounds().getSummary());
//        System.out.println(mqOracle.getStatisticalData().getSummary());

        // model statistics
        System.out.println("States: " + result.size());
        System.out.println("Sigma: " + inputs.size());

        // show model
        System.out.println();
        System.out.println("Model: ");
        GraphDOT.write(result, inputs, System.out); // may throw IOException!

        Visualization.visualize(result, inputs);

        System.out.println("-------------------------------------------------------");

        System.out.println("Final observation table:");
        new ObservationTableASCIIWriter<>().write(learner.getObservationTable(), System.out);

        OTUtils.displayHTMLInBrowser(learner.getObservationTable());


    }
}


