#!/bin/bash

chmod +x ./scripts/server_stop.sh
chmod +x ./scripts/server_start.sh

rm /home/ubuntu/server/*.jar

mv /home/ubuntu/server/build/libs/*.jar /home/ubuntu/server
