# react-native-rbm-suggested

## Getting started

`$ npm install react-native-rbm-suggested --save`

### Mostly automatic installation

`$ react-native link react-native-rbm-suggested`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-rbm-suggested` and add `RbmSuggested.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRbmSuggested.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainApplication.java`
  - Add `import com.rbmsuggested.RbmSuggestedPackage;` to the imports at the top of the file
  - Add `new RbmSuggestedPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-rbm-suggested'
  	project(':react-native-rbm-suggested').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-rbm-suggested/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-rbm-suggested')
  	```


## Usage
```javascript
import RbmSuggested from 'react-native-rbm-suggested';

// TODO: What to do with the module?
RbmSuggested;
```
---

#### `init()`;
Do not forget to call `RBMSuggested.init();` in the initial load.

