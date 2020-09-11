#!/usr/bin/env bash
sh dataserve/shutDown.sh
sh orms-system/shutDown.sh
sh forecast/shutDown.sh
sh dataserve/startUp.sh
sh orms-system/startUp.sh
sh forecast/startUp.sh
