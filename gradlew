#!/usr/bin/env bash

set -euo pipefail

SCRIPT_DIR="$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)"
GRADLE_VERSION="8.8"
CACHE_DIR="$HOME/.gradle/servicehub-gradle/$GRADLE_VERSION"
GRADLE_HOME="$CACHE_DIR/gradle-$GRADLE_VERSION"
GRADLE_EXE="$GRADLE_HOME/bin/gradle"
ZIP_FILE="$CACHE_DIR/gradle-$GRADLE_VERSION-bin.zip"
GRADLE_URL="https://services.gradle.org/distributions/gradle-$GRADLE_VERSION-bin.zip"

if [ ! -x "$GRADLE_EXE" ]; then
    mkdir -p "$CACHE_DIR"
    if [ ! -f "$ZIP_FILE" ]; then
        curl -fsSL "$GRADLE_URL" -o "$ZIP_FILE"
    fi
    rm -rf "$GRADLE_HOME"
    unzip -q "$ZIP_FILE" -d "$CACHE_DIR"
fi

exec "$GRADLE_EXE" -p "$SCRIPT_DIR" "$@"
