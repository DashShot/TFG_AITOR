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

## **Despliegue AWS**

Este documento detalla los pasos necesarios para desplegar la infraestructura en AWS utilizando la CLI de AWS.

---

### **Prerequisitos**
1. **Cuenta de AWS**: Asegúrate de tener una cuenta de AWS activa.
2. **AWS CLI**: Instala la AWS CLI. Puedes seguir las [instrucciones oficiales de instalación](https://docs.aws.amazon.com/cli/latest/userguide/install-cliv2.html).
3. **Credenciales de AWS**: Configura tus credenciales ejecutando:
```bash
aws configure
```
4. Par de claves: Necesitas un par de claves para conectar a tu instancia EC2. Si no tienes uno, créalo con el siguiente comando:
```bash
aws ec2 create-key-pair --key-name MyKeyPair --query 'KeyMaterial' --output text > MyKeyPair.pem
chmod 400 MyKeyPair.pem
```
### **Pasos para el despliegue**

#### 1. Crear una VPC
Una VPC (Virtual Private Cloud) es una red virtual que te permite lanzar recursos en una red aislada. Para crearla, ejecuta el siguiente comando: 
```bash
aws ec2 create-vpc --cidr-block 10.0.0.0/16 --region eu-west-3
```
Este comando crea una VPC con un rango de direcciones IP de 10.0.0.0/16 en la región eu-west-3 (París).

#### 2. Crear subnets públicas
Creamos dos subnets públicas, una en la zona de disponibilidad eu-west-3a y otra en eu-west-3b:

Subnet en eu-west-3a:
```bash
aws ec2 create-subnet --vpc-id <vpc-id> --cidr-block 10.0.1.0/24 --availability-zone eu-west-3a
```
Subnet en eu-west-3b:
```bash
aws ec2 create-subnet --vpc-id <vpc-id> --cidr-block 10.0.2.0/24 --availability-zone eu-west-3b
```

#### 3. Crear un Internet Gateway
Un Internet Gateway es necesario para permitir la comunicación entre las instancias en tu VPC y el mundo exterior. Crea uno ejecutando el siguiente comando:

```bash
aws ec2 create-internet-gateway --region eu-west-3
```
Para adjuntar el Internet Gateway a la VPC, utiliza el siguiente comando:
```bash
aws ec2 attach-internet-gateway --vpc-id <vpc-id> --internet-gateway-id <internet-gateway-id>
```
#### 4. Crear una tabla de enrutamiento para las subredes públicas
Para que las instancias dentro de las subredes públicas puedan acceder a Internet, necesitamos una tabla de enrutamiento asociada con estas subredes. Primero, crea una tabla de enrutamiento:

```bash
aws ec2 create-route-table --vpc-id <vpc-id> --region eu-west-3
```
Esto devolverá un ID de tabla de enrutamiento. A continuación, crea una ruta para redirigir el tráfico hacia el Internet Gateway:

```bash
aws ec2 create-route --route-table-id <route-table-id> --destination-cidr-block 0.0.0.0/0 --gateway-id <internet-gateway-id>
```
Ahora, asocia esta tabla de enrutamiento a las subredes públicas que creaste anteriormente. Ejecuta los siguientes comandos:

Asocia la tabla de enrutamiento con la subnet en eu-west-3a:
```bash
aws ec2 associate-route-table --subnet-id <subnet-id-eu-west-3a> --route-table-id <route-table-id>
```
Asocia la tabla de enrutamiento con la subnet en eu-west-3b:
```bash
aws ec2 associate-route-table --subnet-id <subnet-id-eu-west-3b> --route-table-id <route-table-id>
```

#### 6. Crear un Security Group
Un Security Group es necesario para controlar el tráfico que entra y sale de las instancias. En este paso, crearemos un Security Group que permita:

1. Acceso en el puerto 22 para SSH.
2. Acceso en el puerto 443 para HTTPS.
3. Acceso en el puerto 61614 para el broker de ActiveMQ.
4. Acceso en el puerto 27017 para DocumentDB.

Primero, crea el Security Group:
```bash
aws ec2 create-security-group --group-name MySecurityGroup --description "Security group para tráfico de aplicación" --vpc-id <vpc-id>
```
Esto devolverá un GroupId. Usa este ID para agregar las reglas de entrada:
```bash
aws ec2 authorize-security-group-ingress --group-id <security-group-id> --protocol tcp --port 22 --cidr 0.0.0.0/0
aws ec2 authorize-security-group-ingress --group-id <security-group-id> --protocol tcp --port 443 --cidr 0.0.0.0/0
aws ec2 authorize-security-group-ingress --group-id <security-group-id> --protocol tcp --port 61614 --cidr 0.0.0.0/0
aws ec2 authorize-security-group-ingress --group-id <security-group-id> --protocol tcp --port 27017 --cidr 0.0.0.0/0
```

#### 5. Crear dos instancias EC2
Ahora, crearemos dos instancias EC2 en las subredes públicas para que se encuentren disponibles y puedan ser agregadas al Load Balancer.

Ejecuta el siguiente comando para lanzar la primera instancia en la subnet eu-west-3a:
```bash
aws ec2 run-instances --image-id <ami-id> --count 1 --instance-type t2.micro --key-name MyKeyPair --subnet-id <subnet-id-eu-west-3a> --associate-public-ip-address --security-group-ids <security-group-id> --tag-specifications 'ResourceType=instance,Tags=[{Key=Name,Value=Instancia1}]'
```
Lanza la segunda instancia en la subnet eu-west-3b:
```bash
aws ec2 run-instances --image-id <ami-id> --count 1 --instance-type t2.micro --key-name MyKeyPair --subnet-id <subnet-id-eu-west-3b> --associate-public-ip-address --security-group-ids <security-group-id> --tag-specifications 'ResourceType=instance,Tags=[{Key=Name,Value=Instancia1}]'
```
Reemplaza <ami-id> con el ID de la AMI que desees usar, <subnet-id-eu-west-3a> y <subnet-id-eu-west-3b> con los IDs de las subredes, y <security-group-id> con el ID del grupo de seguridad creado anteriormente.

#### 6. Crear un Target Group
Ahora, crearemos un Target Group para agregar las instancias. Este Target Group manejará el tráfico que será distribuido por el Load Balancer.

```bash
aws elbv2 create-target-group --name MyTargetGroup --protocol HTTPS --port 443 --vpc-id <vpc-id> --target-type instance
```
Esto creará un Target Group llamado MyTargetGroup que escuchará en el puerto 443.

#### 7. Registrar las instancias en el Target Group
Para asociar las instancias EC2 al Target Group creado, primero obtén los IDs de las instancias que lanzaste previamente y regístralas en el Target Group.

```bash
aws elbv2 register-targets --target-group-arn <target-group-arn> --targets Id=<instance-id-1> Id=<instance-id-2>
```
Reemplaza <target-group-arn> con el ARN del Target Group y <instance-id-1> y <instance-id-2> con los IDs de las instancias EC2.

#### 8. Crear un Load Balancer
Crearemos el Load Balancer para distribuir el tráfico entre las instancias. Crea el Load Balancer de la siguiente manera:

```bash
aws elbv2 create-load-balancer --name MyLoadBalancer --subnets <subnet-id-eu-west-3a> <subnet-id-eu-west-3b> --security-groups <security-group-id> --scheme internet-facing --load-balancer-type application --region eu-west-3
```
#### 9. Crear un Listener para el Load Balancer
Finalmente, crea un listener que escuche en el puerto 443 y redirija el tráfico hacia el Target Group:

```bash
aws elbv2 create-listener --load-balancer-arn <load-balancer-arn> --protocol HTTPS --port 443 --default-actions Type=forward,TargetGroupArn=<target-group-arn>
```
Reemplaza <load-balancer-arn> con el ARN del Load Balancer y <target-group-arn> con el ARN del Target Group.

#### 10. Crear un clúster de DocumentDB
En este paso, configuraremos un clúster de Amazon DocumentDB con las siguientes características:

1. 1 instancia en el clúster.
2. TLS desactivado.
3. Asociado a las subredes creadas anteriormente.

##### 1. Crear un grupo de subredes para DocumentDB
Antes de crear el clúster, necesitamos un grupo de subredes que asocie las subnets públicas creadas anteriormente:
```bash
aws docdb create-db-subnet-group \
    --db-subnet-group-name MyDocDBSubnetGroup \
    --db-subnet-group-description "Subnets para DocumentDB" \
    --subnet-ids <subnet-id-eu-west-3a> <subnet-id-eu-west-3b>
```
Reemplaza <subnet-id-eu-west-3a> y <subnet-id-eu-west-3b> con los IDs de las subnets creadas previamente.

##### 2. Crear el clúster DocumentDB
Ahora, crea el clúster:
```bash
aws docdb create-db-cluster \
    --db-cluster-identifier MyDocumentDBCluster \
    --engine docdb \
    --master-username <usernameDB> \
    --master-user-password <passwordDB> \
    --db-subnet-group-name MyDocDBSubnetGroup \
    --vpc-security-group-ids <security-group-id> \
    --port 27017 \
    --enable-tls false
```
Reemplaza <security-group-id> con el ID del Security Group creado anteriormente. El clúster se configurará para escuchar en el puerto 27017.

##### 3. Crear una instancia en el clúster
Finalmente, agrega una instancia al clúster:
```bash
aws docdb create-db-instance \
    --db-instance-identifier MyDocDBInstance \
    --db-cluster-identifier MyDocumentDBCluster \
    --engine docdb \
    --db-instance-class db.t3.medium
```
Esto creará una instancia de clase db.t3.medium en el clúster DocumentDB.

#### 11. Crear un broker de ActiveMQ
En este paso, configuraremos un broker de ActiveMQ utilizando Amazon MQ. Este broker será accesible desde las instancias EC2 y estará asociado al Security Group configurado previamente para permitir el acceso en el puerto `61614`.

##### 1. Crear un broker
Ejecuta el siguiente comando para crear un broker de ActiveMQ:

```bash
aws mq create-broker \
    --broker-name MyActiveMQBroker \
    --engine-type ActiveMQ \
    --engine-version 5.18.4 \
    --deployment-mode SINGLE_INSTANCE \
    --broker-instance-class mq.t3.micro \
    --publicly-accessible true \
    --security-groups <security-group-id> \
    --subnet-ids <subnet-id-eu-west-3a> <subnet-id-eu-west-3b> \
    --authentication-strategy SIMPLE \
    --users '[{"username":"<usernameMQ>","password":"<passwordMQ>"}]'
```
Reemplaza los valores de <security-group-id> con el ID del Security Group que permite el acceso al puerto 61614.
Y los de <subnet-id-eu-west-3a> y <subnet-id-eu-west-3b> con los IDs de las subnets públicas creadas anteriormente.

#### 12. Desplegar la aplicación en las instancias EC2
En este paso, conectaremos a ambas instancias EC2 por SSH, instalaremos Docker y Docker Compose, y configuraremos el despliegue de la aplicación utilizando un archivo `docker-compose.yml`.

##### 1. Conectar a las instancias EC2
Primero, obtén la dirección IP pública de ambas instancias creadas anteriormente. Conéctate a cada una utilizando el comando SSH:

```bash
ssh -i MyKeyPair.pem ec2-user@<public-ip-address>
```
Reemplaza <public-ip-address> con la dirección IP pública de cada instancia y MyKeyPair.pem con tu archivo de clave privada.

##### 2. Instalar Docker
Ejecuta el siguiente comando en ambas instancias para instalar Docker:
```bash
curl -sSL https://get.docker.com | sudo sh
```
##### 3. Descargar y configurar Docker Compose
Descarga el archivo docker-compose_cloud.yml desde el repositorio de GitHub:
```bash
curl -O https://raw.githubusercontent.com/DashShot/TFG_AITOR/refs/heads/main/docker-compose_cloud.yml
```
Edita el archivo descargado para ajustar las variables de entorno necesarias. Utiliza un editor de texto como vim:

```bash
vim docker-compose_cloud.yml
```
Guarda y cierra el archivo.

##### 4. Iniciar el despliegue con Docker Compose
Ejecuta el siguiente comando para iniciar los contenedores:

```bash
docker compose -f docker-compose_cloud.yml up -d
```` 


