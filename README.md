# android-sdk
Biometrics Android SDK

## Prerequisites

- .aar File: Obtain the .aar file you want to add to your Android Studio project.
- Android Studio: Have Android Studio installed on your development machine.

## Steps

1. Open your Android Studio project.

2. Locate the `libs` directory within your Android Studio project. If the directory does not exist, you can create it manually within the `app` module.

3. Copy the .aar file into the `libs` directory.

4. In the project navigator, navigate to the `build.gradle` file for the `app` module.

5. Inside the `dependencies` block, add the following line to include the .aar file as a dependency:

   ```groovy
   implementation files('libs/your_aar_file_name.aar')
   ```
6. Sync your project with the updated dependencies by clicking the "Sync Now" link in the notification banner that appears at the top of the Android Studio window.
7. Build your project to ensure the .aar file is properly added and linked to your app.
