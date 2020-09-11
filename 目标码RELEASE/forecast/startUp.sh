#!/bin/sh
#-------------------------------------------------------------------------------------------------------------
#该脚本的使用方式为-->[sh startup.sh]
#-------------------------------------------------------------------------------------------------------------
JAVA_OPTS="-Duser.timezone=GMT+8 -server -Xms512m -Xmx512m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp/heapdump.hprof"
APP_LOG=log
APP_HOME=$(cd `dirname $0`; pwd)
APP_MAIN=com.sunyard.dap.forecast.SunDapApplication
CLASSPATH=$APP_HOME
for tradePortalJar in "$APP_HOME"/lib/*.jar;
do
   CLASSPATH="$CLASSPATH":"$tradePortalJar"
done

tradePortalPID=0

getTradeProtalPID(){
    javaps=`jps -l | grep $APP_MAIN`
    if [ -n "$javaps" ]; then
        tradePortalPID=`echo $javaps | awk '{print $1}'`
    else
        tradePortalPID=0
    fi
}

startup(){
    getTradeProtalPID
    echo "================================================================================================================"
    if [ $tradePortalPID -ne 0 ]; then
        echo "$APP_MAIN already started(PID=$tradePortalPID)"
        echo "================================================================================================================"
    else
        echo -n "Starting $APP_MAIN"
        nohup java $JAVA_OPTS -classpath $CLASSPATH $APP_MAIN >/dev/null 2>log.log &
        getTradeProtalPID
        if [ $tradePortalPID -ne 0 ]; then
            echo "(PID=$tradePortalPID)...[Success]"
            echo "================================================================================================================"
        else
            echo "[Failed]"
            echo "================================================================================================================"
        fi
    fi
}

startup
