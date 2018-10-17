mkdir data1
mkdir data2
mkdir data3
start "8001" mongod --dbpath data1 --port 8001 --replSet rs1 --rest  --httpinterface
start "8002" mongod --dbpath data2 --port 8002 --replSet rs1 --rest  --httpinterface
start "8003" mongod --dbpath data3 --port 8003 --replSet rs1 --rest  --httpinterface
timeout 3
mongo --port 8002

