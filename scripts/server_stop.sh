#!/bin/bash

PID=`ps -ef | grep java | grep jar | awk '{print $2}'`
if [ -n "$PID" ]
then
  echo "=====spring is running at" $PID "Shutdown spring now"
  kill -9 $PID
else
  echo "=====spring isn't running====="
fi
