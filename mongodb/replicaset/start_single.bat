mkdir data0
start "8000" mongod --dbpath data0 --port 8000
timeout 3
mongo --port 8000