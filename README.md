# Mandelbrot Fractals Explorer

Project uses Kotlin/Native and WASM to render to HTML5 canvas.

## Setup

Download latest Kotlin/Native bundle. For example if its called kotlin-native-macos-1.4.32 place it under /kotlin-native-macos-1.4.32 in the root of the project.
Next, make sure the ```kotlin.native.home``` variable is set correctly under /wasmApp/gradle.properties

## Build Kotlin

```bash
cd app
../gradlew assemble
```

## Clean project

```bash
../gradlew clean
```

## Deploy to CDN

You only need to host /wasmApp/index.html and /wasmApp/build