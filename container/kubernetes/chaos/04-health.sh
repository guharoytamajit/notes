# Source: https://gist.github.com/6be19a176b5cbe0261c81aefc86d516b

######################
# Creating A Cluster #
######################

# Docker Desktop: https://gist.github.com/f753c0093a0893a1459da663949df618 (docker.sh)
# Minikube: https://gist.github.com/ddc923c137cd48e18a04d98b5913f64b (minikube.sh)
# GKE: https://gist.github.com/2351032b5031ba3420d2fb9a1c2abd7e (gke.sh)
# EKS: https://gist.github.com/be32717b225891b69da2605a3123bb33 (eks.sh)
# AKS: https://gist.github.com/c7c9a8603c560eaf88d28db16b14768c (aks.sh)

#############################
# Deploying The Application #
#############################

cd go-demo-8

git pull

kubectl create namespace go-demo-8

cat k8s/terminate-pods/app/*

kubectl --namespace go-demo-8 \
    apply --filename k8s/terminate-pods/app

kubectl --namespace go-demo-8 \
    rollout status deployment go-demo-8

##############################
# Validating The Application #
##############################

kubectl --namespace go-demo-8 \
    get ingress

kubectl apply \
    --filename https://raw.githubusercontent.com/kubernetes/ingress-nginx/nginx-0.27.0/deploy/static/mandatory.yaml

# If Minikube
minikube addons enable ingress

# If Docker Desktop, GKE, or AKS
kubectl apply \
    --filename https://raw.githubusercontent.com/kubernetes/ingress-nginx/nginx-0.27.0/deploy/static/provider/cloud-generic.yaml

# If EKS
kubectl apply \
    --filename https://raw.githubusercontent.com/kubernetes/ingress-nginx/nginx-0.27.0/deploy/static/provider/aws/service-l4.yaml

# If EKS
kubectl apply \
    --filename https://raw.githubusercontent.com/kubernetes/ingress-nginx/nginx-0.27.0/deploy/static/provider/aws/patch-configmap-l4.yaml

# If Minikube
export INGRESS_HOST=$(minikube ip)

# If Docker Desktop or EKS
export INGRESS_HOST=$(kubectl \
    --namespace ingress-nginx \
    get service ingress-nginx \
    --output jsonpath="{.status.loadBalancer.ingress[0].hostname}")

# If GKE or AKS
export INGRESS_HOST=$(kubectl \
    --namespace ingress-nginx \
    get service ingress-nginx \
    --output jsonpath="{.status.loadBalancer.ingress[0].ip}")

echo $INGRESS_HOST

# Repeat the `export` command if the output is empty

cat k8s/health/ingress.yaml

kubectl --namespace go-demo-8 \
    apply --filename k8s/health/ingress.yaml

curl -H "Host: go-demo-8.acme.com" \
    "http://$INGRESS_HOST"

#################################
# Validating Application Health #
#################################
In last section our SSH was defuned for a single pod, instead we can define a SSH which says all pods of any deployment   must me healthy

cat chaos/health.yaml

version: 1.0.0
title: What happens if we terminate an instance of the application?
description: If an instance of the application is terminated, a new instance should be created
tags:
- k8s
- pod
- deployment
steady-state-hypothesis:
  title: The app is healthy
  probes:
  - name: all-apps-are-healthy
    type: probe
    tolerance: true
    provider:
      type: python
      func: all_microservices_healthy                  #all pods must be healthy
      module: chaosk8s.probes
      arguments:
        ns: go-demo-8
method:
- type: action
  name: terminate-app-pod
  provider:
    type: python
    module: chaosk8s.pod.actions
    func: terminate_pods
    arguments:
      label_selector: app=go-demo-8
      rand: true
      ns: go-demo-8

chaos run chaos/health.yaml

In this run  SSH will fail because POST SSH gets executes before the deployment can re-create the terminated pod(it takes few seconds before deploy identifies missing pod and re-create the same).

kubectl --namespace go-demo-8 \
    get pods

cat chaos/health-pause.yaml

diff chaos/health.yaml \
    chaos/health-pause.yaml

chaos run chaos/health-pause.yaml

Now SSH should pass

kubectl --namespace go-demo-8 \
    get pods

#######################################
# Validating Application Availability #
#######################################

Instead of just checking if our pod is ready it would be create if we can validate the pod by making  some http request and see if application is running.

cat chaos/health-http.yaml


version: 1.0.0
title: What happens if we terminate an instance of the application?
description: If an instance of the application is terminated, the applications as a whole should still be operational.
tags:
- k8s
- pod
- http
configuration:
  ingress_host:                                      #here we are defining a variable "ingress_host" its value is valuue of env variable with key "INGRESS_HOST"
      type: env
      key: INGRESS_HOST
steady-state-hypothesis:
  title: The app is healthy
  probes:
  - name: app-responds-to-requests
    type: probe
    tolerance: 200                                  #we are validating if our application returns http 200(OK) status
    provider:
      type: http                                       #SSH is making http request to verify
      timeout: 3                                        #3 second timeout
      verify_tls: false
      url: http://${ingress_host}/demo/person   #now we can use the variable "ingress_host" as shown here,this way we can make our yaml dynamic
      headers:
        Host: go-demo-8.acme.com                    #defining http header for our request
method:
- type: action
  name: terminate-app-pod
  provider:
    type: python
    module: chaosk8s.pod.actions
    func: terminate_pods
    arguments:
      label_selector: app=go-demo-8
      rand: true
      ns: go-demo-8
  pauses: 
    after: 2                                                      #wait 2 seconds after destroying pod, before executing POST SSH

diff chaos/health-pause.yaml \
    chaos/health-http.yaml

chaos run chaos/health-http.yaml

It fails, because although our application is fault tolerant(after using deployment instead of pod), it is still not HA(as deployment has only one replica)
Lets make it HA by defining a HPA with min replica =2

cat k8s/health/hpa.yaml

apiVersion: autoscaling/v2beta1
kind: HorizontalPodAutoscaler
metadata:
  name: go-demo-8
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: go-demo-8
  minReplicas: 2
  maxReplicas: 6
  metrics:
  - type: Resource
    resource:
      name: cpu
      targetAverageUtilization: 80
  - type: Resource
    resource:
      name: memory
      targetAverageUtilization: 80

kubectl apply --namespace go-demo-8 \
    --filename k8s/health/hpa.yaml

kubectl --namespace go-demo-8 \
    get hpa

# Repeat if the number of replicas is not `2`

chaos run chaos/health-http.yaml

########################################
# Terminating Application Dependencies #
########################################

cat chaos/health-db.yaml

version: 1.0.0
title: What happens if we terminate an instance of the DB?
description: If an instance of the DB is terminated, dependant applications should still be operational.
tags:
- k8s
- pod
- http
configuration:
  ingress_host:
      type: env
      key: INGRESS_HOST
steady-state-hypothesis:
  title: The app is healthy
  probes:
  - name: app-responds-to-requests
    type: probe
    tolerance: 200
    provider:
      type: http
      timeout: 3
      verify_tls: false
      url: http://${ingress_host}/demo/person
      headers:
        Host: go-demo-8.acme.com
method:
- type: action
  name: terminate-db-pod
  provider:
    type: python
    module: chaosk8s.pod.actions
    func: terminate_pods
    arguments:
      label_selector: app=go-demo-8-db                                            #now we are deleting mongodb instance instead of application pod
      rand: true
      ns: go-demo-8
  pauses: 
    after: 2

diff chaos/health-http.yaml \
    chaos/health-db.yaml

chaos run chaos/health-db.yaml

It will fail as mongoDb is not configured to be HA,
So out application will never be HA unless database is also HA

##############################
# Destroying What We Created #
##############################

cd ..

kubectl delete namespace go-demo-8
