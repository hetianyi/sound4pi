#! /bin/bash
source /etc/profile
echo $JAVA_HOME
ZOOBIN="${BASH_SOURCE-$0}"
ZOOBIN="$(dirname "${ZOOBIN}")"
ZOOCMD=$ZOOBIN/sound4pi.jar
ZOOCONF=$ZOOBIN/sound.properties
ZOOLOG=$ZOOBIN/play.log
echo "All log information append to log file '$ZOOLOG'."
nohup java -jar $ZOOCMD $ZOOCONF > $ZOOLOG &
