## Linux Version

- The Unix operating system was released by Ken Thompson and Dennis Ritchie of AT@T Bell Lab in 1970
- Unix was later rewritten in C, to make it portable
- In 1977 Berkley Software Distribution (BSD) was developed
- In 1985 Intel released the 80386, the first x86 microprocessor
- Commercial Unix was too expensive for private users using Intel 386 PCs
- In 1987, Andrew Tanebaum released MINIX, a Unix like system intended for academic use
    - Source code was available, but modification and redistribution was restricted
    - The 16 bit design of MINIX was not well suited for the 32 bit design of the cheap and popular Intel 386 PCs
- In 1991 Linus Torvalds released a project called 'Freax' which later become Linux
- In 1992 Torvalds released the Linux kernel under GNU license
- Initially Linux only referred to the kernel, not the distribution as a whole (shell, compilers, editors)
- The Debian project started in 1993 to create a distribution of GNU tools (shell, compilers, editors) and was called
GNU/Linux

### Popular Linux Distributions

- Debian - non commercial, amintained by a volunteer developer community
    - Knoppix
    - Linux Mint Debian Edition
    - Ubuntu - maintained by Canonical Ltd
- Fedora - a community distribution sponsored by Red Hat
    - Red Hat Enterprise Linux (RHEL) - Commercially licensed and supported by Red Hat
        - CentOS - same sources as RHEL
        - Oracle Linux - Based on RHEL
        - Amazon Linux - Based on RHEL
- openSUSE - a community edition sponsered by the company SUSE
    - SUSE Linux Enterprise - commercially licensed and supported by SUSE

### Which Linux Distribution to Use

- Mint / Ubuntu highly popular with desktop users
- RHEL dominates with 67% of the enterprise market
- SUSE Enterprise Linux has 20%
- Oracle Linux has 12%

- Fedora derived Linux Distributions have 79% of the enterprise market share (RHEL / Oracle)
- CentOS - effectively is RHEL, with any references to "Red Hat" removed
    - Red Hat enforces licensing via Trademark low of the term "Red Hat"

## Amazon Web Services

- EC2 - Virtual Machines via AMIs (Amazon Machine Images)
- RDS - Relational Database Service - Managed MySQL
- Route 53 - DNS Services

## Docker

```bash
// list all images
docker images -a
docker ps
docker images -q --no-trunc

// delete all images
docker rmi $(docker images -q)
docker rmi -f $(docker images -a -q)
docker rm $(docker ps --all -q)

docker inspect <NAME>

// run hello world
docker run hello-world
docker images -a

// running mongo db
docker pull mongo
docker run --name some-mongo -d mongo:tag
docker run mongo
docker run -d mongo
docker run -p 27017:21017 -d mongo
docker run -p 27017:21017 -d mongo:4.0.5
docker run -p 27017:21017 -v /{some_path}/dockerdata/mongo:/data/db -d mongo
docker image inspect mongo

// see docker logs
docker logs -f facfcd7e8e01

// running rabbitmq
docker run -d --hostname panda-rabbit --name some-rabbit -p 8080:15672 -p 5671:5671 -p 5672:5672 rabbitmq:3-management
docker kill fa6a7240010f

// running mysql
docker run --name panda-mysql -e MYSQL_ALLOW_EMPTY_PASSWORD=yes -v /{some_path}/mysql:/var/lib/mysql -p 3306:3306 -d mysql

```

### Docker Hub

- Public docker registry, like maven centrals

### Docker Images

- An Image defines a Docker Container
    - Similar in concept to a snapshot of a VM
    - Or a class vs an instance of the class
- Images are immutable
    - Once built, the files making up an image do not change
- Images are built in layers
- Each layer is an immutable file, but is a collection of files and directories
- Layers receive an ID, calculated via a SHA 256 hash of the layer contents
    - Thus, if the layer contents change, the SHA 256 hash changes also

- Image Ids are a SHA 256 hash derived from the layer
    - Thus if the layers of the image changes, the SHA 256 hash changes
- The Image ID listed by docker commands ('docker image') is the first 12 characters of the hash

- The hash values of images are referred to by 'tag' names

- The format of the full tag name is: [REGISTRYHOST/][USERNAME/]NAME[:TAG]
- For Registry Host 'registry.hub.docker.com' is inferred
- For `:TAG` - `latest` is default, an inferred
- Full tag example: registry.hub.docker.com/mongo:latest

## Docker

- There are 3 key areas of house keeping:
    - Containers
    - Images
    - Volumes

### Containers cleaning up

- Kill all Running Docker Containers
    - `docker kill $(docker ps -q)`
- Delete all Stopped Docker Containers
    - `docker rm $(docker ps -a -q)`

### Images cleaning up

- Remove a Docker Image
    - `docker rmi <image name>`
- Delete Untagged (dangling) Images
    - `docker rmi $(docker images -q -f dangling=true)`
- Delete All Images
    - `docker rmi $(docker images -q)`

### Volumes cleaning up

- Once a volume is no longer associated with a container, it is considered `dangling`
- Remove all dangling volumes
    - `docker volume rm $(docker volume ls -f dangling=true -q)`
- Does not remove files from host system in shared volumes

### Questions

- Show running containers
    - `docker ps`
- Show all containers? Running and stopped
    - `docker ps -a`
- What s the default tag?
    - `latest` is selected if no other value is specified
- Command to run docker image
    - `docker run <image name>`
- How to see the console output of a docker container?
    - `docker logs <container name>`
- Command to build a docker image?
    - `docker build -t <tag name>`
- Stop a docker container
    - `docker stop <container name>` OR `docker kill <container name>`
- Parameter tells docker to run the container as a background process
    - `-d`, `docker run -d <<image name>`
- List all docker images on your system
    - `docker images`
- Map a host port to a container port
    - `-p <host port>:<container port>`, `docker run -p 8080:8080 <image name>`
- Tail the console output of a running docker container?
    - `docker logs -f <container name>`
- What is like a .java file to a docker image? ie, the source code?
    - The Dockerfile
- Command to remove a stopped docker container?
    - `docker rm <container name>`
- Specify an environment variable for a docker container
    - `docker run -e MY_VAR=my_prop <image name>`
- Remove a docker image from your system
    - `docker rmi <image name>`
- Shell into a running docker container
    - `docker exec -it <container name> bash`
- Share storage on the host system with a docker container
    - `-v <host path>:<container path>`, `docker run -v <my host path>:<the container path> <image name>`

***

## Spring Boot in CentOS

- `docker run -d centos`
- `docker ps`
- `docker run -d centos tail -f /dev/null`
- `docker exec -it lucid_turing(<NAMES>) bash`

```bash
# java -version
# yum install java
```

### Running Spring Boot from docker

**Dockerfile**
```bash
FROM centos

RUN yum install -y java

VOLUME /tmp
ADD /spring-boot-web-0.0.1-SNAPSHOT.jar myapp.jar
RUN sh -c 'touch /myapp.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/myapp.jar"]
```

- At location where dockerfile is

```bash
$ ll
-rw-r--r-- 1 Mati 197121      209 kwi 29  2017 Dockerfile
-rw-r--r-- 1 Mati 197121 37845015 lut 15  2017 spring-boot-web-0.0.1-SNAPSHOT.jar

$ docker build -t spring-boot-docker .

$ docker run -d -p 8080:8080 spring-boot-docker
```











































