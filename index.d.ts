declare module "@cara-care/react-native-rbm-suggested" {
  export interface IRBMSuggested {
    init: () => Promise<void>;
    suggestedFoodItemFor: (
      selectedFoodItemsIds: number[],
      hour: number,
      timezone: string
    ) => Promise<number[]>;
  }
  const RBMSuggested: IRBMSuggested;
  export default RBMSuggested;
}
