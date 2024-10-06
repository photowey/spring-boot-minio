#!/usr/bin/env bash

set -e

echo "run path: $(pwd)"
cd "$(pwd)"

target_version=$1
echo "--- prepare update version to ${target_version}... ---"

mvn versions:set -DprocessAllModules=true -DgenerateBackupPoms=false -DnewVersion="${target_version}"
mvn versions:update-child-modules -DgenerateBackupPoms=false
mvn versions:commit
