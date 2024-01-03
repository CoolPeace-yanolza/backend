#!/bin/bash

JAR=coolPeace-0.0.1-SNAPSHOT.jar
LOG=/dev/null
#LOG=coolpeace-console.log
export spring_profiles_active=prod

PID=`ps -ef | grep java | grep jar | awk '{print $2}'`
if [ -n "$PID" ]
then
  echo "=====spring is running at" $PID "Shutdown spring now"
  kill -9 $PID
else
  echo "=====spring isn't running====="
fi

cd ~/server
nohup java -jar $JAR > $LOG 2>&1 &
