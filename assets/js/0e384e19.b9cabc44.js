"use strict";(self.webpackChunktouchlab=self.webpackChunktouchlab||[]).push([[671],{3905:(e,t,n)=>{n.d(t,{Zo:()=>u,kt:()=>h});var a=n(7294);function l(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function o(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(e);t&&(a=a.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,a)}return n}function r(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?o(Object(n),!0).forEach((function(t){l(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):o(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function i(e,t){if(null==e)return{};var n,a,l=function(e,t){if(null==e)return{};var n,a,l={},o=Object.keys(e);for(a=0;a<o.length;a++)n=o[a],t.indexOf(n)>=0||(l[n]=e[n]);return l}(e,t);if(Object.getOwnPropertySymbols){var o=Object.getOwnPropertySymbols(e);for(a=0;a<o.length;a++)n=o[a],t.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(l[n]=e[n])}return l}var s=a.createContext({}),c=function(e){var t=a.useContext(s),n=t;return e&&(n="function"==typeof e?e(t):r(r({},t),e)),n},u=function(e){var t=c(e.components);return a.createElement(s.Provider,{value:t},e.children)},p={inlineCode:"code",wrapper:function(e){var t=e.children;return a.createElement(a.Fragment,{},t)}},g=a.forwardRef((function(e,t){var n=e.components,l=e.mdxType,o=e.originalType,s=e.parentName,u=i(e,["components","mdxType","originalType","parentName"]),g=c(n),h=l,d=g["".concat(s,".").concat(h)]||g[h]||p[h]||o;return n?a.createElement(d,r(r({ref:t},u),{},{components:n})):a.createElement(d,r({ref:t},u))}));function h(e,t){var n=arguments,l=t&&t.mdxType;if("string"==typeof e||l){var o=n.length,r=new Array(o);r[0]=g;var i={};for(var s in t)hasOwnProperty.call(t,s)&&(i[s]=t[s]);i.originalType=e,i.mdxType="string"==typeof e?e:l,r[1]=i;for(var c=2;c<o;c++)r[c]=n[c];return a.createElement.apply(null,r)}return a.createElement.apply(null,n)}g.displayName="MDXCreateElement"},9881:(e,t,n)=>{n.r(t),n.d(t,{assets:()=>s,contentTitle:()=>r,default:()=>p,frontMatter:()=>o,metadata:()=>i,toc:()=>c});var a=n(7462),l=(n(7294),n(3905));const o={sidebar_position:1,title:"CrashKiOS Home"},r="CrashKiOS - Crash reporting for Kotlin/iOS",i={unversionedId:"intro",id:"intro",title:"CrashKiOS Home",description:"Thin library that provides symbolicated crash reports for Kotlin code on iOS. Supports sending crashes, and handled exceptions, as well as logging breadcrumbs and custom key/value pairs. Currently supported crash reporting services are Firebase Crashlytics and Bugsnag.",source:"@site/docs/intro.md",sourceDirName:".",slug:"/intro",permalink:"/intro",draft:!1,editUrl:"https://github.com/touchlab/CrashKiOS/tree/main/website/docs/intro.md",tags:[],version:"current",lastUpdatedBy:"Kevin Galligan",lastUpdatedAt:1669913301,formattedLastUpdatedAt:"Dec 1, 2022",sidebarPosition:1,frontMatter:{sidebar_position:1,title:"CrashKiOS Home"},sidebar:"tutorialSidebar",next:{title:"THE_PROBLEM",permalink:"/THE_PROBLEM"}},s={},c=[{value:"Crashlytics Usage",id:"crashlytics-usage",level:2},{value:"Testing",id:"testing",level:3},{value:"Linking",id:"linking",level:3},{value:"Crashlytics Sample",id:"crashlytics-sample",level:3},{value:"Bugsnag Usage",id:"bugsnag-usage",level:2},{value:"iOS Only",id:"ios-only",level:3},{value:"Option 1: Manual Calls",id:"option-1-manual-calls",level:4},{value:"Option 2: Helper Calls",id:"option-2-helper-calls",level:4},{value:"Using the Library",id:"using-the-library",level:3},{value:"Testing",id:"testing-1",level:3},{value:"Linking",id:"linking-1",level:3},{value:"Bugsnag Sample",id:"bugsnag-sample",level:3},{value:"NSExceptionKt",id:"nsexceptionkt",level:2},{value:"Getting Help",id:"getting-help",level:2}],u={toc:c};function p(e){let{components:t,...n}=e;return(0,l.kt)("wrapper",(0,a.Z)({},u,n,{components:t,mdxType:"MDXLayout"}),(0,l.kt)("h1",{id:"crashkios---crash-reporting-for-kotlinios"},"CrashKiOS - Crash reporting for Kotlin/iOS"),(0,l.kt)("p",null,"Thin library that provides symbolicated crash reports for Kotlin code on iOS. Supports sending crashes, and handled exceptions, as well as logging breadcrumbs and custom key/value pairs. Currently supported crash reporting services are ",(0,l.kt)("a",{parentName:"p",href:"https://firebase.google.com/"},"Firebase Crashlytics")," and ",(0,l.kt)("a",{parentName:"p",href:"https://www.bugsnag.com/"},"Bugsnag"),"."),(0,l.kt)("p",null,"To use crash reporting with general logging support, check out ",(0,l.kt)("a",{parentName:"p",href:"https://github.com/touchlab/Kermit/"},"Kermit"),"."),(0,l.kt)("p",null,"If you're wondering ",(0,l.kt)("em",{parentName:"p"},"why")," you need this library, please see ",(0,l.kt)("a",{parentName:"p",href:"/THE_PROBLEM"},"the problem"),"."),(0,l.kt)("blockquote",null,(0,l.kt)("h2",{parentName:"blockquote",id:"subscribe"},"Subscribe!"),(0,l.kt)("p",{parentName:"blockquote"},"We build solutions that get teams started smoothly with Kotlin Multiplatform Mobile and ensure their success in production. Join our community to learn how your peers are adopting KMM.\n",(0,l.kt)("a",{parentName:"p",href:"https://go.touchlab.co/newsletter-gh"},"Sign up here"),"!")),(0,l.kt)("h2",{id:"crashlytics-usage"},"Crashlytics Usage"),(0,l.kt)("p",null,"Add the dependency."),(0,l.kt)("pre",null,(0,l.kt)("code",{parentName:"pre",className:"language-kotlin"},'val commonMain by sourceSets.getting {\n    dependencies {\n        implementation("co.touchlab.crashkios:crashlytics:0.8.1")\n    }\n}\n')),(0,l.kt)("p",null,"The library by default has noop implementations of the crash logging calls. This is because in test situations you generally don't want to interact with the crash logging. On iOS specifically, this will allow you to run tests without needing to link against the Crashlytics runtime library."),(0,l.kt)("p",null,"As a result, in the live app you need to initialize CrashKiOS. For both Android and iOS, you must call the following:"),(0,l.kt)("pre",null,(0,l.kt)("code",{parentName:"pre",className:"language-kotlin"},"enableCrashlytics()\n")),(0,l.kt)("p",null,"You sould generally do this as part of app initialization, after you make the calls to start Crashlytics itself."),(0,l.kt)("p",null,"On iOS, you should also set the unhandled exception hook:"),(0,l.kt)("pre",null,(0,l.kt)("code",{parentName:"pre",className:"language-kotlin"},"setCrashlyticsUnhandledExceptionHook()\n")),(0,l.kt)("p",null,"Once initialized, you call methods on ",(0,l.kt)("inlineCode",{parentName:"p"},"CrashlyticsKotlin"),", from common code or platform-specific code."),(0,l.kt)("pre",null,(0,l.kt)("code",{parentName:"pre",className:"language-kotlin"},'CrashlyticsKotlin.logMessage("Some message")\nCrashlyticsKotlin.sendHandledException(Exception("Some exception"))\nCrashlyticsKotlin.sendFatalException(Exception("Some exception"))\nCrashlyticsKotlin.setCustomValue("someKey", "someValue")\n')),(0,l.kt)("h3",{id:"testing"},"Testing"),(0,l.kt)("p",null,"Your test code should not call ",(0,l.kt)("inlineCode",{parentName:"p"},"enableCrashlytics()"),". Before calling ",(0,l.kt)("inlineCode",{parentName:"p"},"enableCrashlytics()"),", calls to ",(0,l.kt)("inlineCode",{parentName:"p"},"CrashlyticsKotlin")," are all no-ops. Also, on iOS, avoiding ",(0,l.kt)("inlineCode",{parentName:"p"},"enableCrashlytics()")," means you don't need to worry about Crashlytics linker issues."),(0,l.kt)("h3",{id:"linking"},"Linking"),(0,l.kt)("p",null,"If you are using dynamic frameworks, you'll see a linker error when building your framework."),(0,l.kt)("pre",null,(0,l.kt)("code",{parentName:"pre"},'Undefined symbols for architecture x86_64:\n  "_OBJC_CLASS_$_FIRStackFrame", referenced from:\n      objc-class-ref in result.o\n  "_OBJC_CLASS_$_FIRExceptionModel", referenced from:\n      objc-class-ref in result.o\n  "_OBJC_CLASS_$_FIRCrashlytics", referenced from:\n      objc-class-ref in result.o\n  "_FIRCLSExceptionRecordNSException", referenced from:\n      _co_touchlab_crashkios_crashlytics_FIRCLSExceptionRecordNSException_wrapper0 in result.o\nld: symbol(s) not found for architecture x86_64\n')),(0,l.kt)("p",null,"To resolve this, you should tell the linker that Crashlytics will be added later. You can do that directly, or you can use our Gradle plugin. It will find all Xcode Frameworks being built by Kotlin and add the necessary linker arguments."),(0,l.kt)("pre",null,(0,l.kt)("code",{parentName:"pre",className:"language-kotlin"},'plugins {\n  id("co.touchlab.crashkios.crashlyticslink") version "0.8.1"\n}\n')),(0,l.kt)("h3",{id:"crashlytics-sample"},"Crashlytics Sample"),(0,l.kt)("p",null,"See ",(0,l.kt)("a",{parentName:"p",href:"https://github.com/touchlab/CrashKiOS/tree/main/samples/sample-crashlytics"},"samples/sample-crashlytics"),"."),(0,l.kt)("h2",{id:"bugsnag-usage"},"Bugsnag Usage"),(0,l.kt)("p",null,"Add the dependency."),(0,l.kt)("pre",null,(0,l.kt)("code",{parentName:"pre",className:"language-kotlin"},'val commonMain by sourceSets.getting {\n    dependencies {\n        implementation("co.touchlab.crashkios:bugsnag:0.8.1")\n    }\n}\n')),(0,l.kt)("p",null,"The library by default has noop implementations of the crash logging calls. This is because in test situations you generally don't want to interact with the crash logging. On iOS specifically, this will allow you to run tests without needing to link against the Bugsnag runtime library."),(0,l.kt)("p",null,"As a result, in the live app you need to initialize CrashKiOS. For both Android and iOS, you must call the following:"),(0,l.kt)("pre",null,(0,l.kt)("code",{parentName:"pre",className:"language-kotlin"},"enableBugsnag()\n")),(0,l.kt)("p",null,"You sould generally do this as part of app initialization, after you make the calls to start Bugsnag itself."),(0,l.kt)("h3",{id:"ios-only"},"iOS Only"),(0,l.kt)("p",null,"Bugsnag is somewhat more complex than Crashlytics on iOS. On startup, the library needs to suppress an extra error report from being sent. That requires some extra calls, or you can use a helper function that will handle everything."),(0,l.kt)("p",null,"The detailed calls you need to make are the following:"),(0,l.kt)("p",null,"In the iOS init, before starting Bugsnag, you need to call ",(0,l.kt)("inlineCode",{parentName:"p"},"configureBugsnag")," with an instance of ",(0,l.kt)("inlineCode",{parentName:"p"},"BugsnagConfiguration"),". The simplest way to get ",(0,l.kt)("inlineCode",{parentName:"p"},"BugsnagConfiguration")," from Swift is by calling:"),(0,l.kt)("pre",null,(0,l.kt)("code",{parentName:"pre",className:"language-swift"},"let config = BugsnagConfiguration.loadConfig()\n")),(0,l.kt)("h4",{id:"option-1-manual-calls"},"Option 1: Manual Calls"),(0,l.kt)("p",null,"Call ",(0,l.kt)("inlineCode",{parentName:"p"},"configureBugsnag")," with that config. This ",(0,l.kt)("em",{parentName:"p"},"must")," be called before starting Bugsnag."),(0,l.kt)("pre",null,(0,l.kt)("code",{parentName:"pre",className:"language-swift"},"BugsnagConfigKt.configureBugsnag(config: config)\n")),(0,l.kt)("p",null,"Start Bugsnag"),(0,l.kt)("pre",null,(0,l.kt)("code",{parentName:"pre",className:"language-swift"},"Bugsnag.start(with: config)\n")),(0,l.kt)("p",null,"Then set the default exception handler hook"),(0,l.kt)("pre",null,(0,l.kt)("code",{parentName:"pre",className:"language-swift"},"BugsnagConfigKt.setBugsnagUnhandledExceptionHook()\n")),(0,l.kt)("p",null,"If you haven't done so, call:"),(0,l.kt)("pre",null,(0,l.kt)("code",{parentName:"pre",className:"language-swift"},"BugsnagConfigKt.enableBugsnag()\n")),(0,l.kt)("h4",{id:"option-2-helper-calls"},"Option 2: Helper Calls"),(0,l.kt)("p",null,"You can call a single function that performs the 4 steps above."),(0,l.kt)("pre",null,(0,l.kt)("code",{parentName:"pre",className:"language-swift"},"BugsnagConfigKt.startBugsnag(config: config)\n")),(0,l.kt)("p",null,"That function calls ",(0,l.kt)("inlineCode",{parentName:"p"},"configureBugsnag"),", ",(0,l.kt)("inlineCode",{parentName:"p"},"Bugsnag.start"),", ",(0,l.kt)("inlineCode",{parentName:"p"},"setBugsnagUnhandledExceptionHook"),", and ",(0,l.kt)("inlineCode",{parentName:"p"},"enableBugsnag()"),"."),(0,l.kt)("h3",{id:"using-the-library"},"Using the Library"),(0,l.kt)("p",null,"Once initialized, you call methods on ",(0,l.kt)("inlineCode",{parentName:"p"},"BugsnagKotlin")),(0,l.kt)("pre",null,(0,l.kt)("code",{parentName:"pre",className:"language-kotlin"},'BugsnagKotlin.logMessage("Some message")\nBugsnagKotlin.sendHandledException(Exception("Some exception"))\nBugsnagKotlin.sendFatalException(Exception("Some exception"))\nBugsnagKotlin.setCustomValue("someKey", "someValue")\n')),(0,l.kt)("h3",{id:"testing-1"},"Testing"),(0,l.kt)("p",null,"Your test code should not call ",(0,l.kt)("inlineCode",{parentName:"p"},"enableBugsnag()"),". Before calling ",(0,l.kt)("inlineCode",{parentName:"p"},"enableBugsnag()"),", calls to ",(0,l.kt)("inlineCode",{parentName:"p"},"BugsnagKotlin")," are all no-ops. Also, on iOS, avoiding ",(0,l.kt)("inlineCode",{parentName:"p"},"enableBugsnag()")," means you don't need to worry about Bugsnag linker issues."),(0,l.kt)("h3",{id:"linking-1"},"Linking"),(0,l.kt)("p",null,"If you are using dynamic frameworks, you'll see a linker error when building your framework."),(0,l.kt)("pre",null,(0,l.kt)("code",{parentName:"pre"},'Undefined symbols for architecture x86_64:\n  "_OBJC_CLASS_$_BugsnagFeatureFlag", referenced from:\n      objc-class-ref in libco.touchlab.crashkios:bugsnag-cache.a(result.o)\n  "_OBJC_CLASS_$_BugsnagStackframe", referenced from:\n      objc-class-ref in libco.touchlab.crashkios:bugsnag-cache.a(result.o)\n  "_OBJC_CLASS_$_BugsnagError", referenced from:\n      objc-class-ref in libco.touchlab.crashkios:bugsnag-cache.a(result.o)\n  "_OBJC_CLASS_$_Bugsnag", referenced from:\n      objc-class-ref in libco.touchlab.crashkios:bugsnag-cache.a(result.o)\n      objc-class-ref in libco.touchlab:kermit-bugsnag-cache.a(result.o)\nld: symbol(s) not found for architecture x86_64\n')),(0,l.kt)("p",null,"To resolve this, you should tell the linker that Bugsnag will be added later. You can do that directly, or you can use our Gradle plugin. It will find all Xcode Frameworks being built by Kotlin and add the necessary linker arguments."),(0,l.kt)("pre",null,(0,l.kt)("code",{parentName:"pre",className:"language-kotlin"},'plugins {\n  id("co.touchlab.crashkios.bugsnaglink") version "0.8.1"\n}\n')),(0,l.kt)("h3",{id:"bugsnag-sample"},"Bugsnag Sample"),(0,l.kt)("p",null,"See ",(0,l.kt)("a",{parentName:"p",href:"https://github.com/touchlab/CrashKiOS/tree/main/samples/sample-bugsnag"},"samples/sample-bugsnag"),"."),(0,l.kt)("h2",{id:"nsexceptionkt"},"NSExceptionKt"),(0,l.kt)("p",null,"CrashKiOS and Kermit previously created 2 reports on a crash because none of the crash reporting clients had an obvious way to do one. ",(0,l.kt)("a",{parentName:"p",href:"https://github.com/rickclephas"},"Rick Clephas")," has done some excellent work figuring that out with ",(0,l.kt)("a",{parentName:"p",href:"https://github.com/rickclephas/NSExceptionKt"},"NSExceptionKt"),". CrashKiOS now uses part of that library as a base and we've merged the cinterop from Kermit and NSExeptionKt to handle crashes as well as breadcrumb values and log statements."),(0,l.kt)("h2",{id:"getting-help"},"Getting Help"),(0,l.kt)("p",null,"CrashKiOS support can be found in the Kotlin Community Slack, ",(0,l.kt)("a",{parentName:"p",href:"http://slack.kotlinlang.org/"},"request access here"),'. Post in the "',(0,l.kt)("a",{parentName:"p",href:"https://kotlinlang.slack.com/archives/CTJB58X7X"},"#touchlab-tools"),'" channel.'),(0,l.kt)("p",null,"For direct assistance, please ",(0,l.kt)("a",{parentName:"p",href:"https://go.touchlab.co/contactus-gh"},"contact Touchlab")," to discuss support options."))}p.isMDXComponent=!0}}]);