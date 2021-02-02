#!/usr/bin/env bash

# 사용하지 않는 Profile찾기 (runtime or runtime2)

function find_idle_profile() {
  RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost/profile)

  if [ ${RESPONSE_CODE} -ge 400 ]
  then
    CURRENT_PROFILE=runtime2
  else
    CURRENT_PROFILE=$(curl -s http://localhost/profile)
  fi

  if [ ${CURRENT_PROFILE} == runtime ]
  then
    IDLE_PROFILE=runtime2
  else
    IDLE_PROFILE=runtime
  fi

  echo "${IDLE_PROFILE}"
}

# 사용하지 않는 profile의 port 찾기
function find_idle_port() {
  IDLE_PROFILE=$(find_idle_profile)

  if [ ${IDLE_PROFILE} == runtime ]
  then
    echo "8081"
  else
    echo "8082"
  fi
}