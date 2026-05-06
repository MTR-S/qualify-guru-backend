#!/bin/bash
echo "Inicializando o LocalStack..."
awslocal s3 mb s3://qualify-guru-base-cvs
echo "Bucket S3 criado com sucesso!"