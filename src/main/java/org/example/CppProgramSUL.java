package org.example;

import de.learnlib.api.SUL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class CppProgramSUL implements SUL<String, Boolean> {
    private Process process;
    private BufferedReader inputReader;
    private BufferedReader errorReader;

    private String command;

    public CppProgramSUL(String command) throws IOException {
        this.command = command;
    }

    @Override
    public void pre() {
        // no pre-processing necessary
    }

    @Override
    public Boolean step(String input) {
        try {
            List<String> list = new ArrayList<String>();
            list.add(command);
            list.add(input);
            ProcessBuilder build = new ProcessBuilder(list);
            System.out.println(build.command());
            process = build.start();
            System.out.println("Started program");
            inputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            // send input to the C++ program
//            process.getOutputStream().write(input.getBytes());

            // read the program's output
            String output = inputReader.readLine();
            System.out.print(output);
            // check if the output is "Yes"
            if (output.equals("Yes")) {
                return true;
            } else {
                System.out.println("Hit false case..");
                return false;
            }
        } catch (IOException e) {
            throw new RuntimeException("Error communicating with C++ program", e);
        }
    }

    @Override
    public void post() {
//        try {
//            // close the input stream to the C++ program
//            process.getOutputStream().close();
//        } catch (IOException e) {
//            throw new RuntimeException("Error closing input stream to C++ program", e);
//        }
    }

    //    @Override
    public void close() {
        try {
            // close all streams and terminate the C++ program
            inputReader.close();
            errorReader.close();
            process.destroy();
        } catch (IOException e) {
            throw new RuntimeException("Error closing streams and terminating C++ program", e);
        }
    }
}
