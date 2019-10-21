declare module "react-native-rbm-suggested" {
  interface RBMSuggested {
    init: () => Promise<void>;
    suggestedFoodItemFor: (
      selectedFoodItemsIds: number[],
      hour: number,
      timezone: string
    ) => Promise<number[]>;
  }
  const RBMSuggested: RBMSuggested;
  export default RBMSuggested;
}
