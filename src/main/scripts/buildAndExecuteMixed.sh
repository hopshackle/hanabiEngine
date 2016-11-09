#! /bin/bash
##
# Make the damn thing work on the yarcc cluster
#
# Repeatable runs mode - with multiple runs
##
set -e

VERSION=0.1.0
SUBJECT=webpigeon
JAR_FILE=fireworks-0.1-SNAPSHOT.jar
JOB_FILE=mixed.job

# params
export FIREWORKS_NUM_SEEDS=200

# check the repo is clean (local changes mean pull could fail)
if [[ -n $(git status -s) ]]
then
	echo "[ERROR] working directory not clean, aborting run"
	exit 1
fi

##
# build phase: create the jar file
##

# ok - update to the latest from the server
git pull

# build phase: clean build the project
export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk
module load maven
mvn clean package

# check the repo is (still) clean
if [[ -n $(git status -s) ]]
then
	echo "[ERROR] working directory not clean, aborting run"
	exit 1
fi

##
# task generation phase: place the jar and generate tasks
##
GIT_COMMIT=$(git rev-parse HEAD)

# Create a folder to drop stuff into
TASK_DIR=`pwd`/results/tasks/$GIT_COMMIT/`date -I`
mkdir -p $TASK_DIR

# drop the stuff we need in it
cp target/$JAR_FILE $TASK_DIR/$JAR_FILE
cp main/scripts/$JOB_FILE $TASK_DIR/$JOB_FILE

# Build the game runner
$JAVA_HOME/bin/java -cp target/fireworks-0.1-SNAPSHOT.jar com.fossgalaxy.games.fireworks.cluster.GenerateGames > $TASK_DIR/mixedArgs.txt

