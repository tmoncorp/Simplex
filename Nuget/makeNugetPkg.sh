#!/bin/sh


## 파라미터가 없으면 종료 
if [ "$#" -lt 1 ]; then
    echo "$# is Illegal number of parameters."
    echo "Usage: $0 [version=1.0.0] [config=release]"
    exit 1
fi
args=("$@")
config=""
version=""
 
## for loop 를 파라미터 갯수만큼 돌리기 위해 three-parameter loop control 사용
for (( c=0; c<$#; c++ ))
do

    if echo ${args[$c]} | grep -q "version=" ;then
        version=${args[$c]#version=} 
    fi
    if echo ${args[$c]} | grep -q 'config=' ;then
        config=${args[$c]#config=}
    fi
done

## 빌드 타입이 없으면 release로 설정
if [ -z $config ];then
   config="release"
fi

## 버전정보는 필수 입력값
if [ -z $version ];then
    echo "version is required!"
    exit 1
fi

## 입력 내용 출력
echo "version=" $version
echo "config==" $config
##echo "Tmon.Simplex.Platform."$version".nupkg" 

## file 유무 확인
input="y"
if [ -f "./nupkg/Tmon.Simplex.Platform."$version".nupkg" ]; then
    echo "The same file already exists. Do you want to overwrite it? (y/n)"
    read input
fi

## nuget 패키지 파일 생성 
if [ $input == "y" ]; then
    nuget pack Tmon.Simplex.Platform.nuspec -Version $version -OutputDirectory "./nupkg" -Properties configuration=$config
fi