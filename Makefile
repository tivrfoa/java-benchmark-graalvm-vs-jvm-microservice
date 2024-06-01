buildApi:
	go build -o go/bin go/client-api.go

buildGo:
	cd go && go build -o go/bin go/service.go

api:
	./go/bin/client-api

goService:
	./go/bin/service

javaQuarkus:
	java -jar code-with-quarkus/target/quarkus-app/quarkus-run.jar

# TODO javaNativeQuarkus:

# TODO: javaVertx


k6:
	k6 run k6_bench1.js

wrk:
	wrk -L -t 10 -d 20 -c 100 -R 2000 http://localhost:8081/hello

wrkQuarkusNonBlocking:
	wrk -L -t 10 -d 20 -c 100 -R 2000 http://localhost:8081/hello/NonBlocking

vegeta:
	echo "GET http://localhost:8081/hello" | vegeta attack -duration=20s -rate=2000/1s | vegeta report --type=text

vegetaQuarkusNonBlocking:
	echo "GET http://localhost:8081/hello/NonBlocking" | vegeta attack -duration=20s -rate=2000/1s | vegeta report --type=text

