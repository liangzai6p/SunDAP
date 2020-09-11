#!/bin/sh
APP_MAIN=com.sunyard.dap.forecast.SunDapFcApplication

tradePortalPID=0
 
getTradeProtalPID(){
    javaps=`jps -l | grep $APP_MAIN`
    if [ -n "$javaps" ]; then
        tradePortalPID=`echo $javaps | awk '{print $1}'`
    else
        tradePortalPID=0
    fi
}

shutdown(){
    getTradeProtalPID
    echo "================================================================================================================"
    if [ $tradePortalPID -ne 0 ]; then
        echo -n "Stopping $APP_MAIN(PID=$tradePortalPID)..."
        kill -9 $tradePortalPID
        if [ $? -eq 0 ]; then
            echo "[Success]"
            echo "================================================================================================================"
        else
            echo "[Failed]"
            echo "================================================================================================================"
        fi
        getTradeProtalPID
        if [ $tradePortalPID -ne 0 ]; then
            shutdown
        fi
    else
        echo "$APP_MAIN is not running"
        echo "================================================================================================================"
    fi
}

shutdown
