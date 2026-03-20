#!/bin/bash
set -e

#Executar manualmente -> ./gradlew installDist (ir em Gradle -> Distribuition -> InstallDist)

# Copia apenas os JARs da aplicação, sem os do JavaFX
cp build/install/mable/lib/*.jar build/

APP_NAME="mable"
APP_VERSION="1.0.0"
APP_MAIN_CLASS="code.EntryPoint"
JAR_FILE="mable.jar"

echo "5. Criando instalador .deb..."
jpackage \
    --type deb \
    --input "build" \
    --name "${APP_NAME}" \
    --main-jar "${JAR_FILE}" \
    --main-class "${APP_MAIN_CLASS}" \
    --dest "dist" \
    --runtime-image "build/runtime" \
    --app-version "$APP_VERSION" \
    --java-options '-Djava.library.path=$APPDIR/bin'