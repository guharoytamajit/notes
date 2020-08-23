Applications can be scaled in multiple ways, from manual to automatic at the POD level:

You can manually define the number of pods with:
kubectl scale --replicas=5 deployment/hostname-v1
kubectl get pods

Update the hostname deployment CPU requests and limits to the following:

kubectl apply -f hostname.yml

Now scale your app with the following:
kubectl autoscale deployment hostname-v1 --cpu-percent=50 --min=3 --max=10

You can see the status of your pods with:
kubectl get hpa
kubectl get pods

The manually set number of replicas (5) should reduce to 3 given there is minimal load on the app.

It is also possible to change the actual k8s cluster size. During cluster creation, you can set the cluster size with the flag:
--node-count

If we didn't enable cluster-autoscale, we could manually change the pool size after creation you can change the node pool size using:
az aks scale --resource-group myResourceGroup --name myAKSCluster --node-count 3

The auto-scaling needs to be done at cluster create time, as it is not possible to enable autoscaling at the moment, or to change the min and max node counts on the fly (though we can manually change the node count in our cluster).

In order to trigger an autoscale, we can first remove the POD autoscaling hpa service:

kubectl delete hpa hostname-v1

Then we can scale our PODs (we set a max of 20 per node) to 25:

kubectl delete hpa hostname-v1
kubectl scale --replicas=25 deployment/hostname-v1

After a few minutes, we should see 25 pods running across at least two if not all three nodes in our autoscale group
 
kubectl get pods -o wide -w


