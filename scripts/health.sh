#!/usr/bin/env sh
ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh
source ${ABSDIR}/switch.sh

echo "> Health Check Start!"
echo "> IDLE_PORT: $IDLE_PORT"
echo "> curl -s http://localhost:$IDLE_PORT/profile"
sleep 10

for RETRY_COUNT in {1..10}
do
  RESPONSE=$(curl -s http://localhost:${IDLE_PORT}/profile)
  UP_COUNT=$(echo ${RESPONSE} | grep 'runtime' | wc -l)

  # $UP_COUNT >= 1 ('runtime'이라는 문자열이 있는 지 검증한다.)
  if [ ${UP_COUNT} -ge 1 ]
  then
    echo "> Heath check 성공"
    switch_proxy
    break
  else
    echo "> Health Check의 응답을 알 수 없거나 실행 상태가 아닙니다."
    echo "> Health Check : ${RESPONSE}"
  fi

  if [ ${RETRY_COUNT} -eq 10 ]
  then
    echo "> Health check 실패"
    echo "> Nginx에 연결하지 않고 배포를 종료합니다."
    exit 1
  fi

  echo "> Health check 연결 실패. 재시도..."
  sleep 10
done