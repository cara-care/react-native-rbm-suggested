package com.reactlibrary;

import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReadableArray;

import org.ejml.simple.SimpleMatrix;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
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
        if (this.mRbmSuggested == null) {
            this.initMatrix();
            this.mRbmSuggested = this;
        }

        promise.resolve(true);
    }


    @ReactMethod
    public void suggestedFoodItemFor(final ReadableArray foodItemsIds, Integer hour, String timezone, Promise promise) {
        ArrayList<Integer> foodItemsIdsArrayList = new ArrayList<>();

        // TODO: CREATE A UTIL FUNCTION TO convert ReadableArray to ArrayList ??
        for (int i = 0; i < foodItemsIds.size(); i++) {
            foodItemsIdsArrayList.add(foodItemsIds.getInt(i));
        }

        SimpleMatrix inputVector = this.createInputVector(foodItemsIdsArrayList, hour, timezone);
        Set<Integer> setValues = new HashSet<>();
    }

    private void initMatrix() {
        // weights matrix
        double[] weightsArray = this.matrixValuesFrom("weights_v5.txt");
        SimpleMatrix weightsMatrix = new SimpleMatrix(weightsArray.length,1, false, weightsArray);
        weightsMatrix.reshape(130, 459);
        this.mMatrixWeights = weightsMatrix;

        // intercept hidden
        double[] interceptsHiddenArray = this.matrixValuesFrom("intercept_hidden_v5.txt");
        this.mInterceptsHidden = new SimpleMatrix(130, 1, false, interceptsHiddenArray);

        // intercept visible
        double[] interceptsVisibleArray = this.matrixValuesFrom("intercept_visible_v5.txt");
        this.mInterceptsVisible = new SimpleMatrix(130, 1, false, interceptsVisibleArray);
    }

    private double[] matrixValuesFrom(String filename) {
        BufferedReader bufferedReader = null;
        ArrayList<String> matrixValuesArrayList = new ArrayList<>();

        try {
            // FIXME: getAssets() won't work?
            bufferedReader = new BufferedReader(new InputStreamReader(getAssets().open(filename), "UTF-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                matrixValuesArrayList.add(line);
                // Log.i("RbmSuggestedModule",line);
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

    private SimpleMatrix createInputVector(ArrayList<Integer> foodItemsIds, int hour, String timezone) {
        // TODO: FINISH FUNCTION IMPLEMENTATION
        double[] values = {459};
        int foodOffset = 425;

        for (int i = 0; i < foodItemsIds.size(); i++) {
            // FIXME: JAVA being java
            int index = Arrays.asList(Constants.FOOD_IDS_FEATURES_ORDERED).indexOf(foodItemsIds[i]);;
            if (index != -1) {
                values[i] = 1;
            }
        }

        values[foodOffset + hour] = 1;


        if (Constants.TIMEZONES_FEATURES_ORDERED.containsKey(timezone)) {
            int timezoneIndex = Constants.TIMEZONES_FEATURES_ORDERED.get(timezone);
            values[timezoneIndex] = 1;
        }

        double[][] inputVectorMatrixData = {values, {}};
        SimpleMatrix inputVector = new SimpleMatrix(inputVectorMatrixData);
        inputVector.reshape(459, 1);
        return inputVector;
    }
}
