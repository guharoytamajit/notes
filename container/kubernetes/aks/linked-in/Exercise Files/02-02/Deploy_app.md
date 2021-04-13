To deploy your app, you must update the image name in the Kubernetes manifest file to include the ACR login server name:
az acr list --resource-group myResourceGroup --query "[].{acrLoginServer:loginServer}" --output tsv

Open the file for editing:
vi hostname.yml

replace microsoft with your ACR sever login name:

containers:
- name: hostname
  image: hostname:v1

With

containers:
- name: hostname
  image: akscourse.azurecr.io/hostname:v1

We can then deploy the app with:
kubectl apply -f hostname.yml

You can see the progress of this process with:
kubectl get svc hostname -w

When the public IP changes from <pending> to a value, navigate to the IP with your web browser to verify the app is working.

curl http://<ip_from_service_external>/

