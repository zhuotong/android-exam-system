#!/bin/bash
CLASS_PATH=lib/timingframework-1.0.jar:lib/swing-worker-1.1.jar:lib/SwingClient-1.0-SNAPSHOT.jar:lib/ClientBase-1.0-SNAPSHOT.jar:lib/appframework-1.03.jar 
${JAVA_HOME}/bin/java -classpath $CLASS_PATH com.msxt.client.swing.launcher.ExamLauncher