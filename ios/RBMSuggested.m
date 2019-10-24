#import "React/RCTBridgeModule.h"

@interface RCT_EXTERN_MODULE(RBMSuggested, NSObject)

RCT_EXTERN_METHOD(
  init: (RCTPromiseResolveBlock)resolve
  rejecter: (RCTPromiseRejectBlock)reject
)

RCT_EXTERN_METHOD(
  suggestedFoodItemFor: (NSArray<NSNumber *> *)items
  date: (NSDate *)date
  timezone:(NSString *)string
  resolve: (RCTPromiseResolveBlock)resolve
  rejecter: (RCTPromiseRejectBlock)reject
)

@end
