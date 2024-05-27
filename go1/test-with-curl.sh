while true; do
    curl -s localhost:8080/address > /dev/null;
    curl -s localhost:8080/phones > /dev/null;
done
