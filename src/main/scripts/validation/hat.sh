#! /bin/bash
set -e
#params
export GENERATOR_ARGS="hat 5"
export FIREWORKS_REPEAT_COUNT=1
export FIREWORKS_NUM_SEEDS=1000000

./src/main/scripts/validation/buildAndExecuteValdiation.sh

