#!/usr/bin/env bash

echo Building Client

rm -rf build/client
mkdir -p build/client

sbt -error mainJS/clean mainJS/fastOptJS

cp js/target/scala-2.12/client-jsdeps.js build/client/sux_dependencies.js
cp js/target/scala-2.12/client-fastopt.js build/client/sux_client.js
cp js/target/scala-2.12/client-fastopt.js.map build/client/client-fastopt.js.map

cp js/resources/index.html build/client/index.html
