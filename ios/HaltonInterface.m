#import "HaltonInterface.h"

// NB: Mock to make the project compile.
@implementation HaltonInterface

+ (Matrix *)sampleWithDimension:(int)dimension count:(int)count
{
    NSAssert(false, @"+[HaltonInterface sampleWithDimension:count:] is not implemented");
    exit(0);
}

@end
