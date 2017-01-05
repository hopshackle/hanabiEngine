#! /bin/bash
set -e
#params
export GENERATOR_ARGS="inner 2"
export FIREWORKS_REPEAT_COUNT=1
export FIREWORKS_NUM_SEEDS=100

./buildAndExecuteValdiation.sh

