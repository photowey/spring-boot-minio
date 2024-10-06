@echo off
setlocal enableDelayedExpansion

echo run path: !cd!
cd !cd!

set target_version=%1
echo --- prepare update version to %target_version%... ---

mvn versions:set -DprocessAllModules=true -DgenerateBackupPoms=false -DnewVersion=%target_version%
mvn versions:update-child-modules -DgenerateBackupPoms=false
mvn versions:commit

endlocal
