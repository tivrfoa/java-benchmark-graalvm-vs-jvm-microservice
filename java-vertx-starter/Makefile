buildDocker:
	docker build -t eclipse-temurin-21-vertx:nochecks .

runDocker:
	docker run --cpus 1.0 --memory 250m --network="host" eclipse-temurin-21-vertx:nochecks
