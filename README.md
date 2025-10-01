# YouTube Top (Android)

Android app (Jetpack Compose, Kotlin, Hilt, Retrofit) that shows YouTube most popular videos and lets you filter by:
- Minimum view count
- Minimum channel subscribers
- Channel creation date (created after YYYY-MM-DD)

## Prerequisites
- Java 17
- Android SDK (compileSdk 35)
- Gradle 8.6+ (or use the provided GitHub Actions CI)
- YouTube Data API v3 key

## Configure API key
Add your key to `gradle.properties` or export an env var:

```
YOUTUBE_API_KEY=YOUR_KEY
```

The key is injected into `BuildConfig.YOUTUBE_API_KEY`.

## Build & Run
```
gradle :app:assembleDebug
```

Install the APK from `app/build/outputs/apk/debug/`.

## CI
GitHub Actions workflow `.github/workflows/android.yml` builds the project. Add `YOUTUBE_API_KEY` as a repository secret.
