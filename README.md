# Pi Emoji Skin

Android app project for preparing a custom PNG skin for the 🥷 emoji.

## Important technical limitation
A normal third-party APK cannot directly replace the emoji graphics rendered by Gboard or Android's system emoji font. This project therefore provides the safe, buildable PNG workflow and keeps Gboard itself untouched. System-wide replacement requires a supported system/font mechanism or privileged integration and is not silently claimed here.

## Replace the sample
Replace `app/src/main/res/drawable-nodpi/ninja_sample.png` with your own PNG. Keep the filename `ninja_sample.png` or update the drawable reference.

## Build
The included GitHub Actions workflow builds a debug APK with Gradle and uploads it as an artifact.
