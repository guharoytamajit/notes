# Source: https://gist.github.com/bc334351b8f5659e903de2a6eb9e3079

######################
# Creating A Cluster #
######################

# GKE with Istio: https://gist.github.com/924f817d340d4cc52d1c4dd0b300fd20 (gke-istio.sh)
# EKS with Istio: https://gist.github.com/3989a9707f80c2faa445d3953f18a8ca (eks-istio.sh)
# AKS with Istio: https://gist.github.com/c512488f6a30ca4783ce3e462d574a5f (aks-istio.sh)

#############################
# Deploying The Application #
#############################

cd go-demo-8

git pull

kubectl create namespace go-demo-8

kubectl label namespace go-demo-8 \
    istio-injection=enabled

kubectl --namespace go-demo-8 \
    apply --filename k8s/app-db

kubectl --namespace go-demo-8 \
    rollout status deployment go-demo-8

curl -H "Host: go-demo-8.acme.com" \
    "http://$INGRESS_HOST"

###########################
# Drainining Worker Nodes #
###########################
    
cat chaos/node-drain.yaml

version: 1.0.0
title: What happens if we drain a node
description: All the instances are distributed among healthy nodes and the applications are healthy
tags:
- k8s
- deployment
- node
configuration:
  node_label:
      type: env
      key: NODE_LABEL
steady-state-hypothesis:
  title: Nodes are indestructible
  probes:
  - name: all-apps-are-healthy
    type: probe
    tolerance: true
    provider:
      type: python
      func: all_microservices_healthy
      module: chaosk8s.probes
      arguments:
        ns: go-demo-8
method:
- type: action
  name: drain-node
  provider:
    type: python
    func: drain_nodes                                        #drain node
    module: chaosk8s.node.actions
    arguments:
      label_selector: ${node_label}                        #only drain node which has this label
      count: 1                                                      #drain one random node which is running any of the pod belong to go-demo-8 namespace
      pod_namespace: go-demo-8
      delete_pods_with_local_storage: true
  pauses: 
    after: 1

kubectl describe nodes

export NODE_LABEL="beta.kubernetes.io/os=linux"

chaos run chaos/node-drain.yaml

Node draining is a two step process 1)condon 2)actual pod drain

We have to take care of the following:
1)Make sure nginx controller/istio gateway controller has more than one pod otherwise we will have downtime.
2)Make sure node drain does not conflict with pod disruption budget, other wise it will fail
3)make sure we add a rollback section so that after POST SSH we can make the drained node schedulable again(uncordon: see next example)


############################
# Uncordoning Worker Nodes #
############################

kubectl get nodes

cat chaos/node-uncordon.yaml


version: 1.0.0
title: What happens if we drain a node
description: All the instances are distributed among healthy nodes and the applications are healthy
tags:
- k8s
- deployment
- node
configuration:
  node_label:
      type: env
      key: NODE_LABEL
steady-state-hypothesis:
  title: Nodes are indestructible
  probes:
  - name: all-apps-are-healthy
    type: probe
    tolerance: true
    provider:
      type: python
      func: all_microservices_healthy
      module: chaosk8s.probes
      arguments:
        ns: go-demo-8
method:
- type: action
  name: drain-node
  provider:
    type: python
    func: drain_nodes
    module: chaosk8s.node.actions
    arguments:
      label_selector: ${node_label}
      count: 1
      pod_namespace: go-demo-8
      delete_pods_with_local_storage: true
  pauses: 
    after: 1
rollbacks:                                                           #new rollback section
- type: action
  name: uncordon-node
  provider:
    type: python
    func: uncordon_node                                     #uncordon all nodes with matching label
    module: chaosk8s.node.actions
    arguments:
      label_selector: ${node_label}


diff chaos/node-drain.yaml \
    chaos/node-uncordon.yaml

chaos run chaos/node-uncordon.yaml

kubectl get nodes

##########################
# Making Nodes Drainable #
##########################

To make node drainable we have to make sure:
1)Pod Disruption Budget is not violated(istio has PDB that each deployment in istio-system must have atleast one pod running, so to support node draining we have to make sure min replica of 2 for each deployment)
2)we have more than one nodes

kubectl --namespace istio-system \
    get deployment

export CLUSTER_NAME=[...] # Replace `[...]` with the name of the cluster (e.g., `chaos`)

# NOTE: Might need to increase quotas

# If GKE
gcloud container clusters \
    resize $CLUSTER_NAME \
    --zone us-east1-b \
    --num-nodes=3

# If EKS
eksctl get nodegroup \
    --cluster $CLUSTER_NAME

# If EKS
export NODE_GROUP=[...] # Replace `[...]` with the node group

# If EKS
eksctl scale nodegroup \
    --cluster=$CLUSTER_NAME \
    --nodes 3 \
    $NODE_GROUP

# If AKS
az aks show \
    --resource-group chaos \
    --name chaos \
    --query agentPoolProfiles

# If EKS
export NODE_GROUP=[...] # Replace `[...]` with the `name` (e.g., `nodepool1`)

# If AKS
az aks scale \
    --resource-group chaos \
    --name chaos \
    --node-count 3 \
    --nodepool-name $NODE_GROUP

kubectl get nodes

# Repeat the previous command if there are no three `Ready` nodes

kubectl --namespace istio-system \
    get hpa

kubectl --namespace istio-system \
    patch hpa istio-ingressgateway \
    --patch '{"spec": {"minReplicas": 2}}'

kubectl --namespace istio-system \
    get hpa

kubectl --namespace istio-system \
    get pods \
    --output wide

chaos run chaos/node-uncordon.yaml

kubectl get nodes

#########################
# Deleting Worker Nodes #
#########################

cat chaos/node-delete.yaml

diff chaos/node-uncordon.yaml \
    chaos/node-delete.yaml

chaos run chaos/node-delete.yaml

kubectl get nodes

# NOTE: You might need to terminate the node that was removed from Kubernetes

kubectl --namespace go-demo-8 \
    get pods

############################
# Destroying Cluster Zones #
############################

# Regional and scalable GKE: https://gist.github.com/88e810413e2519932b61d81217072daf
# Regional and scalable EKS: https://gist.github.com/d73fb6f4ff490f7e56963ca543481c09
# Regional and scalable AKS: https://gist.github.com/b068c3eadbc4140aed14b49141790940

##############################
# Destroying What We Created #
##############################

cd ..

kubectl delete namespace go-demo-8
