#!/bin/sh
# ----------------------------------------------------------------------------
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#    https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.
# ----------------------------------------------------------------------------

# ----------------------------------------------------------------------------
# Apache Maven Wrapper startup batch script, version 3.3.2
#
# Optional ENV vars
# -----------------
#   JAVA_HOME - location of a JDK home dir, required when type is jdk
#   MVNW_REPOURL - repo url base for downloading maven distribution
#   MVNW_USERNAME/MVNW_PASSWORD - user and password for downloading maven
#   MVNW_VERBOSE - true: enable verbose log; others: silence the output
# ----------------------------------------------------------------------------

set -euf
[ "${MVNW_VERBOSE-}" != true ] || set -x

# OS specific support.
native_path() { printf '%s\n' "$1"; }
case "$(uname)" in
CYGWIN* | MINGW*)
  [ -z "${JAVA_HOME-}" ] || JAVA_HOME="$(cygpath --unix "$JAVA_HOME")"
  native_path() { cygpath --path --windows "$1"; }
  ;;
esac

# set JAVACMD and JAVACCMD
set_java_home() {
  # For Cygwin and MinGW, ensure paths are in Unix format before anything is touched
  if [ -n "${JAVA_HOME-}" ]; then
    if [ -x "$JAVA_HOME/jre/sh/java" ]; then
      # IBM's JDK on AIX uses strange locations for the executables
      JAVACMD="$JAVA_HOME/jre/sh/java"
      JAVACCMD="$JAVA_HOME/jre/sh/javac"
    else
      JAVACMD="$JAVA_HOME/bin/java"
      JAVACCMD="$JAVA_HOME/bin/javac"

      if [ ! -x "$JAVACMD" ] || [ ! -x "$JAVACCMD" ]; then
        echo "The JAVA_HOME environment variable is not defined correctly, so mvnw cannot run." >&2
        echo "JAVA_HOME is set to \"$JAVA_HOME\", but \"\$JAVA_HOME/bin/java\" or \"\$JAVA_HOME/bin/javac\" does not exist." >&2
        return 1
      fi
    fi
  else
    JAVACMD="$(
      'set' +e
      'unset' -f command 2>/dev/null
      'command' -v java
    )" || :
    JAVACCMD="$(
      'set' +e
      'unset' -f command 2>/dev/null
      'command' -v javac
    )" || :

    if [ ! -x "${JAVACMD-}" ]; then
      echo "The java(1) command could not be found in your PATH, please set" >&2
      echo "the JAVA_HOME environment variable ..." >&2
      return 1
    fi
    if [ ! -x "${JAVACCMD-}" ]; then
      echo "The javac(1) command could not be found in your PATH, please set" >&2
      echo "the JAVA_HOME environment variable ..." >&2
      return 1
    fi
  fi
}

# hash string like '1234567890abcdef', format and return as string checksum
hash_string() {
  str="${1:-}" h=0
  while [ -n "$str" ]; do
    char="${str%"${str#?}"}"
    h=$(((h * 31 + $(LC_CTYPE=C printf '%d' "'$char")) % 4294967296))
    str="${str#?}"
  done
  printf '%08x\n' $h
}

verbose() { :; }
[ "${MVNW_VERBOSE-}" != true ] || verbose() { printf '%s\n' "${1-}"; }

die() {
  printf '%s\n' "$1" >&2
  exit 1
}

trim() {
  # Trims leading and trailing whitespace
  local var="$*"
  var="${var#"${var%%[![:space:]]*}"}"
  var="${var%"${var##*[![:space:]]}"}"
  printf '%s' "$var"
}

# parse distributionUrl and optional distributionSha256Sum, wrapperUrl and optional wrapperSha256Sum
while IFS="=" read -r key value; do
  case "${key-}" in
  distributionUrl) distributionUrl=$(trim "${value-}") ;;
  distributionSha256Sum) distributionSha256Sum=$(trim "${value-}") ;;
  wrapperUrl) wrapperUrl=$(trim "${value-}") ;;
  wrapperSha256Sum) wrapperSha256Sum=$(trim "${value-}") ;;
  esac
done <"${0%/*}/.mvn/wrapper/maven-wrapper.properties"

[ -n "${distributionUrl-}" ] || die "cannot read distributionUrl property in ${0%/*}/.mvn/wrapper/maven-wrapper.properties"

case "${distributionUrl##*/}" in
apache-maven-*-bin.zip | apache-maven-*-bin.tar.gz) : ;;
*) die "distributionUrl is not valid, must be 'apache-maven-<version>-bin.zip' or 'apache-maven-<version>-bin.tar.gz'" ;;
esac

# find distributionVersion
distributionVersion="${distributionUrl##*/apache-maven-}"
distributionVersion="${distributionVersion%%-bin.*}"
distributionDirectory="apache-maven-${distributionVersion}"

