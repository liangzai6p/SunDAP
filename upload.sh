#!/usr/bin/env bash
#复制到release
cp -r dataserve/target/SunDAP-dataserve.jar 目标码RELEASE/dataserve/lib
cp -r common/target/SunDap-Common.jar 目标码RELEASE/dataserve/lib

cp -r SunORMS_system/target/SunORMS-system.jar 目标码RELEASE/orms-system/lib
cp -r common/target/SunDap-Common.jar 目标码RELEASE/orms-system/lib


#复制到服务器
echo "=========上传SunDAP-dataserve.jar=========="
scp -P 22 dataserve/target/SunDAP-dataserve.jar root@172.1.1.43:/root/SunDAP/dataserve/lib
echo "=========上传SunDAP-Common.jar到dataserve========="
scp -P 22 common/target/SunDap-Common.jar root@172.1.1.43:/root/SunDAP/dataserve/lib

echo "=========上传SunORMS-system.jar========="
scp -P 22 SunORMS_system/target/SunORMS-system.jar root@172.1.1.43:/root/SunDAP/orms-system/lib

echo "=========上传SunDAP-Common.jar到ORMS========="
scp -P 22 common/target/SunDap-Common.jar root@172.1.1.43:/root/SunDAP/orms-system/lib

echo "=========上传SunDAP-forecast.jar========="
scp -P 22 forecast/target/SunORMS-system.jar root@172.1.1.43:/root/SunDAP/forecast/lib

echo "=========上传SunDAP-Common.jar到ORMS========="
scp -P 22 common/target/SunDap-Common.jar root@172.1.1.43:/root/SunDAP/forecast/lib

