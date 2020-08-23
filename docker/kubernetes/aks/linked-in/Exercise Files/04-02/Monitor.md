You can create a new default workspace by enabling the add-on for our cluster:

az aks enable-addons -a monitoring -n myAKSCluster -g myResourceGroup

Once installed, we should be able to see that the monitoring agent has been installed in the kube-system namespace:
kubectl get ds omsagent --namespace=kube-system

To view output, we need to use the Azure web portal:

In the resource pane at the far left (it may be collapsed, in which case expand it), select the "All Services" panel, and search for Kubernetes.

Select the Kubernetes services, and then select your test cluster (myAKSCluster if you used the same name in the course).

Then select Monitoring, and we can sort through log and monitoring data from nodes to individual contaiers. Note it may take up to 15 minutes for data collection to be displayed as the services may need to synchronize first.
