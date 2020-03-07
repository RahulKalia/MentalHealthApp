package com.example.fitnesstrackingapp;

public class BayesHelper {
    // [rows][columns]
    static int[][] moodStepsMatrix = new int[6][11];
    static int[][] moodJitterMatrix = new int[6][7];
    static float[][][] bayesMatrix = new float[5][10][6];
    static float omega = -1;

//    public void updateMatrices(int mood, int steps,int jitters){
//        // Increment column steps and corresponding mood
//        int stepIndex = calculateStepBucket(steps);
//        moodStepsMatrix[mood][stepIndex]++;
//
//        // Update each column totals
//        moodStepsMatrix[5][stepIndex]++;
//        moodStepsMatrix[mood][10]++;
//        moodStepsMatrix[5][10]++; //total of all entries
//
//        updateBayesTable(stepIndex);
//
//    }

    public void updateMatrices(int mood, int steps, int jitters){
        // Increment column steps and corresponding mood
        int stepIndex = calculateStepBucket(steps);
        int jitterIndex = calculateJitterBucket(jitters);

        moodStepsMatrix[mood][stepIndex]++;
        moodJitterMatrix[mood][jitterIndex]++;

        // Update each column totals
        moodStepsMatrix[5][stepIndex]++;
        moodJitterMatrix[5][jitterIndex]++;

        moodStepsMatrix[mood][10]++;
        moodJitterMatrix[mood][6]++;

        moodStepsMatrix[5][10]++; //total of all entries
        moodJitterMatrix[5][6]++;

        //Calculate Omega
        omega = 0;
        for (int m =0; m <5; m++){
            // p of Jitter given mood
            float a = moodJitterMatrix[m][jitterIndex] / moodJitterMatrix[m][6];

            // p of steps given mood
            float b = moodStepsMatrix[m][stepIndex] / moodStepsMatrix[m][10];

            // p of mood
            float c = moodJitterMatrix[m][6] / moodJitterMatrix[5][6];

            omega += a * b * c;
        }


        updateBayesTable(stepIndex, jitterIndex);

    }

    public void init(){
        // Populate matrix and initialise to 1
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 10; j++) {
                moodStepsMatrix[i][j] = 1;
            }
        }

        for (int i  = 0; i < 5; i++){
            for (int j = 0; j < 6; j++){
                moodJitterMatrix[i][j] = 1;
            }
        }

        // Populate the totals column to 10
        for(int i =0; i <5; i++){
            moodStepsMatrix[i][10] = 10;
            moodJitterMatrix[i][6] = 6;
        }

        // Populate the totals row to 5
        for(int i =0; i <10; i++){
            moodStepsMatrix[5][i] = 5;
        }
        for (int i=0; i < 6; i++){
            moodJitterMatrix[5][i] = 5;
        }

        moodStepsMatrix[5][10] = 50;
        moodJitterMatrix[5][6] = 30;
    }


    // Pass in number of steps and function returns bucket it belongs to
    public  int calculateStepBucket(int steps){
        if (steps >= 0 && steps <= 1000){
            return 0;
        }
        if (steps >= 1001 && steps <= 2000){
            return 1;
        }
        if (steps >= 2001 && steps <= 3000){
            return 2;
        }
        if (steps >= 3001 && steps <= 4000){
            return 3;
        }
        if (steps >= 4001 && steps <= 5000){
            return 4;
        }
        if (steps >= 5001 && steps <= 6000){
            return 5;
        }
        if (steps >= 6001 && steps <= 7000){
            return 6;
        }
        if (steps >= 7001 && steps <= 8000){
            return 7;
        }
        if (steps >= 8001 && steps <= 9000){
            return 8;
        }
        if (steps >= 9001 && steps <= 10000){
            return 9;
        }
        return -1;
    }

    // Pass in number of steps and function returns bucket it belongs to
    public  int calculateJitterBucket(int shakes){
        if (shakes > 0){
            return 0;
        }
        if (shakes > 10){
            return 1;
        }
        if (shakes > 20){
            return 2;
        }
        if (shakes > 30){
            return 3;
        }
        if (shakes > 40){
            return 4;
        }
        else{
            return 5;
        }


    }

    public void updateBayesTable(int stepBracket, int jitterBracket){

        for (int m =0; m < 5; m++){

            // p of jitter given mood
           float a = moodJitterMatrix[m][jitterBracket] / moodJitterMatrix[m][6];
           // p of steps given mood
           float b = moodStepsMatrix[m][stepBracket] / moodStepsMatrix[m][10];
           // p of mood
           float c = moodJitterMatrix[m][6] / moodJitterMatrix[5][6];

           // update 3d matrix
           bayesMatrix[m][stepBracket][jitterBracket] = ((a * b) / omega) * c;

        }

    }

    public int getMood(int steps, int jitter){
        int stepIndex = calculateStepBucket(steps);
        int jitterIndex = calculateJitterBucket(jitter);


        // Initialize maximum element
        float max = bayesMatrix[0][stepIndex][jitterIndex];
        int maxIndex = 0;

        // Traverse array elements from second and
        // compare every element with current max
        for (int m = 1; m < 5; m++) {
            if (bayesMatrix[m][stepIndex][jitterIndex] > max){
                max = bayesMatrix[m][stepIndex][jitterIndex];
                maxIndex = m;
            }
        }

        return  maxIndex;
    }


}
