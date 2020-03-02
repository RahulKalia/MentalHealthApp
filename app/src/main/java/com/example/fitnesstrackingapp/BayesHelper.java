package com.example.fitnesstrackingapp;

public class BayesHelper {
    // [rows][columns]
    int[][] moodMatrix = new int[6][11];
    float[][] bayesMatrix = new float[5][10];

    public void updateMoodMatrix(int mood, int steps){
        // Increment column steps and corresponding mood
        int stepIndex = calculateStepBucket(steps);
        moodMatrix[mood][stepIndex]++;

        // Update each column totals
        moodMatrix[5][stepIndex]++;
        moodMatrix[mood][10]++;
        moodMatrix[5][10]++; //total of all entries

        updateBayesTable(stepIndex);
    }

    public void init(){
        // Populate matrix and initialise to 1
        for (int i = 0; i < moodMatrix.length-1; i++) {
            for (int j = 0; j < moodMatrix[i].length-1; j++) {
                moodMatrix[i][j] = 1;
            }
        }

        // Populate the totals column to 10
        for(int i =0; i <5; i++){
            moodMatrix[i][10] = 10;
        }

        // Populate the totals row to 5
        for(int i =0; i <10; i++){
            moodMatrix[5][i] = 5;
        }
    }


    // Pass in number of steps and function returns bucket it belongs to
    public  int calculateStepBucket(int steps){
        if (steps >= 0 && steps <= 1000){
            return 1;
        }
        if (steps >= 1001 && steps <= 2000){
            return 2;
        }
        if (steps >= 2001 && steps <= 3000){
            return 3;
        }
        if (steps >= 3001 && steps <= 4000){
            return 4;
        }
        if (steps >= 4001 && steps <= 5000){
            return 5;
        }
        if (steps >= 5001 && steps <= 6000){
            return 6;
        }
        if (steps >= 6001 && steps <= 7000){
            return 7;
        }
        if (steps >= 7001 && steps <= 8000){
            return 8;
        }
        if (steps >= 8001 && steps <= 9000){
            return 9;
        }
        if (steps >= 9001 && steps <= 10000){
            return 10;
        }
        return -1;
    }


    public void updateBayesTable(int stepBracket){

        for (int i =0; i < 5; i++){
            // Step 1 numTimesMoodFeltInBracket/totalTimesMoodFelt
            float pOfStepBracketGivenMood = (moodMatrix[i][stepBracket]/moodMatrix[i][10]);

            // Step 2 totalTimesMoodFelt/totalOfAllMoodsFelt
            float pOfMood = (moodMatrix[i][10]/moodMatrix[5][10]);

            // Step 3 totalStepsInBracket/totalStepsInAnyBracket
            float pOfStepBracket = moodMatrix[5][stepBracket]/moodMatrix[5][10];

            // Step 4 ((1*2)/3)
            float result = (pOfStepBracketGivenMood*pOfMood)/pOfStepBracket;

            bayesMatrix[i][stepBracket] = result;
        }

    }

    public int getMood(int steps){
        int stepIndex = calculateStepBucket(steps);

        int i;

        // Initialize maximum element
        float max = bayesMatrix[0][stepIndex];
        int maxIndex = 0;

        // Traverse array elements from second and
        // compare every element with current max
        for (i = 1; i < 5; i++) {
            if (bayesMatrix[i][stepIndex] > max){
                max = bayesMatrix[i][stepIndex];
                maxIndex = i;
            }
        }

        return  maxIndex;
    }


}
