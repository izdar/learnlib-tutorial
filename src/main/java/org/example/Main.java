package org.example;

import de.learnlib.algorithms.lstar.dfa.ClassicLStarDFA;
import de.learnlib.algorithms.lstar.dfa.ClassicLStarDFABuilder;
import de.learnlib.algorithms.lstar.dfa.ExtensibleLStarDFA;
import de.learnlib.algorithms.lstar.dfa.ExtensibleLStarDFABuilder;
import de.learnlib.api.SUL;

import de.learnlib.api.algorithm.LearningAlgorithm;
import de.learnlib.api.oracle.EquivalenceOracle;
import de.learnlib.api.oracle.MembershipOracle;
import de.learnlib.api.query.DefaultQuery;
import de.learnlib.api.query.Query;

import de.learnlib.datastructure.observationtable.OTUtils;
import de.learnlib.datastructure.observationtable.writer.ObservationTableASCIIWriter;
import de.learnlib.filter.statistic.oracle.DFACounterOracle;
import de.learnlib.oracle.equivalence.DFAWMethodEQOracle;
import de.learnlib.oracle.equivalence.RandomWordsEQOracle;
import de.learnlib.oracle.equivalence.WpMethodEQOracle;
import de.learnlib.oracle.membership.SULOracle;
import de.learnlib.util.Experiment;
import de.learnlib.util.statistics.SimpleProfiler;
import net.automatalib.automata.fsa.DFA;
import net.automatalib.automata.fsa.impl.compact.CompactDFA;
import net.automatalib.serialization.dot.GraphDOT;
import net.automatalib.visualization.Visualization;
import net.automatalib.words.Alphabet;
import net.automatalib.words.Word;
import net.automatalib.words.impl.Alphabets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


public class Main {
    public static void main(String[] args) throws IOException {
        // create a new SUL for the C++ program
        CppProgramSUL cppSul = new CppProgramSUL("/Users/pirwani/Desktop/Security/learnlib/moore");

        // define the alphabet of the inputs
        List<String> list = new ArrayList<>();
        list.add("0");
        list.add("1");
        Alphabet<String> inputs = Alphabets.fromList(list);

//      create a LearnLib membership oracle using the SUL

        MembershipOracle<String,Boolean> membershipOracle = new CppProgramMembershipOracle();
//        DFACounterOracle<Character> mqOracle = new DFACounterOracle<>(membershipOracle, "membership queries");

//      create a LearnLib L* DFA learner
        ClassicLStarDFA<String> learner = new ClassicLStarDFA<>(inputs,membershipOracle);

        EquivalenceOracle eqOracle = new WpMethodEQOracle(membershipOracle,5,10,200);

        Experiment.DFAExperiment<String> experiment = new Experiment.DFAExperiment<String>(learner, eqOracle,inputs);
        // turn on time profiling
        experiment.setProfile(true);

        // enable logging of models
        experiment.setLogModels(true);
        // run experiment
        experiment.run();

        // get learned model
        DFA<?, String> result = experiment.getFinalHypothesis();

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


