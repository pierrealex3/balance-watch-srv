Docker on AWS

ways of deploying docker on aws
- ECS
- Elastic BeansTalk
- AWS Cloud Formation
EC2 VM
Docker Swarm

ECS launch types: Fargate and EC2 (not convinced with Fargate launch type after watching the pluralsight course)

*-*-*-*---**-*-*-*----*---*-*-

!!!
install docker on an EC2 instance, then manage your dockers...



// ssh use the (local) key specified as .pem
ssh -i newcluster.pem ubuntu@34.207.122.35

// download ubuntu:latest from docker repo and then run it within an interactive (-i) tty terminal (-t)
docker run -it ubuntu bash

// runs an ubuntu container in the background, returning a container id
docker run -it --detach=true ubuntu bash

// returns the list of containers running in the host env (EC2 ?)
docker ps

// from ubuntu container; update sw repo indexes
apt update

// docker info
docker info

// docker rename to rename container auto-gen name (stick to 'newname' as i kept this tag in further examples)
docker rename oldcrappycrap newname

// docker inspect does return plenty of data on the container specified
docker inspect newname

// docker network ls
shows NETWORK ID | NAME | DRIVER | SCOPE
- pay atention to bridge interface, which connects containers to host

// creates a network, and connect the previously created container to connect to it (because, by insepcting them you may see they're defined in different subnets by default)
docker network create newnet
docker inspect newnet
docker network connect newnet newname

// simple 'Dockerfile' that can configure an ubuntu docker image on the fly
# Simple Dockerfile
#
FROM ubuntu:latest

RUN apt-get update
RUN apt-get install -y apache	// the y switch is for "yes" on all prompts
RUN echo "welcome" > /var/www/html/index.html/index
EXPOSE 80

// and to build an image from this Dockerfile - assuming Dockerfile is in the current dir
docker build -t "myImg" .

// show docker images - should show the new image "myImg"
docker images

// docker run - order of params is CRITICAL
// -d switch -> run in detached process to avoid getting the stdout/err of the container within the host console
// -p connects host to container ports
// the command after myImg is the command to start on the container once the container is running
docker run -d -p 80:80 myImg /usr/sbin/apache2ctl -D  FOREGROUND

// curl localhost
curl localhost

// docker stores
hub.dockerhub.com

// docker swarm: build containers from a yml file describing the containers you want to launch (images and env vars)
docker swarm init
docker stack deploy -c stack.yml mySvcClusterName

// example docker swarm yml file
version: '3.1'

services:
	wordpress:
		image: wordpress
		ports:
		- 80:80
		environment:
			WORDPRESS_DB_HOST: db
			WORDPRESS_DB_NAME: wpdb
	
	db:
		image: mysql:5.7
		environment:
			MYSQL_DATABASE: wpdb
			MYSQL_USER: wpuser
			
// install amazon ECS cli - follow docs for more options
# define ecs cluster configuration
ecs-cli configure \
--cluster ec2-test-App \
--region us-east-1 \
--default-launch-type EC2 \
--config-name ec2-test-App

--> Saved ECS CLI cluster configuration ec2-test-App

# tell the tool how to pass credentials to ecs
ecs-cli configure profile \	
--access-key <AJHDJSAHDKJHSJ>
--secret-key <KASJKLJLKAJKL>
--profile-name ec2-test-App

--> Saved ECS CLI profile configuration ec2-test-App

# bring the cluster to life - normally include a ref to a keypair for https access
ecs-cli-up \
--capability-iam \
--size 2 \
-- instance-type t2.medium \
--cluster-config ec2-test-App

! note: this also creates the VPC, and then the EC2 instances, security group, etc.

// docker-compose.yml to specify what will be built for ECS
version: '3' // very important because ecs might not support docker > 3
services:
	wordpress:
		image: wordpress
		ports:
			- "80:80"
		links:
		- mysql
	mysql:
		image: mysql:5.7
		environment:
			MYSQL_ROOT_PASSWORD:
		
