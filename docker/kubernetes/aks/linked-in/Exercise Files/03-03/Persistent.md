A PersistentVolumeClaim requests either Disk or File storage of a particular StorageClass, access mode, and size. The Kubernetes API server can dynamically provision the underlying storage resource in Azure if there is no existing resource to fulfill the claim based on the defined StorageClass. The pod definition includes the volume mount once the volume has been connected to the pod.

The hostname_volume.yml file is made up of three components, a deployment of our simple hostname application, a service with LoadBalancer type so that we can reach the application from the public network, and a PVC with our retained policy volume. 

We can launch our resource with:

kubectl apply -f hostname_volume.yml