# find wrapperVersion
wrapperVersion="${wrapperUrl##*/maven-wrapper-}"
wrapperVersion="${wrapperVersion%%.jar}"

# find the maven home
# ~/.m2/wrapper/dists/apache-maven-<version>/
if [ "${MVNW_REPOURL-}" ]; then
  distributionUrl="${MVNW_REPOURL}/org/apache/maven/apache-maven/${distributionVersion}/apache-maven-${distributionVersion}-bin.zip"
  wrapperUrl="${MVNW_REPOURL}/org/apache/maven/wrapper/maven-wrapper/${wrapperVersion}/maven-wrapper-${wrapperVersion}.jar"
fi

distributionHash=$(hash_string "${distributionUrl}")
wrapperHash=$(hash_string "${wrapperUrl}")

# find maven home
maven_home="${MAVEN_USER_HOME:-${HOME}/.m2}/wrapper/dists/${distributionDirectory}/${distributionHash}"
maven_exec="${maven_home}/bin/mvn"
wrapperJar="${MAVEN_USER_HOME:-${HOME}/.m2}/wrapper/dists/maven-wrapper/${wrapperHash}/maven-wrapper-${wrapperVersion}.jar"

# download wrapperJar if not found
if [ ! -f "${wrapperJar}" ]; then
  wrapperJarDir="${wrapperJar%/*}"
  if [ ! -d "${wrapperJarDir}" ]; then
    verbose "Creating directory: ${wrapperJarDir}"
    mkdir -p "${wrapperJarDir}"
  fi
  verbose "Downloading: ${wrapperUrl}"
  if ! command -v wget >/dev/null 2>&1; then
    if ! command -v curl >/dev/null 2>&1; then
      die "Neither 'wget' nor 'curl' found, cannot download wrapper"
    fi
    verbose "Found curl, will use it"
    if [ -z "${MVNW_USERNAME-}" ] || [ -z "${MVNW_PASSWORD-}" ]; then
      curl -fsSL -o "${wrapperJar}" "${wrapperUrl}" || die "Could not download ${wrapperUrl}"
    else
      curl -fsSL --user "${MVNW_USERNAME}:${MVNW_PASSWORD}" -o "${wrapperJar}" "${wrapperUrl}" || die "Could not download ${wrapperUrl}"
    fi
  else
    verbose "Found wget, will use it"
    if [ -z "${MVNW_USERNAME-}" ] || [ -z "${MVNW_PASSWORD-}" ]; then
      wget -q -O "${wrapperJar}" "${wrapperUrl}" || die "Could not download ${wrapperUrl}"
    else
      wget -q --http-user="${MVNW_USERNAME}" --http-password="${MVNW_PASSWORD}" -O "${wrapperJar}" "${wrapperUrl}" || die "Could not download ${wrapperUrl}"
    fi
  fi
fi

# download maven if not found
if [ ! -d "${maven_home}" ]; then
  mavenParentDir="${maven_home%/*}"
  [ -d "${mavenParentDir}" ] || mkdir -p "${mavenParentDir}"
  verbose "Downloading: ${distributionUrl}"
  tmp_download_dir=$(mktemp -d)
  trap 'rm -rf -- "${tmp_download_dir}"' EXIT
  tmp_file="${tmp_download_dir}/apache-maven-${distributionVersion}-bin.zip"
  if ! command -v wget >/dev/null 2>&1; then
    if ! command -v curl >/dev/null 2>&1; then
      die "Neither 'wget' nor 'curl' found, cannot download maven distribution"
    fi
    verbose "Found curl, will use it"
    if [ -z "${MVNW_USERNAME-}" ] || [ -z "${MVNW_PASSWORD-}" ]; then
      curl -fsSL -o "${tmp_file}" "${distributionUrl}" || die "Could not download ${distributionUrl}"
    else
      curl -fsSL --user "${MVNW_USERNAME}:${MVNW_PASSWORD}" -o "${tmp_file}" "${distributionUrl}" || die "Could not download ${distributionUrl}"
    fi
  else
    verbose "Found wget, will use it"
    if [ -z "${MVNW_USERNAME-}" ] || [ -z "${MVNW_PASSWORD-}" ]; then
      wget -q -O "${tmp_file}" "${distributionUrl}" || die "Could not download ${distributionUrl}"
    else
      wget -q --http-user="${MVNW_USERNAME}" --http-password="${MVNW_PASSWORD}" -O "${tmp_file}" "${distributionUrl}" || die "Could not download ${distributionUrl}"
    fi
  fi
  unzip -q "${tmp_file}" -d "${mavenParentDir}"
fi

exec "${maven_exec}" "$@"
