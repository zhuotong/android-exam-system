@ECHO OFF
 
IF %JAVA_HOME% == "" GOTO FROM_PATH
SET JAVA_CMD=%JAVA_HOME%\bin\java
GOTO EXEC
 
:FROM_PATH
SET JAVA_CMD=java
GOTO EXEC
 
:EXEC
SET MEM_ARGS=-Xms128m -Xmx256m
SET CLASS_PATH=lib\timingframework-1.0.jar;lib\swing-worker-1.1.jar;lib\SwingClient-1.0-SNAPSHOT.jar;lib\ClientBase-1.0-SNAPSHOT.jar;lib\appframework-1.03.jar
%JAVA_CMD% %MEM_ARGS% -classpath %CLASS_PATH% com.msxt.client.swing.launcher.ExamLauncher
