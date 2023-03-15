"use strict";(self.webpackChunktouchlab=self.webpackChunktouchlab||[]).push([[479],{3905:(e,n,t)=>{t.d(n,{Zo:()=>c,kt:()=>g});var a=t(7294);function o(e,n,t){return n in e?Object.defineProperty(e,n,{value:t,enumerable:!0,configurable:!0,writable:!0}):e[n]=t,e}function r(e,n){var t=Object.keys(e);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(e);n&&(a=a.filter((function(n){return Object.getOwnPropertyDescriptor(e,n).enumerable}))),t.push.apply(t,a)}return t}function i(e){for(var n=1;n<arguments.length;n++){var t=null!=arguments[n]?arguments[n]:{};n%2?r(Object(t),!0).forEach((function(n){o(e,n,t[n])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(t)):r(Object(t)).forEach((function(n){Object.defineProperty(e,n,Object.getOwnPropertyDescriptor(t,n))}))}return e}function s(e,n){if(null==e)return{};var t,a,o=function(e,n){if(null==e)return{};var t,a,o={},r=Object.keys(e);for(a=0;a<r.length;a++)t=r[a],n.indexOf(t)>=0||(o[t]=e[t]);return o}(e,n);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);for(a=0;a<r.length;a++)t=r[a],n.indexOf(t)>=0||Object.prototype.propertyIsEnumerable.call(e,t)&&(o[t]=e[t])}return o}var l=a.createContext({}),u=function(e){var n=a.useContext(l),t=n;return e&&(t="function"==typeof e?e(n):i(i({},n),e)),t},c=function(e){var n=u(e.components);return a.createElement(l.Provider,{value:n},e.children)},p={inlineCode:"code",wrapper:function(e){var n=e.children;return a.createElement(a.Fragment,{},n)}},d=a.forwardRef((function(e,n){var t=e.components,o=e.mdxType,r=e.originalType,l=e.parentName,c=s(e,["components","mdxType","originalType","parentName"]),d=u(t),g=o,h=d["".concat(l,".").concat(g)]||d[g]||p[g]||r;return t?a.createElement(h,i(i({ref:n},c),{},{components:t})):a.createElement(h,i({ref:n},c))}));function g(e,n){var t=arguments,o=n&&n.mdxType;if("string"==typeof e||o){var r=t.length,i=new Array(r);i[0]=d;var s={};for(var l in n)hasOwnProperty.call(n,l)&&(s[l]=n[l]);s.originalType=e,s.mdxType="string"==typeof e?e:o,i[1]=s;for(var u=2;u<r;u++)i[u]=t[u];return a.createElement.apply(null,i)}return a.createElement.apply(null,t)}d.displayName="MDXCreateElement"},1255:(e,n,t)=>{t.r(n),t.d(n,{assets:()=>l,contentTitle:()=>i,default:()=>p,frontMatter:()=>r,metadata:()=>s,toc:()=>u});var a=t(7462),o=(t(7294),t(3905));const r={id:"bugsnag"},i="Bugsnag",s={unversionedId:"bugsnag",id:"bugsnag",title:"Bugsnag",description:"If you use Bugsnag and an uncaught exception gets thrown from shared Kotlin code running on iOS, the crash report generated",source:"@site/docs/BugsnagTutorial.md",sourceDirName:".",slug:"/bugsnag",permalink:"/docs/bugsnag",draft:!1,editUrl:"https://github.com/touchlab/CrashKiOS/tree/main/website/docs/BugsnagTutorial.md",tags:[],version:"current",lastUpdatedBy:"Russell Wolf",lastUpdatedAt:1678912470,formattedLastUpdatedAt:"Mar 15, 2023",frontMatter:{id:"bugsnag"},sidebar:"tutorialSidebar",previous:{title:"Crashlytics",permalink:"/docs/crashlytics"}},l={},u=[{value:"Step 1 - Add Bugsnag to Your Apps",id:"step-1---add-bugsnag-to-your-apps",level:2},{value:"Step 2 - Add CrashKiOS",id:"step-2---add-crashkios",level:2},{value:"Step 3 - Setup Dynamic Linking (Optional)",id:"step-3---setup-dynamic-linking-optional",level:2},{value:"Sending Extra Info to Bugsnag",id:"sending-extra-info-to-bugsnag",level:2}],c={toc:u};function p(e){let{components:n,...t}=e;return(0,o.kt)("wrapper",(0,a.Z)({},c,t,{components:n,mdxType:"MDXLayout"}),(0,o.kt)("h1",{id:"bugsnag"},"Bugsnag"),(0,o.kt)("p",null,"If you use Bugsnag and an uncaught exception gets thrown from shared Kotlin code running on iOS, the crash report generated\nwon't be very helpful in determining the cause, see ",(0,o.kt)("a",{parentName:"p",href:"/docs/misc/THE_PROBLEM"},"The Problem")," for more details. CrashKiOS was made\nto remedy this issue and provide meaningful stack traces for Kotlin crashes. "),(0,o.kt)("h2",{id:"step-1---add-bugsnag-to-your-apps"},"Step 1 - Add Bugsnag to Your Apps"),(0,o.kt)("p",null,"First you'll need to set up Bugsnag for your Android and iOS individually. Follow the steps in the ",(0,o.kt)("a",{parentName:"p",href:"https://docs.bugsnag.com/platforms/"},"bugsnag docs"),"\nto set up Bugsnag with your Android app the normal way then do the same for your iOS app. "),(0,o.kt)("p",null,"Make sure to force a crash on both apps from non-shared code and ensure the crash shows up on the Bugsnag Console.\nIf you're testing a crash on an iOS simulator, you'll need to hit run to build your changes, then close and reopen the app. Otherwise,\nXcode will catch and swallow your crash and it won't get reported. You'll also need to reopen the app after crashing it because\ncrash reports are sent at app startup. You'll also need to make sure you aren't crashing the app at app start which can cause it to\nnot get reported. Make sure the crash is triggered by some action like a button click. Also don't forget to set up automatic\ndSYM uploading, so you can see proper stack traces for Swift code."),(0,o.kt)("h2",{id:"step-2---add-crashkios"},"Step 2 - Add CrashKiOS"),(0,o.kt)("p",null,"Once you've verified Bugsnag is working for both platforms, you can add the CrashKiOS dependency to ",(0,o.kt)("inlineCode",{parentName:"p"},"commonMain")," in your\nshared module. "),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-kotlin"},'val commonMain by sourceSets.getting {\n    dependencies {\n        api("co.touchlab.crashkios:bugsnag:0.8.2") // More on why api later\n    }\n}\n')),(0,o.kt)("p",null,"Then disable caching in your ",(0,o.kt)("inlineCode",{parentName:"p"},"gradle.properties")," file. We're currently working to update things to avoid this, for now\nwe need it to deal with iOS linking issues."),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre"},"kotlin.native.cacheKind.iosX64=none\nkotlin.native.cacheKind.iosSimulatorArm64=none\n")),(0,o.kt)("p",null,"After a Gradle sync, make a call to ",(0,o.kt)("inlineCode",{parentName:"p"},"enableBugsnag()")," somewhere in your startup code for each app. This switches from the\ndefault no-op implementations to real implementations which avoid issues in testing. Depending on how the ",(0,o.kt)("inlineCode",{parentName:"p"},"cacheKind")," issue\nabove is fixed this may no longer be necessary. "),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-kotlin"},'Bugsnag.sendHandledException(Exception("Some exception"))\n')),(0,o.kt)("p",null,"On iOS, there's a few more things to do at startup for Bugsnag to work. These are all wrapped in a helper function ",(0,o.kt)("inlineCode",{parentName:"p"},"startBugsnag(config: BugsnagConfiguration)"),".\nWhere ",(0,o.kt)("inlineCode",{parentName:"p"},"config")," is a configuration created in Swift for your Bugsnag setup. If you don't need to do any other config for Bugsnag\nyou can create an empty config like this: "),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-swift"},"let config = BugsnagConfiguration.loadConfig()\n")),(0,o.kt)("p",null,"Then, in your shared module's ",(0,o.kt)("inlineCode",{parentName:"p"},"build.gradle")," you can expose the CrashKiOS api to Swift and call ",(0,o.kt)("inlineCode",{parentName:"p"},"startBugsnag()")," directly.\nThis is why we need to add the CrashKiOS dependency with ",(0,o.kt)("inlineCode",{parentName:"p"},"api()")," rather than ",(0,o.kt)("inlineCode",{parentName:"p"},"implementation()")),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-kotlin"},'cocoapods {\n    framework {\n        export("co.touchlab.crashkios:bugsnag:0.8.2")\n    }\n    ...\n}\n')),(0,o.kt)("p",null,"Now that it's exported, you should be able to call ",(0,o.kt)("inlineCode",{parentName:"p"},"startBugsnag()")," from app startup in Swift: "),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-swift"},"let config = BugsnagConfiguration.loadConfig()\nBugsnagConfigKt.startBugsnag(config: config)\n")),(0,o.kt)("p",null,"Once this is done, throw an uncaught exception from Kotlin code on iOS. Reminder this needes to happen through user action,\nnot in startup code, and you need to relaunch the app after building and after crashing to send the report. The crash should show\nup on the dashboard and should now have Kotlin line numbers "),(0,o.kt)("h2",{id:"step-3---setup-dynamic-linking-optional"},"Step 3 - Setup Dynamic Linking (Optional)"),(0,o.kt)("p",null,"If you're ok with using static frameworks for your shared code, you're done with setup. If you want to export a dynamic framework then you'll see an error like this: "),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre"},'Undefined symbols for architecture x86_64:\n  "_OBJC_CLASS_$_BugsnagFeatureFlag", referenced from:\n      objc-class-ref in libco.touchlab.crashkios:bugsnag-cache.a(result.o)\n  "_OBJC_CLASS_$_BugsnagStackframe", referenced from:\n      objc-class-ref in libco.touchlab.crashkios:bugsnag-cache.a(result.o)\n  "_OBJC_CLASS_$_BugsnagError", referenced from:\n      objc-class-ref in libco.touchlab.crashkios:bugsnag-cache.a(result.o)\n  "_OBJC_CLASS_$_Bugsnag", referenced from:\n      objc-class-ref in libco.touchlab.crashkios:bugsnag-cache.a(result.o)\n      objc-class-ref in libco.touchlab:kermit-bugsnag-cache.a(result.o)\nld: symbol(s) not found for architecture x86_64\n')),(0,o.kt)("p",null,"This is because on iOS only the definitions for Bugsnag are added when comiling the Kotlin code and building the framework. The binary (the actual Bugsnag library) isn't added until later when you build the iOS app. When building a dynamic framework, the Kotlin compile expects to be able to resolve everything, so you'll see the above error because Bugsnag isn't there yet."),(0,o.kt)("p",null,"To work around this, we need to tell the compiler that these symbols are find and will be there later. Doing it manually is a bit messy so you can just add our gradle plugin to handle it "),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-kotlin"},'  id("co.touchlab.crashkios.bugsnaglink") version "0.8.2"\n')),(0,o.kt)("h2",{id:"sending-extra-info-to-bugsnag"},"Sending Extra Info to Bugsnag"),(0,o.kt)("p",null,"CrashKiOS-bugsnag also provides shared code wrappers for manually sending info to Bugsnag. When there is a crash,\nthese will show up in with the crash in the dashboard"),(0,o.kt)("pre",null,(0,o.kt)("code",{parentName:"pre",className:"language-kotlin"},'BugsnagKotlin.logMessage("Some message")\nBugsnagKotlin.sendHandledException(Exception("Some exception"))\nBugsnagKotlin.sendFatalException(Exception("Some exception"))\nBugsnagKotlin.setCustomValue("someKey", "someValue")\n')))}p.isMDXComponent=!0}}]);