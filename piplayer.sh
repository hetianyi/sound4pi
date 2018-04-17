#! /bin/bash
CUR=$PWD
ZOOBIN="${BASH_SOURCE-$0}"
ZOOBIN="$(dirname "${ZOOBIN}")"
ZOOCMD=$ZOOBIN/sound4pi.jar
java -cp $ZOOCMD com.hty.sound4pi.CmdPlayer $*