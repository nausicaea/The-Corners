#!/bin/sh

if [ "$1" = "-f" ]; then
    rm -rf special-models/build special-models/.gradle limlib/build limlib/.gradle build .gradle $HOME/.gradle
fi

# cd special-models; ./gradlew --no-daemon --quiet :clean :build; cd -; 
# cd limlib; ./gradlew --no-daemon --quiet :build; cd -; 
./gradlew --no-daemon --quiet :build
