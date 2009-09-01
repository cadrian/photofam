#!/bin/sh
export MAVEN_OPTS=-Xmx1024m
exec mvn -e -Prun install
