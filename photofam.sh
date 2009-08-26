#!/bin/sh
cd photofam-ui
export MAVEN_OPTS=-Xmx1024m
exec mvn exec:java
