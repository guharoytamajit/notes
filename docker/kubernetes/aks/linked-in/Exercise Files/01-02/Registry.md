Azure Container Registry (ACR) is an Azure-based private registry for Docker container images. Lets create an ACR instance to store our application in, and upload our container app to the newly created registry.

First you'll need to create a resource group, which we will re-use for the rest of the course:

az group create --name myResourceGroup --location eastus

Now create we can create an ACR instance:

az acr create --resource-group myResourceGroup --name akscourse --sku Basic

Login to your ACR instance:
az acr login --name akscourse


Verify that you have a local copy of your application image:
docker images

If you don't have an application, you can quickly build your own mini-application image (see the app_example/README.md file):
docker build app_example/ -t hostname:v1

We need to tag the image with the registry login server address which we can get with:

az acr list --resource-group myResourceGroup --query "[].{acrLoginServer:loginServer}" --output tsv

export aLS=<acrLoginServer>
Tag your nginx image with the server address and verion (v1 in this case):
docker tag hostname:v1 ${aLS}/hostname:v1

Verify that your tags have been applied:
docker images

Push the image to the Registry:
docker push ${aLS}/hostname:v1

Verify that the image has been pushed correctly:
az acr repository list --name akscourse --output tsv
repository=<repository_output>

You can verify that the image is appropriately tagged with (repository is the output of the previous step):
az acr repository show-tags --name akscourse --repository ${repository} --output tsv
