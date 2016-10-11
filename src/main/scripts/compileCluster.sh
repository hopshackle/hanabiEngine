#! /bin/bash
##
# Make the damn thing work on the yarcc cluster
##
export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk.x86_64
git pull
module load maven
mvn clean package
export FIREWORKS_NUM_SEEDS=250
$JAVA_HOME/bin/java -cp target/fireworks-0.1-SNAPSHOT.jar com.fossgalaxy.games.fireworks.cluster.GenerateGames > mixedArgs.txt
$JAVA_HOME/bin/java -cp target/fireworks-0.1-SNAPSHOT.jar com.fossgalaxy.games.fireworks.cluster.GeneratePureGames > pureArgs.txt
wc --lines mixedArgs.txt
wc --lines pureArgs.txt
