#!/bin/bash

# Configuración
DOCKER_USERNAME="dashshot" # Cambia esto por tu nombre de usuario en Docker Hub
DOCKER_REPO="webchat_agc"  # Cambia esto por el nombre de tu repositorio en Docker Hub
IMAGE_TAG="Local_Test"  # Puedes cambiar "latest" por otro tag si lo necesitas
IMAGE_NAME="$DOCKER_USERNAME/$DOCKER_REPO:$IMAGE_TAG"

# Paso 1: Construir la imagen Docker
echo "Construyendo la imagen Docker..."
docker build -t $IMAGE_NAME .

# Paso 2: Iniciar sesión en Docker Hub (si no lo has hecho ya)
echo "Iniciando sesión en Docker Hub..."
docker login || { echo "Error al iniciar sesión en Docker Hub"; exit 1; }

# Paso 3: Subir la imagen a Docker Hub
echo "Subiendo la imagen a Docker Hub..."
docker push $IMAGE_NAME || { echo "Error al subir la imagen a Docker Hub"; exit 1; }

# Confirmación de éxito
echo "La imagen $IMAGE_NAME se ha construido y subido a Docker Hub correctamente."
