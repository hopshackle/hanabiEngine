#! /bin/bash
##
# Make the damn thing work on the yarcc cluster.
#
# This version of the script does not use external job files and therefore is more robust to changes.
#
# Repeatable, well defined runs mode
##
set -e

VERSION=1.0.0
SUBJECT=webpigeon

JAR_FILE=fireworks-0.2.2-SNAPSHOT-jar-with-dependencies.jar
BASE_GENERATOR_PACKAGE=com.fossgalaxy.games.fireworks.cluster
BASIC_GENERATOR=GenerateGames
RUNNER_CLASS=com.fossgalaxy.games.fireworks.cluster.MixedAgentGameSingle
MAX_JOB_SIZE=5000
JOB_NAME="Hanabi_Run"

# check the user provided task to run
if [ $# -ge 1 ]; then
    EXPERIMENT=$1
    echo "using $1 as generator..."
else
    echo "MISSING GENERATOR ARG, PLEASE PROVIDE"
    exit 1;
fi

# they may have provided a runner, but might not have...
if [ $# -ge 2 ]; then
    RUNNER_CLASS="com.fossgalaxy.games.fireworks.cluster."$2
    echo "using $RUNNER_CLASS as runner..."
fi

GENERATOR_CLASS=$BASE_GENERATOR_PACKAGE.$EXPERIMENT


# params for generator
export FIREWORKS_NUM_SEEDS=200

# check the repo is clean (local changes mean pull could fail)
if [[ -n $(git status -s) ]]
then
	echo "[ERROR] working directory not clean, aborting run"
	exit 1
fi

##
# setup phase: check the git repo is upto update and generate the folder structure
##
git pull
GIT_COMMIT=$(git rev-parse HEAD)

# Create a folder to drop stuff into
TASK_DIR=`pwd`/results/tasks/`date -I`/$GIT_COMMIT/$EXPERIMENT

# Sanity check - don't permit the same run twice
if [ -d "$TASK_DIR" ]; then
	echo "[ERROR] task directory exists, i would over write data, abort!"
	exit 1
fi

mkdir -p $TASK_DIR

##
# build phase: create jar file
##
echo "building project..."
export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk
module load maven
mvn clean package > $TASK_DIR/build.log
echo "[OK] project built"

##
# Deploy phase: copy/generate the stuff we need to run
##
echo "copying files..."
# drop the stuff we need in our task directory
cp target/$JAR_FILE $TASK_DIR/$JAR_FILE

cat >$TASK_DIR/experiment.job <<END
#$ -cwd
#$ -V
#$ -l h_vmem=4G
#$ -l h_rt=10:00:00
#$ -q iggi-cluster
#$ -o ./results/
#$ -e ./results/

##
# Hanabi cluster
# Accountable, repeatable runs version
#
# This was generated by the buildAndExecuteExperiment Script
##

export MALLOC_ARENA_MAX=4
export FIREWORKS_RUN_COUNT=1
vmArgs="-Xmx1024m -XX:ParallelGCThreads=1"
args=\$(awk NR==\$SGE_TASK_ID args.txt)
java $vmArgs -cp $JAR_FILE $RUNNER_CLASS \$args

END

echo $GIT_COMMIT > $TASK_DIR/commit
mkdir -p $TASK_DIR/results/ # place to put our data :)
echo "[OK] files in place"

##
# Job creation phase: generate arguments
##
echo "generating arguments..."
$JAVA_HOME/bin/java -cp $TASK_DIR/$JAR_FILE $GENERATOR_CLASS > $TASK_DIR/args.txt
ARG_COUNT=($(wc --lines $TASK_DIR/args.txt))
ARG_COUNT=${ARG_COUNT[0]}
echo "[OK] generated $ARG_COUNT setups."

##
# Job submission phase: submit jobs and prey
##
echo "submitting jobs..."
cd $TASK_DIR

CONCURRENT_TASKS=$(($ARG_COUNT<150?$ARG_COUNT:150))

# Run jobs in batches of MAX_JOB_SIZE
for i in `seq 1 $MAX_JOB_SIZE $ARG_COUNT`;
 do
   let LAST_VAL=i+$MAX_JOB_SIZE-1;
   END_VAL=$(($LAST_VAL<$ARG_COUNT?$LAST_VAL:$ARG_COUNT))

   echo "Creating job for $i to $END_VAL"

    # normal jobs
    QLOG=$(qsub -t $i-$END_VAL -tc $CONCURRENT_TASKS -N $JOB_NAME $JOB_FILE)
    echo $QLOG > qsub.$i.log
    echo "[OK] job file submitted: $QLOG"
done

