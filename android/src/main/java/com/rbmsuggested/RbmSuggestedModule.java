package com.rbmsuggested;

import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableArray;

import org.ejml.data.DMatrixIterator;
import org.ejml.simple.SimpleMatrix;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;



public class RbmSuggestedModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    private RbmSuggestedModule mRbmSuggested;
    private SimpleMatrix mMatrixWeights;
    private SimpleMatrix mInterceptsHidden;
    private SimpleMatrix mInterceptsVisible;
    private final int[] alcoholIds = {3589, 3587, 2993};


    public RbmSuggestedModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "RbmSuggested";
    }

    @ReactMethod
    public void init(Promise promise) {
        try {
            if (this.mRbmSuggested == null) {
                this.initMatrix();
                this.mRbmSuggested = this;
            }
            promise.resolve(true);
        } catch (Exception e) {
            promise.reject(e);
        }
    }


    @ReactMethod
    public void suggestedFoodItemFor(final ReadableArray foodItemsIds, final Integer hour, final String timezone, final Promise promise) {
        ArrayList<Integer> foodItemsIdsArrayList = new ArrayList<>();
        for (int i = 0; i < foodItemsIds.size(); i++) {
            foodItemsIdsArrayList.add(foodItemsIds.getInt(i));
        }

        SimpleMatrix inputVector = this.createInputVector(foodItemsIdsArrayList, hour, timezone);
        // TODO: figure out which set to use / more efficient way of doing this
        Set<Integer> setValues = new HashSet<Integer>() {};
        // foodItemIds
        setValues.addAll(foodItemsIdsArrayList);
        // alcoholIds
        for (int i = 0; i < this.alcoholIds.length; i++) {
            setValues.add(this.alcoholIds[i]);
        }

        ArrayList<Integer> suggestedFoodItemsIdsArrayList = this.foodItemIdsReconstructFor(inputVector, setValues);
        try {
            WritableArray suggestedFoodItemsIds = Arguments.createArray();
            for (int i = 0; i < suggestedFoodItemsIdsArrayList.size(); i++) {
                int id = suggestedFoodItemsIdsArrayList.get(i);
                suggestedFoodItemsIds.pushInt(id);
            }
            promise.resolve(suggestedFoodItemsIds);
        } catch (Exception e) {
            promise.reject(e);
        }
    }

    private void initMatrix() {
        // weights matrix
        double[] weightsArray = this.matrixValuesFrom("weights_v5.txt");
        SimpleMatrix weightsMatrix = new SimpleMatrix(weightsArray.length,1, true, weightsArray);
        weightsMatrix.reshape(130, 459);
        this.mMatrixWeights = weightsMatrix;

        // intercept hidden
        double[] interceptsHiddenArray = this.matrixValuesFrom("intercept_hidden_v5.txt");
        this.mInterceptsHidden = new SimpleMatrix(130, 1, true, interceptsHiddenArray);

        // intercept visible
        double[] interceptsVisibleArray = this.matrixValuesFrom("intercept_visible_v5.txt");
        this.mInterceptsVisible = new SimpleMatrix(459, 1, true, interceptsVisibleArray);
    }

    private double[] matrixValuesFrom(String filename) {
        BufferedReader bufferedReader = null;
        ArrayList<String> matrixValuesArrayList = new ArrayList<>();

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(reactContext.getAssets().open(filename), "UTF-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                matrixValuesArrayList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        double[] array = new double[matrixValuesArrayList.size()];

        for (int i = 0; i < array.length; i++) {
            array[i] = Double.parseDouble(matrixValuesArrayList.get(i));
        }


        return array;
    }

    private SimpleMatrix logistic(SimpleMatrix inputVector) {
        SimpleMatrix probabilities = inputVector.copy();

        DMatrixIterator iterator = probabilities.iterator(
                true,
                0,
                0,
                inputVector.numRows() - 1,
                inputVector.numCols() - 1
        );
        while (iterator.hasNext()) {
            double x = iterator.next();
            iterator.set(1.0 / (1.0 + Math.exp(-x)));
        }

        return probabilities;
    }

    private SimpleMatrix visibleStateToHiddenProbabilities(SimpleMatrix inputVector) {
        SimpleMatrix hiddenInputs = this.mMatrixWeights.copy().mult(inputVector);
        hiddenInputs = hiddenInputs.plus(this.mInterceptsHidden);
        return this.logistic(hiddenInputs);
    }

    private SimpleMatrix hiddenStateToVisibleProbabilities(SimpleMatrix inputVector) {
        SimpleMatrix matrixWeightsTransposed = this.mMatrixWeights.copy().transpose();
        SimpleMatrix visibleInputs = matrixWeightsTransposed.mult(inputVector);
        visibleInputs = visibleInputs.plus(this.mInterceptsVisible);
        return this.logistic(visibleInputs);
    }


    private SimpleMatrix hiddenStateToVisibleInputVariance(SimpleMatrix inputVector) {
        SimpleMatrix matrixWeights2 = this.mMatrixWeights.copy();
        DMatrixIterator matrixWeights2Iterator = matrixWeights2.iterator(
                true,
                0,
                0,
                matrixWeights2.numRows() - 1,
                matrixWeights2.numCols() - 1
        );
        while (matrixWeights2Iterator.hasNext()) {
            double x = matrixWeights2Iterator.next();
            matrixWeights2Iterator.set(x * x);
        }
        SimpleMatrix hiddenVariance = inputVector.copy();
        DMatrixIterator hiddenVarianceIterator = hiddenVariance.iterator(
                true,
                0,
                0,
                hiddenVariance.numRows() - 1,
                hiddenVariance.numCols() - 1
        );
        while (hiddenVarianceIterator.hasNext()) {
            double x = hiddenVarianceIterator.next();
            hiddenVarianceIterator.set(x * (1 - x));
        }

        return matrixWeights2.transpose().mult(hiddenVariance);
    }

    private SimpleMatrix deterministicReconstruction(SimpleMatrix inputVector) {
        SimpleMatrix hiddenProbabilities = this.visibleStateToHiddenProbabilities(inputVector);
        SimpleMatrix visibleProbabilities = this.hiddenStateToVisibleProbabilities(hiddenProbabilities);
        SimpleMatrix visibleInputVariance = this.hiddenStateToVisibleInputVariance(hiddenProbabilities);
        SimpleMatrix temp = visibleProbabilities.copy();

        DMatrixIterator tempIterator = temp.iterator(
                true,
                0,
                0,
                temp.numRows() - 1,
                temp.numCols() - 1
        );
        while (tempIterator.hasNext()) {
            double x = tempIterator.next();
            tempIterator.set(x * (1 - x));
        }

        SimpleMatrix temp2 = visibleProbabilities.copy();
        DMatrixIterator temp2Iterator = temp2.iterator(
                true,
                0,
                0,
                temp2.numRows() - 1,
                temp2.numCols() -1
        );
        while (temp2Iterator.hasNext()) {
            double x = temp2Iterator.next();
            temp2Iterator.set(1 - (x * 2));
        }

        SimpleMatrix varianceMultiplier = temp.elementMult(temp2);

        return visibleProbabilities.plus(varianceMultiplier.elementMult(visibleInputVariance));
    }


    private SimpleMatrix createInputVector(ArrayList<Integer> foodItemsIds, int hour, String timezone) {
        double[] values = new double[459];
        int foodOffset = 425;

        for (int i = 0; i < foodItemsIds.size(); i++) {
            int id = foodItemsIds.get(i);
            int index = Arrays.asList(Constants.FOOD_IDS_FEATURES_ORDERED).indexOf(id);
            if (index != -1) {
                values[i] = 1;
            }
        }

        values[foodOffset + hour] = 1;

        if (Constants.TIMEZONES_FEATURES_ORDERED.containsKey(timezone)) {
            int timezoneIndex = Constants.TIMEZONES_FEATURES_ORDERED.get(timezone);
            values[timezoneIndex] = 1;
        }

        return new SimpleMatrix(459, 1, true, values);
    }

    private ArrayList<Integer> foodItemIdsReconstructFor(SimpleMatrix inputVector, Set<Integer> inputFoodItemIds) {
        SimpleMatrix inputVectorReconstruct = this.deterministicReconstruction(inputVector);
        ArrayList<Double> inputVectorReconstructArray = matrixToArrayList(inputVectorReconstruct);
        ArrayList<Double> probabilitiesValues = new ArrayList<>(inputVectorReconstructArray.subList(0, 425));
        ArrayList<Double> probabilitiesSorted = new ArrayList<>(probabilitiesValues);
        Collections.sort(probabilitiesSorted);
        Collections.reverse(probabilitiesSorted);
        ArrayList<Integer> suggestedFoodItemsIds = new ArrayList<>();
        int index = 0;
        while((suggestedFoodItemsIds.size() < 10) && (index < probabilitiesValues.size())) {
            double doubleInd = probabilitiesSorted.get(index);
            int columnId = probabilitiesValues.indexOf(doubleInd);
            int foodItemId = Constants.FOOD_IDS_FEATURES_ORDERED[columnId];

            if (!inputFoodItemIds.contains(foodItemId)) {
                suggestedFoodItemsIds.add(foodItemId);
            }

            index += 1;
        }

        return suggestedFoodItemsIds;
    }

    private ArrayList<Double> matrixToArrayList(SimpleMatrix matrix) {
        DMatrixIterator sampleArrayIterator = matrix.iterator(
                true,
                0, 0,
                matrix.numRows() - 1,
                matrix.numCols() - 1
        );
        ArrayList<Double> values = new ArrayList<>();
        while (sampleArrayIterator.hasNext()) {
            double x = sampleArrayIterator.next();
            values.add(x);
        }
        return values;
    }

}
