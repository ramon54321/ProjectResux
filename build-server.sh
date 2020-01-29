#!/usr/bin/env bash

echo Building Server

if [ -n "$(git status --untracked-files=no --porcelain)" ]
then
  echo -- Git working directory not clean
  exit 1
fi

rm -rf build/server
mkdir -p build/server

sbt -error mainJVM/clean mainJVM/assembly

cp jvm/target/scala-2.12/sux_server.jar build/server/sux_server.jar
