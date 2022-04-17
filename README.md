# Android-Notes-App

To Start

Step 1: Create a new project through 
[Firebase Console](https://firebase.google.com/)

Step 2: Setup Firebase project to use with Android

Step 3: Copy the google-services.json file generated from firebase in android studio app folder

Steps to Setup the Services

1. Authentication
* Connect Android Application to firebase by navigating to Tools->FireBase->Authentication->Add the firebase Authentication SDK to your app
* Add the dependency *implementation 'com.google.firebase:firebase-auth:21.0.2'* to the build gradle file

2. RealTime Databases
* Connect Android Application to firebase by navigating to Tools->FireBase->Realtime Datbase->Add the firebase RealTime Database SDK to your app
* Add the dependency *implementation 'com.google.firebase:firebase-database:20.0.4'* to the build gradle file

3. Storage
* Connect Android Application to firebase by navigating to Tools->FireBase->Storage->Add the firebase Storage SDK to your app
* Add the dependency *implementation 'com.google.firebase:firebase-storage:20.0.1'* to the build gradle file

3. Analytics
* Connect Android Application to firebase by navigating to Tools->FireBase->Anaytics->Add the firebase Analytics SDK to your app
* Add the dependency *implementation 'com.google.firebase:firebase-analytics:20.1.1'* to the build gradle file


