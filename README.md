# crypto-recommendation

Quick introduction:
This application was developed using the following main technologies:
- Java
- Spring Boot
- MongoDB

From an architectural standpoint, this app follows the 'Onion Architecture' or 'Clean Architecture' guidelines.
The layers are separated into: 
 - exposition (the endpoints)
 - application (where the services reside)
 - domain
 - infrastructure (data persistence, cron jobs, and csv importer)




Endpoints documentation was realised using OpenAPI 3 and SwaggerUI. You can check it out by accessing: http://localhost:8080/swagger-ui/index.html#/ 

Also, interfaces have been documented with JavaDocs

**!IMPORTANT!** Before you start querying the database, you should trigger the data import from the .csv files located in resources/static/prices.

The import can be triggered by sending a POST request to /crypto-prices/import (check swagger)

Also, the import will be automatically triggered every day at midnight.

If you wish to add/ remove data files used for import, you can do so by adding/ removing the files in the resources/static/prices folder and
you should also update the configuration in **application.yml (cryptorec.file-locations)**

The application needs to connect to a **MongoDB** instance. If ran in the provided **kubernetes environment**, a mongo pod will be included.

If you wish to run the app locally, please install MongoDB or spawn a docker container with it. It's using the default port (27017)

To run the project inside **k8s**, I've used **minikube** to deploy the cluster locally.

*You also need to install kubectl before installing minikube

After minikube installation you should open a command prompt and enter:

**minikube start**

Then, cd to the project's root and enter:

**kubectl apply -f ./kube/** -> this will start the pods

To fetch the port inside the k8s environment you can use **minikube service cryptorec --url**

**Please contact me if I can provide any meaningful information related to starting/ deploying or using this project.**

