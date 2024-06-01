echo "GET http://localhost:8081/hello" | vegeta attack -duration=20s -rate=2000/1s | vegeta report --type=text
