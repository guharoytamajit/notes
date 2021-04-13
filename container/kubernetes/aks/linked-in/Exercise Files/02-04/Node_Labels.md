A last method for manipulating the node/POD relationship includes taints and tolerations, which allows us to manually define specific nodes to allow or disallow POD deployments.  There is also a less draconian approach using selectors and affinity.  We will enable an affinity approach based on node labels.

First we need to manually scale our cluster to have two resources:
az aks update --disable-cluster-autoscaler --resource-group myResourceGroup --name myAKSCluster
az aks scale --resource-group myResourceGroup --name myAKSCluster --node-count 2

kubectl get nodes 

kubectl label node <new_node_name> anykey=anyvalue

We can then edit a container specification in a POD or Deployment to include a nodeSelector that matches the node label, forcing that POD to be deployed only to that node (assuming there is capacity).

spec:
  nodeSelector:
    anykey: anyvalue

And this will force the scheduling of this pod to our second node and will deploy 5 replicas (which should only be applied to the second node)

kubectl apply -f hostname-anykey.yml

We can also deploy and scale our original hostname app that doesn't have the same restrictions, of which some PODs should be scheduled to our original node:

kubectl apply -f hostname.yml
kubectl scale deploy hostname-v1 --replicas=5

Get pods with node assignments

kubectl get pods -o wide | awk '{print $1 " " $7}'
or (if you don't have awk in your CLI)
kubectl get pods -o wide

