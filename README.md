# TFG_AITOR

## Despliegue Local

Este documento detalla los pasos necesarios para desplegar el proyecto en un entorno local utilizando Docker.

---

### **Prerequisitos**
Asegúrese de tener Docker instalado en su sistema.  
Puede instalar Docker siguiendo las instrucciones del sitio web oficial:  
[Guía de instalación de Docker](https://docs.docker.com/get-started/get-docker/)

---

### **Pasos para el despliegue**

#### 1. Descargar archivos de configuración
Ejecute los siguientes comandos en su terminal para descargar los archivos necesarios:

```bash
curl -O https://raw.githubusercontent.com/DashShot/TFG_AITOR/refs/heads/main/haproxy.cfg
curl -O https://raw.githubusercontent.com/DashShot/TFG_AITOR/refs/heads/main/fullchain.pem
curl -O https://raw.githubusercontent.com/DashShot/TFG_AITOR/refs/heads/main/docker-compose_local.yml
```

#### 2. Ejecutar Docker Compose
Levante los servicios definidos en el archivo docker-compose_local.yml ejecutando:

```bash
docker compose -f docker-compose_local.yml up
```
