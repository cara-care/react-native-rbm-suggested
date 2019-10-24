import Foundation
import UIKit

@objc(RBMSuggested) class RBMSuggested: NSObject, RCTBridgeModule {

    static let alcoholIds = [3589, 3587, 2993]
    // MARK: Properties
    var matrix_weights = Matrix()
    var matrix_intercepts_hidden = Matrix()
    var matrix_intercepts_visible = Matrix()
    
    static var instance: RBMSuggested?
    
    // MARK: Init
    
    static func getInstance() -> RBMSuggested {
        objc_sync_enter(RBMSuggested.self)
        defer {
            objc_sync_exit(RBMSuggested.self)
        }
        
        if instance == nil {
            instance = RBMSuggested()
            instance!.initMatrix()
        }
        return instance!
    }
    
    // MARK : DEBUG
    var probasList = [Double]()
    
    // MARK: Private functions
    
    func initMatrix() {
        DispatchQueue.global(qos: .default).async {
            let weights = self.matrixValuesFrom(textfile: "weights_v5")
            let intercepts_hidden = self.matrixValuesFrom(textfile: "intercept_hidden_v5")
            let intercepts_visible = self.matrixValuesFrom(textfile: "intercept_visible_v5")
            
            DispatchQueue.main.async {
                self.matrix_weights = Matrix(from: weights, rows: 130, columns: 459)
                self.matrix_intercepts_hidden = Matrix(from: intercepts_hidden, rows: 130, columns: 1)
                self.matrix_intercepts_visible = Matrix(from: intercepts_visible, rows: 459, columns: 1)
            }
        }
    }
    
    func matrixValuesFrom(textfile path: String) -> [Double] {
        var valuesString: [String]?
        do {
            if let path = Bundle.main.path(forResource: path, ofType: "txt") {
                let data = try String(contentsOfFile: path, encoding: String.Encoding.utf8)
                valuesString = data.components(separatedBy: "\n")
            }
        } catch let err as NSError {
            print(err)
        }
        let valuesDouble = valuesString?.compactMap {
            Double($0)
        }
        return valuesDouble!
    }
    
    func visibleStateToHiddenProbabilities(intputVector: Matrix) -> Matrix {
        var hidden_inputs = matrix_weights.multiplying(withRight: intputVector)!
        hidden_inputs = hidden_inputs.adding(matrix_intercepts_hidden)
        return logistic(inputVector: hidden_inputs)
    }
    
    func hiddenStateToVisibleProbabilities(inputVector: Matrix) -> Matrix {
        var visible_inputs = matrix_weights.transposingAndMultiplying(withRight: inputVector)!
        visible_inputs = visible_inputs.adding(matrix_intercepts_visible)
        return logistic(inputVector: visible_inputs)
    }
    
    func hiddenStateToVisibleInputVariance(inputVector: Matrix) -> Matrix {
        let matrix_weights_2 = matrix_weights.applyingFunction {
            return $0 * $0
        }
        let hidden_variance = inputVector.applyingFunction {
            return $0 * (1 - $0)
        }
        let visible_input_variance = matrix_weights_2?.transposingAndMultiplying(withRight: hidden_variance)
        return visible_input_variance!
    }
    
    func logistic(inputVector: Matrix) -> Matrix {
        let probabilities = inputVector.applyingFunction { element -> Double in
            return 1.0 / (1.0 + exp(-element))
        }
        return probabilities!
    }
    
    func deterministicReconstruction(inputVector: Matrix) -> Matrix {
        let hidden_probabilities = visibleStateToHiddenProbabilities(intputVector: inputVector)
        let visible_probabilities = hiddenStateToVisibleProbabilities(inputVector: hidden_probabilities)
        let visible_input_variance = hiddenStateToVisibleInputVariance(inputVector: hidden_probabilities)
        let temp = visible_probabilities.applyingFunction {
            return $0 * (1 - $0)
        }
        let temp2 = visible_probabilities.applyingFunction {
            return 1 - ($0 * 2)
        }
        let variance_multiplier = temp?.byElementWiseMultiply(with: temp2)
        let probabilities_final = visible_probabilities.adding(variance_multiplier?.byElementWiseMultiply(with: visible_input_variance)!)
        return probabilities_final!
    }
    
    func createInputVector(foodItemIds: [Int], timezone: String, date: Date) -> Matrix {
        let calendar = Calendar.current
        var values = [Double](repeating: 0, count: 459)
        for foodItemId in foodItemIds {
            if let index = RBMConstants.foodIdsFeaturesOrdered.firstIndex(of: foodItemId) {
                values[index] = 1
            }
        }
        let foodOffset = 425
        values[foodOffset + calendar.component(.hour, from: date)] = 1
        if let index = RBMConstants.timeZonesFeaturesOrdered[timezone] {
            values[index] = 1
        }
        
        // ********************************* SET THE VECTOR HERE TO FORCE A DEBUG VALUE ******************************************* //
        //        values = [Double](repeating: 0, count: 459)
        //        values[79] = 1
        //        values[102] = 1
        //        values[34] = 1
        //        values[433] = 1
        //        values[458] = 1
        // ********************************* SET THE VECTOR HERE TO FORCE A DEBUG VALUE ******************************************* //
        
        let inputVector = Matrix(from: values, rows: 459, columns: 1)
        return inputVector!
    }
    
    func foodItemIdsReconstructFor(inputVector: Matrix, inputFoodItemIds: Set<Int>) -> [Int] {
        let inputVectorReconstruct = deterministicReconstruction(inputVector: inputVector)
        let probaValues = (inputVectorReconstruct.numberArray as! [Double])[0...424]
        let probaValuesOrdered = probaValues.sorted {
            $0 > $1
        }
        var suggestedFoodItemsIds = [Int]()
        var index = 0
        while ((suggestedFoodItemsIds.count < 10) && (index < probaValues.count)) {
            let proba = probaValuesOrdered[index]
            let columnId = probaValues.firstIndex(of: proba)!
            let foodId = RBMConstants.foodIdsFeaturesOrdered[columnId]
            if !(inputFoodItemIds.contains(foodId)) {
                suggestedFoodItemsIds.append(foodId)
            }
            index += 1
        }
        return suggestedFoodItemsIds
    }
    
    // MARK: React Native
    @objc static func moduleName() -> String! {
        return "RBMSuggested"
    }
    
    @objc static func requiresMainQueueSetup() -> Bool {
      return false
    }
    
    // MARK: Public functions
    
    @objc func `init`(_ resolve: RCTPromiseResolveBlock
        , rejecter reject: RCTPromiseRejectBlock) {
        let _ = RBMSuggested.getInstance()
        resolve(nil)
    }
    
    @objc func suggestedFoodItemFor(_ foodItemIds: [NSNumber]
        , date: Date
        , timezone: String
        , resolve: RCTPromiseResolveBlock
        , rejecter reject: RCTPromiseRejectBlock) {
        
        let inputVector = RBMSuggested.getInstance().createInputVector(foodItemIds: foodItemIds as! [Int]
            , timezone: timezone
            , date: date)
        let setValues: [Int] = foodItemIds as! [Int] + RBMSuggested.alcoholIds
        resolve(RBMSuggested.getInstance().foodItemIdsReconstructFor(inputVector: inputVector
        , inputFoodItemIds: Set(setValues)) as! [NSNumber])
    }
    
}
