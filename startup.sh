echo "--------- maven package  ---------"
cd ./comments-tree-backend
mvn clean && mvn package


echo "--------- run docker  ---------"
cd ../
docker-compose up -d

echo "--------- open browser  ---------"
open 'http://localhost:8888'