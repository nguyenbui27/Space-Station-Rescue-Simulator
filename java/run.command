#!/bin/zsh
cd "$(dirname "$0")"
mkdir -p build
javac -d build *.java || exit 1
java -cp build Controller
