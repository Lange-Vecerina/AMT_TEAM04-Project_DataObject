# AMT_TEAM04-Project_DataObject

**Authors : Yanik Lange, Ivan Vecerina**

## **Introduction**


Hello there ! 

Welcome to the AMT Team 04 Project DataObject repository.
The following repository is part of a bigger multi-repository project that we are working on, consisting of 3 repositories :
* [API](https://github.com/Lange-Vecerina/AMT_TEAM04-Project_API)
* [Simple Storage Microservice](https://github.com/Lange-Vecerina/AMT_TEAM04-Project_DataObject) (you are here)
* [Label Detection Microservice](https://github.com/Lange-Vecerina/AMT_TEAM04-Project_LabelDetector)

## **Repository description**

> This repository is used to store the Simple Storage Microservice of our project.

The Simple Storage Microservice is a microservice that allows us to store and retrieve data from a simple storage service. It is implemented using the Spring Boot framework and currently uses AWS S3 as a storage service.

## **Microservice endpoints**

### **POST {host}/data-object?uri={uri}**

This endpoint allows us to store a data object in the storage service. The uri parameter is used to specify the path of the data object in the storage service. The root object of uri is created if it does not exist.

The body of the request must contain the data of the data object to store. The data can be sent in the body of the request as a byte array or as a URL to a data object.

The response of the request is a 200 OK response if the data object has been successfully stored in the storage service. Otherwise, the response returns an error code and a message explaining the error.

**Request Example**

    POST {host}/data-object?uri=folder1/folder2/data.txt

    {
        url: "https://www.example.com/data.txt"
        content: [byte1, byte2, byte3, ...]
    }

### **PUT {host}/data-object?uri={uri}**

This endpoint allows us to update a data object in the storage service. The uri parameter is used to specify the path of the data object in the storage service. 

The request for this endpoint is the same as the request for the POST endpoint. The difference is that the PUT endpoint will update the data object if it already exists in the storage service. If the data object does not exist, the PUT endpoint will return an error.

### **DELETE {host}/data-object?uri={uri}&recursive={recursive}**

This endpoint allows us to delete a data object in the storage service. The uri parameter is used to specify the path of the data object in the storage service. The recursive parameter is used to specify if the data object is a folder and if it should be deleted recursively. If the recursive parameter is set to true, the folder and all its content will be deleted. If the recursive parameter is set to false, the folder will be deleted only if it is empty.

The response of the request is a 200 OK response if the data object has been successfully deleted from the storage service. Otherwise, the response returns an error code and a message explaining the error.

### **GET {host}/data-object?uri={uri}**

This endpoint allows us to check if a data object exists in the storage service. The uri parameter is used to specify the path of the data object in the storage service.

The response of the request is a 200 OK response if it could check if the data object exists in the storage service. Otherwise, the response returns an error code and a message explaining the error. The response body contains a boolean value that indicates if the data object exists in the storage service.

### **GET {host}/data-object/content?uri={uri}**

This endpoint allows us to retrieve the content of a data object in the storage service. The uri parameter is used to specify the path of the data object in the storage service.

The response of the request is a 200 OK response if the data object exists in the storage service. Otherwise, the response returns an error code and a message explaining the error. The response body contains the content of the data object as a byte array.

### **GET {host}/data-object/link?uri={uri}&ttl={ttl}**

This endpoint allows us to retrieve a link to a data object in the storage service. The uri parameter is used to specify the path of the data object in the storage service. The ttl parameter is used to specify the time to live of the link in seconds.

The response of the request is a 200 OK response if the data object exists in the storage service. Otherwise, the response returns an error code and a message explaining the error. The response body contains the link to the data object.

## **Dependencies**

*The following prerequisite are necessary to run the project on your machine :*

* Java 11 (or higher)
* Maven 
* AWS credentials
* Docker

### **Why Maven ?**

> Maven enables us to manage the project's build, reporting and documentation from a central piece of information. Maven is a build automation tool used primarily for Java projects. Maven addresses two aspects of building software: first, it describes how software is built, and second, it describes its dependencies. Maven is used to build and manage projects based on the Project Object Model (POM). 

### **Why Spring Boot ?**

> Spring Boot is an open source Java-based framework used to create a Micro Service. It is developed by Pivotal Team and is used to build stand-alone and production ready spring applications. It is used to create a microservice that can be deployed on the cloud platform. It is easy to create a RESTful web service using Spring Boot.

### **Why Docker ?**

> Docker enables us to package an application with all of its dependencies into a standardized unit for software development that includes everything it needs to run: code, runtime, system tools, system libraries and settings. Making our application portable and easy to run on any machine.

### **Why AWS S3 ?**

> Amazon Simple Storage Service (Amazon S3) is an object storage service that offers industry-leading scalability, data availability, security, and performance. This means customers of all sizes and industries can use it to store and protect any amount of data for a range of use cases, such as websites, mobile applications, backup and restore, archive, enterprise applications, IoT devices, and big data analytics.

### **Install Dependencies**

- To install Java 11 on your machine you can follow the following tutorial : https://www.oracle.com/java/technologies/javase-jdk11-downloads.html
- To install Maven on your machine you can follow the following tutorial : https://maven.apache.org/install.html
- To install Docker on your machine you can follow the following tutorial : https://docs.docker.com/get-docker/

## **How to Contribute**

>TODO all below

### **Check the project's backlog**

See the project's backlog on the following link : [Backlog](https://github.com/orgs/Lange-Vecerina/projects/2)

This will allow you to see the current state of the project and the tasks that are currently being worked on. This will also allow you to see the tasks that are available to be worked on.

### **Fork/Clone the project**


### **Adapt personal settings**

To run the application with your own AWS credentials you need to add the following credentials as environnement path of 
your machine :

Variable : **AWS_ACCESS_KEY_ID** Value : **<your_aws_access_key>**

Variable : **AWS_SECRET_ACCESS_KEY** Value : **<your_aws_secret_key>**

Example how to set environment variable on windows :
https://docs.oracle.com/en/database/oracle/machine-learning/oml4r/1.5.1/oread/creating-and-modifying-environment-variables-on-windows.html#GUID-DD6F9982-60D5-48F6-8270-A27EC53807D0

> TODO ajouter les commandes pour récupérer les dépendances

> TODO ajouter les commandes pour compiler, ainsi que quelque explications sur comment et pourquoi vous utilisez Maven 

> TODO ajouter la commande sur comment lancer un ou tout les tests 


## **Run the Test Scenarios on your machine**

Now that you have everything set up, you can run the test scenarios on your machine.


### **Run on your machine TODO**

Download the latest **.jar** release on the **Release** section in this github repository.

Open a *cmd/terminal* and go the directory where you saved your downloaded **.jar**.

To run the program enter the following command : 

```java -jar lab3_rekognition-v1-shaded.jar "<your_path_of_the_image_you_want_to_label>"```

Example of a **fruits.jpg** image in ```D:/image/``` folder :

```java -jar lab3_rekognition-v1-shaded.jar "D:/image/fruits.jpg"```

This will generate 2 temporary links to the bucket :

* The first links to the bucket where the image you asked to be labeled by Amazon Rekognition is.
* The second links to the result of the labeling (in the bucket too).

