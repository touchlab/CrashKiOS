#import <objc/runtime.h>
#import <Private/BugsnagHandledState.h>

BOOL NSExceptionKt_BugsnagHandledStateOriginalUnhandledValue(BugsnagHandledState* self, SEL _cmd) {
    return self.unhandled;
}

// In Bugsnag 6.26.2 and above we need to override the originalUnhandledValue property.
// By default it will prevent our exceptions from being stored to disk.
// https://github.com/bugsnag/bugsnag-cocoa/pull/1549
void NSExceptionKt_OverrideBugsnagHandledStateOriginalUnhandledValue() {
    Method method = class_getInstanceMethod([BugsnagHandledState class], @selector(originalUnhandledValue));
    method_setImplementation(method, (IMP)NSExceptionKt_BugsnagHandledStateOriginalUnhandledValue);
}