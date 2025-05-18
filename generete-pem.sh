#!/bin/bash

OUTPUT_DIR="./src/main/resources/keys/"

mkdir -p "$OUTPUT_DIR"

openssl genpkey -algorithm RSA -out "$OUTPUT_DIR/private-key.pem" -pkeyopt rsa_keygen_bits:2048

openssl rsa -pubout -in "$OUTPUT_DIR/private-key.pem" -out "$OUTPUT_DIR/public-key.pem"

echo "✅ Chaves RSA geradas com sucesso em: $OUTPUT_DIR"
