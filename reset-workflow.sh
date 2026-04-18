#!/usr/bin/env bash
set -e

POSTGRES_CONTAINER="order-postgres"
KAFKA_CONTAINER="order-kafka"
DB_USER="postgres"
KAFKA_BIN="/opt/kafka/bin/kafka-topics.sh"

RESET_DBS=(
  "order_db"
  "payment_db"
)

TOPICS=(
  "order-created"
  "inventory-reserved"
)

echo "Make sure all Spring Boot apps are stopped before running this."
echo

echo "Dropping workflow databases..."
for db in "${RESET_DBS[@]}"; do
  docker exec -i "$POSTGRES_CONTAINER" psql -U "$DB_USER" -d postgres -c "DROP DATABASE IF EXISTS $db;"
done

echo "Recreating workflow databases..."
for db in "${RESET_DBS[@]}"; do
  docker exec -i "$POSTGRES_CONTAINER" psql -U "$DB_USER" -d postgres -c "CREATE DATABASE $db;"
done

echo "Resetting inventory quantities..."
docker exec -i "$POSTGRES_CONTAINER" psql -U "$DB_USER" -d inventory_db -c "
UPDATE inventory
SET available_quantity = available_quantity + reserved_quantity,
    reserved_quantity = 0;
"

echo "Deleting Kafka topics if they exist..."
for topic in "${TOPICS[@]}"; do
  docker exec -i "$KAFKA_CONTAINER" "$KAFKA_BIN" \
    --bootstrap-server localhost:9092 \
    --delete \
    --if-exists \
    --topic "$topic" || true
done

echo "Recreating Kafka topics..."
for topic in "${TOPICS[@]}"; do
  docker exec -i "$KAFKA_CONTAINER" "$KAFKA_BIN" \
    --bootstrap-server localhost:9092 \
    --create \
    --if-not-exists \
    --topic "$topic" \
    --partitions 1 \
    --replication-factor 1
done

echo
echo "Workflow reset done."
echo "Product data preserved."
echo "Inventory rows preserved and restocked."
echo "Order and payment databases recreated."
echo "Kafka topics recreated."
echo
echo "Next steps:"
echo "1. Restart order-service"
echo "2. Restart inventory-service"
echo "3. Restart payment-service"
echo "4. Restart product-service if needed"
