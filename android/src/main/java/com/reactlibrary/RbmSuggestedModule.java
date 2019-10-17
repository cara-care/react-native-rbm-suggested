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
        // weights Matrix
        double[] weightsArray = this.matrixValuesFrom("assets/weights_v5.txt");
        double[][] weightsMatrixData = {weightsArray, {}};
        SimpleMatrix weightsMatrix = new SimpleMatrix(weightsMatrixData);
        weightsMatrix.reshape(130, 459);
        this.mMatrixWeights = weightsMatrix;

        // intercept hidden
        double[] interceptsHiddenArray = this.matrixValuesFrom("assets/intercept_hidden_v5.txt");
        double[][] interceptsHiddenMatrixData = {interceptsHiddenArray, {}};
        SimpleMatrix interceptsHiddenMatrix = new SimpleMatrix(interceptsHiddenMatrixData);
        interceptsHiddenMatrix.reshape(130, 1);
        this.mInterceptsHidden = interceptsHiddenMatrix;

        // intercept visible
        double[] interceptsVisibleArray = this.matrixValuesFrom("assets/intercept_visible_v5.txt");
        double[][] interceptsVisibleMatrixData = {interceptsVisibleArray, {}};
        SimpleMatrix interceptsVisibleMatrix = new SimpleMatrix(interceptsVisibleMatrixData);
        interceptsVisibleMatrix.reshape(130, 1);
        this.mInterceptsVisible = interceptsVisibleMatrix;
    }

    private double[] matrixValuesFrom(String path) {
        File file = new File(path);
        BufferedReader bufferedReader = null;
        ArrayList<String> matrixValuesArrayList = new ArrayList<>();

        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            String line = bufferedReader.readLine();
            matrixValuesArrayList.add(line);
            Log.d("debug",line);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        return new double[matrixValuesArrayList.size()];
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
