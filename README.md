自动化拨测平台

部署机器：
10.10.220.151(公网：124.42.103.148， ssh端口2202)

部署说明（由于抓包的存在，直接部署可能会丢失抓包进程，导致抓包文件无法停止越来越大）：
1.停止机器上的service-atp服务；
2.重启机器；
3.启动H5程序（在/home/urcs/AutoTest-Web/下命令：npm start）
4.配置DNS（因为这个文件内容重启机器可能会丢失），在/etc/resolv.conf中添加：
nameserver 114.114.114.114
nameserver 8.8.8.8

5.使用salt部署service-atp服务；