#! /bin/bash
set -e
#params
export GENERATOR_ARGS="vandenbergh 3"
export FIREWORKS_REPEAT_COUNT=1
export FIREWORKS_NUM_SEEDS=100000

./buildAndExecuteValdiation.sh
