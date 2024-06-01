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

