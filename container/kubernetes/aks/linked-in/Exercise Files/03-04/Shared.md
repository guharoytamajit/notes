#Shared volumes must be of the Azure File type. Azure files only work with Standard storage. They will fail if you try using Premium.

In most cases persistent _disk_ storage is only available in a one-to-one mapping connecting a disk to a pod directly. In AKS, it is also possible to map _file_ style resources, which use the SMB storage model, and allow multiple connections and multiple read and write resources.  In order to use the file storage resources, we first need to create a storage account if we don't have one already, and it needs to be associated with our AKS enviornment.  Find our node resource class name:

$ az aks show --resource-group myResourceGroup --name myAKSCluster --query nodeResourceGroup -o tsv

Then we can create a storage account with that resource group (note that the group should start with MC_):
az storage account create -n myaksstorageaccount -g MC_myResourceGroup_myAKSCluster_eastus -l eastus --sku Standard_LRS

Now we can create the storage class, and a role and role binding which are needed to allow the storage controller to create a secret for the PVC to use for accessing the file share resource.

Create the storage class:
kubectl apply -f file_sc.yml

And the Cluster Role and Role Binding (RBAC parameters):
kubectl apply -f files_pvc_roles.yml

Finally, we can create a shared PVC:
kubectl apply -f file_pvc.yml

Lastly, we can see that the file was created:
kubectl get pvc azurefile

And if we attach the PVC to a pod as we did previously, we can attach more than one POD to the same file resource:

kubectl apply -f hostname_files.yml

And we can now write into, and read from the two pods to see that the both mount the same filesystem:

kubectl exec -it $(kubectl get pod -l app=hostname-file -o jsonpath='{.items[0].metadata.name}') -- sh -c 'hostname > /www/hostname; cat /www/hostname'
kubectl exec -it $(kubectl get pod -l app=hostname-file -o jsonpath='{.items[1].metadata.name}') -- sh -c 'cat /www/hostname'
