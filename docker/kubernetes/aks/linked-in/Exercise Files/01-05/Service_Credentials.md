To allow an AKS cluster to interact with other Azure resources such as the Azure Container Registry we created in a previous chapter, an Azure Active Directory (ad) service principal is used. To create the service principal:
az ad sp create-for-rbac --skip-assignment

Make a note of the appId and password, you will need these. Better yet, save this credential somewhere secure.

Get the ACR resource ID:
az acr show --resource-group myResourceGroup --name akscourse --query "id" --output tsv

Create a role assignment:
az role assignment create --assignee <appId> --scope <acrId> --role Reader

It is also possible to integrate AKS with Azure Active Directory in a much deeper fashion, which may be appropriate in an enterprise environment. Further instructions can be found here:

https://docs.microsoft.com/en-us/azure/aks/aad-integration
