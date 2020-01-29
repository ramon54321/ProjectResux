#!/usr/bin/env bash

echo Building HTTP

if [ -n "$(git status --untracked-files=no --porcelain)" ]
then
  echo -- Git working directory not clean
  exit 1
fi

rm -rf build/http
mkdir -p build/http

cp http/package.json build/http/package.json
cp http/package-lock.json build/http/package-lock.json
cp http/index.js build/http/index.js
