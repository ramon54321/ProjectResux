#!/usr/bin/env bash

if [ -n "$(git status --untracked-files=no --porcelain)" ]
then
  echo -- Git working directory not clean
  exit 1
fi

./build-client.sh
./build-server.sh
./build-http.sh
