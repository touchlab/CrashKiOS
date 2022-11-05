#import <Foundation/Foundation.h>
#import <BugsnagConfiguration.h>
#import <BugsnagEvent.h>

// We need the following wrapper function because of
// https://github.com/JetBrains/kotlin/blob/7bc0132cca92464344ded194f2273c56699f99ca/kotlin-native/runtime/src/main/cpp/ObjCExport.mm#L515
void NSExceptionKt_BugsnagConfigAddOnSendErrorBlock(BugsnagConfiguration* config, BugsnagOnSendErrorBlock block) {
    [config addOnSendErrorBlock:^BOOL(BugsnagEvent * _Nonnull event) {
        return block(event);
    }];
}
