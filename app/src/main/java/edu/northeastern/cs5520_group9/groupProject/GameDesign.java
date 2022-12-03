package edu.northeastern.cs5520_group9.groupProject;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class GameDesign {
    // instance variables
    String operation; // + or -
    String difficultyLevel; // easy medium hard
    boolean singleMode; // single player is true, else false
    int curStage; // single problem, like 2+3
    int score; // current score in this round
    Queue<String> questionQueue;
    Queue<HashSet<Integer>> optionsQueue;
    Queue<Integer> correctOptionQueue;

    int curNumber1; // randomly generate the first number
    int curNumber2; // randomly generate the second number

    HashSet<Integer> curOptions; // generate multiple choices
    String curQuestion;
    int curAnswer; // right answer for the current question

    Random rand = new Random();
    long startTime;

    public GameDesign(String operation, String difficultyLevel, boolean singleMode, int curStage, int score) {
        this.operation = operation;
        this.difficultyLevel = difficultyLevel;
        this.singleMode = singleMode;
        this.curStage = curStage;
        this.score = score;
        generateQuestions();
    }

    public GameDesign(String operation, String difficultyLevel, boolean singleMode, int curStage) {
        this.operation = operation;
        this.difficultyLevel = difficultyLevel;
        this.singleMode = singleMode;
        this.curStage = curStage;
        this.score = 0;
        generateQuestions();
    }


    public GameDesign() {

    }

    public String getOperation() {
        return operation;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public Boolean getSingleMode() {
        return singleMode;
    }

    public int getCurStage() {
        return curStage;
    }

    public int getCurScore() {
        return score;
    }

    public void clearStage() {
        curOptions.clear();
    }

    /**
     * Generates the 10 questions and 5 answer options for each question.
     */
    private void generateQuestions() {
        questionQueue = new LinkedList<>();
        optionsQueue = new LinkedList<>();
        correctOptionQueue = new LinkedList<>();

        for (int i = 0; i < 10; i++) {
            generateNumbers();
            generateOptions();
            String question = curNumber1 + " " + operation + " " + curNumber2 + " = ?";
            questionQueue.add(question);
            optionsQueue.add(curOptions);
            correctOptionQueue.add(curAnswer);
        }
    }

    private void getCurrentQuestion() {
        curQuestion = questionQueue.remove();
    }

    private void getCurrentOptions() {
        curOptions = optionsQueue.remove();
    }

    private void getCurrentAnswer() {
        curAnswer = correctOptionQueue.remove();
    }

    /**
     * Generates one game stage.
     */
    public void generateOneStage() {

        getCurrentQuestion();
        getCurrentOptions();
        getCurrentAnswer();
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        startTime = ts.getTime();
    }

    /**
     * Generates two numbers for the math operation (+/-) based on difficulty level.
     */
    public void generateNumbers() {
        int upperBound = 0;

        if (difficultyLevel.equals("easy")) {
            upperBound = 10;
        } else if (difficultyLevel.equals("medium")) {
            upperBound = 20;
        } else if (difficultyLevel.equals("hard")) {
            upperBound = 100;
        }


        if (operation.equals("-")) {
            curNumber1 = rand.nextInt(upperBound) + 1;
            curNumber2 = rand.nextInt(curNumber1) + 1;
        } else {
            curNumber1 = rand.nextInt(upperBound) + 1;
            curNumber2 = rand.nextInt(upperBound) + 1;
        }
    }

    /**
     * Generates five options for each question.
     */
    public void generateOptions() {
        curOptions = new HashSet();
        if (operation.equals("+")) {
            curAnswer = curNumber1 + curNumber2;
        } else if (operation.equals("-")) {
            curAnswer = curNumber1 - curNumber2;
        }

        // add the correct answer to options
        curOptions.add(curAnswer);

        // ensure there are 5 options
        while (curOptions.size() < 5) {
            int option = curAnswer + (rand.nextInt(11) - 5);
            if (option >= 0) {
                curOptions.add(option);
            }
        }
    }
}