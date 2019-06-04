#!/usr/bin/env sh
# set -e

if [ "$1" = 'build' ]
then

  ./gradlew build
  # ./gradlew jar

elif [ "$1" = 'test' ]
then

  ./gradlew test --stacktrace

elif [ "$1" = 'coverage' ]
then

  ./gradlew test jacocoTestReport jacocoTestCoverageVerification --stacktrace

elif [ "$1" = 'docs' ]
then

  ./gradlew javadoc

elif [ "$1" = 'publish' ]
then

  ./gradlew publish

else

    exec "$@"

fi
