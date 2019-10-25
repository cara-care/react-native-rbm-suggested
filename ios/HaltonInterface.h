#import <Foundation/Foundation.h>
@class Matrix;

// NB: Mock to make the project compile.
@interface HaltonInterface : NSObject

+ (Matrix *)sampleWithDimension:(int)dimension count:(int)count;

@end
