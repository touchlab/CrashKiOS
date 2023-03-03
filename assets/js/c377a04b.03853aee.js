"use strict";(self.webpackChunktouchlab=self.webpackChunktouchlab||[]).push([[971],{3905:(e,t,r)=>{r.d(t,{Zo:()=>p,kt:()=>d});var n=r(7294);function a(e,t,r){return t in e?Object.defineProperty(e,t,{value:r,enumerable:!0,configurable:!0,writable:!0}):e[t]=r,e}function o(e,t){var r=Object.keys(e);if(Object.getOwnPropertySymbols){var n=Object.getOwnPropertySymbols(e);t&&(n=n.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),r.push.apply(r,n)}return r}function i(e){for(var t=1;t<arguments.length;t++){var r=null!=arguments[t]?arguments[t]:{};t%2?o(Object(r),!0).forEach((function(t){a(e,t,r[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(r)):o(Object(r)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(r,t))}))}return e}function s(e,t){if(null==e)return{};var r,n,a=function(e,t){if(null==e)return{};var r,n,a={},o=Object.keys(e);for(n=0;n<o.length;n++)r=o[n],t.indexOf(r)>=0||(a[r]=e[r]);return a}(e,t);if(Object.getOwnPropertySymbols){var o=Object.getOwnPropertySymbols(e);for(n=0;n<o.length;n++)r=o[n],t.indexOf(r)>=0||Object.prototype.propertyIsEnumerable.call(e,r)&&(a[r]=e[r])}return a}var c=n.createContext({}),l=function(e){var t=n.useContext(c),r=t;return e&&(r="function"==typeof e?e(t):i(i({},t),e)),r},p=function(e){var t=l(e.components);return n.createElement(c.Provider,{value:t},e.children)},u={inlineCode:"code",wrapper:function(e){var t=e.children;return n.createElement(n.Fragment,{},t)}},h=n.forwardRef((function(e,t){var r=e.components,a=e.mdxType,o=e.originalType,c=e.parentName,p=s(e,["components","mdxType","originalType","parentName"]),h=l(r),d=a,m=h["".concat(c,".").concat(d)]||h[d]||u[d]||o;return r?n.createElement(m,i(i({ref:t},p),{},{components:r})):n.createElement(m,i({ref:t},p))}));function d(e,t){var r=arguments,a=t&&t.mdxType;if("string"==typeof e||a){var o=r.length,i=new Array(o);i[0]=h;var s={};for(var c in t)hasOwnProperty.call(t,c)&&(s[c]=t[c]);s.originalType=e,s.mdxType="string"==typeof e?e:a,i[1]=s;for(var l=2;l<o;l++)i[l]=r[l];return n.createElement.apply(null,i)}return n.createElement.apply(null,r)}h.displayName="MDXCreateElement"},1269:(e,t,r)=>{r.r(t),r.d(t,{assets:()=>c,contentTitle:()=>i,default:()=>u,frontMatter:()=>o,metadata:()=>s,toc:()=>l});var n=r(7462),a=(r(7294),r(3905));const o={sidebar_position:1,title:"Home"},i="CrashKiOS - Crash reporting for Kotlin/iOS",s={unversionedId:"index",id:"index",title:"Home",description:"Thin library that provides symbolicated crash reports for Kotlin code on iOS. Supports sending crashes, and handled exceptions, as well as logging breadcrumbs and custom key/value pairs. Currently supported crash reporting services are Firebase Crashlytics and Bugsnag.",source:"@site/docs/index.md",sourceDirName:".",slug:"/",permalink:"/docs/",draft:!1,editUrl:"https://github.com/touchlab/CrashKiOS/tree/main/website/docs/index.md",tags:[],version:"current",lastUpdatedBy:"Kevin Galligan",lastUpdatedAt:1677854015,formattedLastUpdatedAt:"Mar 3, 2023",sidebarPosition:1,frontMatter:{sidebar_position:1,title:"Home"},sidebar:"tutorialSidebar",next:{title:"Crashlytics",permalink:"/docs/crashlytics"}},c={},l=[{value:"NSExceptionKt",id:"nsexceptionkt",level:2},{value:"Getting Help",id:"getting-help",level:2}],p={toc:l};function u(e){let{components:t,...r}=e;return(0,a.kt)("wrapper",(0,n.Z)({},p,r,{components:t,mdxType:"MDXLayout"}),(0,a.kt)("h1",{id:"crashkios---crash-reporting-for-kotlinios"},"CrashKiOS - Crash reporting for Kotlin/iOS"),(0,a.kt)("p",null,"Thin library that provides symbolicated crash reports for Kotlin code on iOS. Supports sending crashes, and handled exceptions, as well as logging breadcrumbs and custom key/value pairs. Currently supported crash reporting services are ",(0,a.kt)("a",{parentName:"p",href:"https://firebase.google.com/"},"Firebase Crashlytics")," and ",(0,a.kt)("a",{parentName:"p",href:"https://www.bugsnag.com/"},"Bugsnag"),"."),(0,a.kt)("p",null,"To use crash reporting with general logging support, check out ",(0,a.kt)("a",{parentName:"p",href:"https://github.com/touchlab/Kermit/"},"Kermit"),"."),(0,a.kt)("p",null,"If you're wondering ",(0,a.kt)("em",{parentName:"p"},"why")," you need this library, please see ",(0,a.kt)("a",{parentName:"p",href:"/docs/misc/THE_PROBLEM"},"the problem"),"."),(0,a.kt)("h2",{id:"nsexceptionkt"},"NSExceptionKt"),(0,a.kt)("p",null,"CrashKiOS and Kermit previously created 2 reports on a crash because none of the crash reporting clients had an obvious way to do one. ",(0,a.kt)("a",{parentName:"p",href:"https://github.com/rickclephas"},"Rick Clephas")," has done some excellent work figuring that out with ",(0,a.kt)("a",{parentName:"p",href:"https://github.com/rickclephas/NSExceptionKt"},"NSExceptionKt"),". CrashKiOS now uses part of that library as a base and we've merged the cinterop from Kermit and NSExeptionKt to handle crashes as well as breadcrumb values and log statements."),(0,a.kt)("h2",{id:"getting-help"},"Getting Help"),(0,a.kt)("p",null,"CrashKiOS support can be found in the Kotlin Community Slack, ",(0,a.kt)("a",{parentName:"p",href:"http://slack.kotlinlang.org/"},"request access here"),'. Post in the "',(0,a.kt)("a",{parentName:"p",href:"https://kotlinlang.slack.com/archives/CTJB58X7X"},"#touchlab-tools"),'" channel.'),(0,a.kt)("p",null,"For direct assistance, please ",(0,a.kt)("a",{parentName:"p",href:"https://go.touchlab.co/contactus-gh"},"contact Touchlab")," to discuss support options."))}u.isMDXComponent=!0}}]);