// ecs-params.yml to provision services
version: 1
task_definition:
	services:
		wordpress:
			cpu_shares: 100
			mem_limit: 524288000
		mysql:
			cpu_shares: 100
			mem_limit: 524288000

// ecs-cli compose
ecs-cli compose \
--project-name ec2-project service up \
--cluster-config ec2-test-App

// ecs-cli ps  - to look at the processes state for the ecs cluster specified
ecs-cli ps --cluster-config ec2-test-App

// SHUT THE CLUSTER DOWN TO AVOID CHARGES $$$$$$$$$$ !!!
ecs-cli compose down --cluster-config ec2-test-App
ecs-cli-down --force --cluster-config ec2-test-App
# and check on the EC2 console to make sure the EC2 VMs are down

// aws cli vs ecs-cli
aws-cli is not meant to execute ecs commands.

// KUBERNETES vs DOCKER-SWARM : Kubernetes are more enterprise-grade, apparently.  Still not convinced because lacking arguments atm.

// eksctl, kubectl and aws-iam-authenticator must be downloaded

// validate signature: howto
$ cat allo
Hey allo!
$ cat allo.sha256
44564546545654
$ openssl sha1 -sha256 allo
SHA256(allo)= 44564546545654

// eksctl create cluster
$ eksctl get cluster
$ eksctl create cluster \
--name wp-cluster \
--version 1.12 \ -> this is about the kubectl version
--nodegroup-name standard-workers \
--node-type t3.medium \ -> this ec2 note type is not free
--nodes 3 \
--nodes-min 1 \
--nodes-max 4 \
--node-ami auto

// to check if kubectl can access our aws account
kubectl get svc
NAME		TYPE		CLUSTER-IP	EXTERNAL-IP	PORT(S)	AGE
Kubernetes	ClusterIP	10.100.0.1	<none>		443/TCP	9m36s

// kubectl get nodes shows the configured nodes of the cluster
kubectl get nodes
NAME							STATUS		ROLES		AGE		VERSION
ip-192-168-41-130.ec2.internal	Ready		<none>		...		v1.12.7
ip-192-168-63-94.ec2.internal	Ready		<none>		...		v1.12.7
ip-192-168-7-152.ec2.internal	Ready		<none>		...		v1.12.7

// get kubernetes deployment descriptor (yaml) from github to start with
ex for the wordpress webapp -> mySql db:
??? are the links between the webapp and mysql db present in those (2) files ???
https://raw.githubusercontent.com/kubernetes/website/master/content/en/examples/application/wordpress/mysql-deployment.yaml
https://raw.githubusercontent.com/kubernetes/website/master/content/en/examples/application/wordpress/wordpress-deployment.yaml

// create the kubernetes and run
kubectl create -f mysql-deployment.yaml
kubectl create -f wordpress-deployment.yaml
kubectl get pvc	// tells us that we have a data volume
kubectl get pods

// give out the EXTERNAL-IP to access the webapp
kubectl get services --all-namespaces -o wide

// kubernetes delete everything for avoid costs
kubectl delete deployment -l app=wordpress
kubectl delete service -l app=wordpress	-> the services
kubectl delete pvc -l app=wordpress		-> the data volumes
eksctl delete cluster --name wp-cluster -> delete the cluster itself

// ECR (elastic container repository) -> docker container repo in AWS
// create repo (login first)
aws ecr get-login --no-include-email --region us-east-1
(copy output in the cli)
aws ecr create-repository --repository-name newrepo
aws ecr describe-repositories

// push a docker image to the docker repo
docker images
... IMAGE ID
...	<my_img_id>

docker tag <my_img_id> <docker newrepo uri obtained from describe-repositories>/newrepo:latest
docker push <docker newrepo uri obtained from describe-repositories>/newrepo:latest

// delete repositories
aws ecr delete-repository --force --repository-name newrepo
aws ecr describe-repositories

// docker_compose.yaml definition files are welcomed unchaged where kubernetes are honored.
	
			
			




