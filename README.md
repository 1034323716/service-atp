自动化拨测平台

部署机器：
10.10.220.151(公网：124.42.103.148， ssh端口2202)

部署说明（由于抓包的存在，直接部署可能会丢失抓包进程，导致抓包文件无法停止越来越大，所以要重启机器）：
1.停止机器上的service-atp服务；
2.重启机器(若使用jps看到没有AtpAppBootstrap在运行则可以直接跳到第6步)；
3.启动H5程序（在/home/urcs/AutoTest-Web/下命令：npm start）
4.配置DNS（因为这个文件内容重启机器可能会丢失），在/etc/resolv.conf中添加：
nameserver 114.114.114.114
nameserver 8.8.8.8

5.运行提供下载抓包和日志能力的程序
nohup  ./home/urcs/HttpFs/run.sh > /home/urcs/HttpFs/HttpFs.log 2>&1 &

6.使用salt部署service-atp服务；

修改client-atp-开头的模块时，若想使修改应用到用例中，需要修改用例中依赖的client-atp-*包的版本，
一般用例都放在Auto-Test-Module项目，且依赖的都是小版本