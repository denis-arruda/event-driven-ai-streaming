@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    https://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@REM ----------------------------------------------------------------------------
@REM Apache Maven Wrapper startup batch script, version 3.3.2
@REM ----------------------------------------------------------------------------

@IF "%__MVNW_ARG0_NAME__%"=="" (SET "__MVNW_ARG0_NAME__=%~nx0")
@SET __MVNW_CMD__=
@SET __MVNW_ERROR__=
@SET __MVNW_Gustav_JAVA_HOME=%JAVA_HOME%
@SET JAVA_HOME=

@SETLOCAL

@SET MAVEN_PROJECTBASEDIR=%~dp0

IF NOT "%MAVEN_BASEDIR%" == "" (
  SET MAVEN_PROJECTBASEDIR=%MAVEN_BASEDIR%
)

@SET WRAPPER_PROPERTIES=%MAVEN_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.properties
@IF NOT EXIST "%WRAPPER_PROPERTIES%" (
  ECHO Could not find %WRAPPER_PROPERTIES% 1>&2
  GOTO error
)

@FOR /F "tokens=1,2 delims==" %%A IN (%WRAPPER_PROPERTIES%) DO (
  IF "%%A"=="distributionUrl" SET DISTRIBUTION_URL=%%B
  IF "%%A"=="wrapperUrl" SET WRAPPER_URL=%%B
)

@SET WRAPPER_JAR=%USERPROFILE%\.m2\wrapper\dists\maven-wrapper\maven-wrapper.jar

@IF NOT EXIST "%WRAPPER_JAR%" (
  IF NOT EXIST "%USERPROFILE%\.m2\wrapper\dists\maven-wrapper\" (
    MKDIR "%USERPROFILE%\.m2\wrapper\dists\maven-wrapper\"
  )
  powershell -Command "& {Invoke-WebRequest -Uri '%WRAPPER_URL%' -OutFile '%WRAPPER_JAR%'}"
)

@SET JAVA_HOME=%__MVNW_GUSTAV_JAVA_HOME%

@"%JAVA_HOME%\bin\java.exe" -classpath "%WRAPPER_JAR%" org.apache.maven.wrapper.MavenWrapperMain %DISTRIBUTION_URL% %*
GOTO end

:error
SET __MVNW_CMD__=1

:end
@ENDLOCAL
EXIT /B %__MVNW_CMD__%
