#!/usr/bin/env bash

BUILD_ENV=$1

if [ "$BUILD_ENV" = "dev" ]
then
  echo Building Client for Development
else
  echo Building Client for Production
fi

rm -rf build/client
mkdir -p build/client

sbt -error mainJS/clean mainJS/fastOptJS

cp js/target/scala-2.12/client-jsdeps.js build/client/sux_dependencies.js
cp js/target/scala-2.12/client-fastopt.js build/client/sux_client.js
cp js/target/scala-2.12/client-fastopt.js.map build/client/client-fastopt.js.map
cp js/resources/index.html build/client/index.html

# Copy Config
if [ "$BUILD_ENV" = "dev" ]
then
  cp js/resources/config_dev.js build/client/sux_config.js
else
  cp js/resources/config.js build/client/sux_config.js
fi

# Inject Git commit into built config
source project.config
COMMIT_HASH=$(git log --pretty=format:"%h" HEAD^1..HEAD)
COMMIT_AUTHOR=$(git log --pretty=format:"%cn" HEAD^1..HEAD)
CLIENT_BUILD_HASH=$(shasum ./build/client/sux_client.js | awk '{split($0,a,"  "); print a[1]}')
BUILD_DATE=$(date '+%d.%m.%Y %H:%M')
sed -i '' "3i\\
\\ \\ version: \"${VERSION}\",\\
\\ \\ commitHash: \"${COMMIT_HASH}\",\\
\\ \\ commitAuthor: \"${COMMIT_AUTHOR}\",\\
\\ \\ clientBuildHash: \"${CLIENT_BUILD_HASH}\",\\
\\ \\ buildDate: \"${BUILD_DATE}\",
" build/client/sux_config.js
