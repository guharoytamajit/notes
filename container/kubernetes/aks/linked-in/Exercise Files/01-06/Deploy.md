We will be using a preview command to create an autoscaling capable cluster, so we first need to add the preview commands to our az CLI:

az extension add --name aks-preview

Until the preview commands become standard, if you need a stable cluster, do not enable the preview parameter (or the vmss and autoscaler parameters below).

Finally we can create an AKS cluster (you will need your <appId> and <password> from the previous section):

cat ../sp.txt

az aks create \
    --resource-group myResourceGroup \
    --name myAKSCluster \
    --node-count 1 \
    --max-pods 20 \
    --kubernetes-version 1.12.4 \
    --generate-ssh-keys \
    --enable-vmss \
    --enable-cluster-autoscaler \
    --min-count 1 \
    --max-count 3 \
    --service-principal <appId> --client-secret <password> 

This will create a cluster (which may take 5-10 minutes). Once done, we can connect to the kubernetes environment via the Kubernetes CLI. If you are using the Azure Cloud Shell, the kubernetes client (kubectl) is already installed. You can also install locally if you haven't previously installed a version of kubectl:
az aks install-cli

az aks get-credentials --resource-group myResourceGroup --name myAKSCluster --admin

The resource-group and name were set during the creation process, and you should use those if different from what we're using here.

Check your connection and that the kubernetes cli is working with:
kubectl get nodes

If you have issues with connecting, one thing you can check is the version of your kubernetes client:
kubectl version

This will tell you both the local client, and the configured kubernetes service version, make sure the client is at least the same if not newer than the server.
