#!/bin/bash
set -e

FX_MODULES="javafx.controls,javafx.graphics,javafx.base"

mkdir -p "build/bin"
cp java_fx_modules/linux-25.0.1/lib/*.so build/bin/
cp -r java_fx_modules/linux-25.0.1/lib build/lib

# 3. JLink
echo "3. Criando JRE customizado..."
jlink \
    --module-path "java_fx_modules/linux-25.0.1/lib" \
    --add-modules ${FX_MODULES} \
    --output "build/runtime"


