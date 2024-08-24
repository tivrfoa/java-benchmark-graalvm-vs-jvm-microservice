buildApi:
	go build -o bin/ client-api/client-api.go

buildGo:
	cd go && go build -o bin/

api:
	./bin/client-api

goService:
	./go/bin/acme

javaQuarkus:
	java -jar quarkus/target/quarkus-app/quarkus-run.jar

graalvm:
	./bin/code-with-quarkus-1.0.0-SNAPSHOT-runner

vertx:
	java -jar java-vertx-starter/target/starter-1.0.0-SNAPSHOT-fat.jar


k6:
	k6 run k6_bench1.js

k6DB:
	k6 run k6_bench_db.js

wrkJSON:
	wrk -L -t 10 -d 20 -c 100 -R 2000 http://localhost:8081/hello

wrkDB:
	wrk -L -t 10 -d 20 -c 100 -R 2000 http://localhost:8081/db

wrkQuarkusNonBlocking:
	wrk -L -t 10 -d 20 -c 100 -R 2000 http://localhost:8081/hello/NonBlocking

vegeta:
	echo "GET http://localhost:8081/hello" | vegeta attack -duration=20s -rate=2000/1s | vegeta report --type=text

vegetaQuarkusNonBlocking:
	echo "GET http://localhost:8081/hello/NonBlocking" | vegeta attack -duration=20s -rate=2000/1s | vegeta report --type=text

