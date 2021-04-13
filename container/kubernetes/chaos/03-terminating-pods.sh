# Source: https://gist.github.com/419032bc714cc31cd2f72d45ebef07c7

######################
# Creating A Cluster #
######################

# Docker Desktop: https://gist.github.com/f753c0093a0893a1459da663949df618 (docker.sh)
# Minikube: https://gist.github.com/ddc923c137cd48e18a04d98b5913f64b (minikube.sh)
# GKE: https://gist.github.com/2351032b5031ba3420d2fb9a1c2abd7e (gke.sh)
# EKS: https://gist.github.com/be32717b225891b69da2605a3123bb33 (eks.sh)
# AKS: https://gist.github.com/c7c9a8603c560eaf88d28db16b14768c (aks.sh)


####################################################
# Create a conda environment for chaos engineering#
###################################################

>conda create --name chaos python=3.8                  #create an environment with name chaos and  python 3.8 installed
>conda activate chaos                                            #use chaos environment
>pip install -U chaostoolkit-kubernetes
>chaos --help

>conda env list #list of environments
>source activate chaos
>source deactivate
>conda env remove --name chaos    #delete environment



#############################
# Deploying The Application #
#############################

git clone \
    https://github.com/vfarcic/go-demo-8.git

cd go-demo-8

git pull

kubectl create namespace go-demo-8

cat k8s/terminate-pods/pod.yaml


create a pod:
kubectl --namespace go-demo-8 \
    apply --filename k8s/terminate-pods/pod.yaml

#################################
# Discovering Kubernetes Plugin #
#################################

>chaos toolkit comes with discover command which enables us to find features offered by chaostoolkit.

>chaos discover chaostoolkit-kubernetes                                            #result is stored in a file named discovery.json

>cat discovery.json                                                                            #this file is like full documentation of chaostoolkit    



#####################################
# Terminating Application Instances #
#####################################

>cat chaos/terminate-pod.yaml


version: 1.0.0
title: What happens if we terminate a Pod?
description: If a Pod is terminated, a new one should be created in its places.
tags:
- k8s
- pod
method:                                                                          #method can be action or probe
- type: action
  name: terminate-pod
  provider:
    type: python
    module: chaosk8s.pod.actions
    func: terminate_pods                                                     #this function can terminate pods ,for more details see  discovery.json 
    arguments:
      label_selector: app=go-demo-8                                     #pod with this matching label will be deleted
      rand: true                                                                     #if multiple pods found with same label ,delete a random pod
      ns: go-demo-8                                                              #namespace

>chaos run chaos/terminate-pod.yaml

> echo $?                                                                   #     0=>success   ;        any other number =>failure

kubectl --namespace go-demo-8 \
    get pods

###########################
# Steady State Hypothesis #
###########################

> cat chaos/terminate-pod-ssh.yaml

version: 1.0.0
title: What happens if we terminate a Pod?
description: If a Pod is terminated, a new one should be created in its places.
tags:
- k8s
- pod
steady-state-hypothesis:                               #check the state before and after  destroying stuffs,if state is unchanged before and after action we conclude our cluster is fault tolerant/relilient
  title: Pod exists
  probes:                                                      #probe validates a system
  - name: pod-exists
    type: probe
    tolerance: 1                                             #steadystate hypothesis will pass only  if No. of pods ==1 (not less not more) both before and after action executes
    provider:
      type: python
      func: count_pods                                 #check number of pods with matching criteria(label and namespace)
      module: chaosk8s.pod.probes
      arguments:
        label_selector: app=go-demo-8
        ns: go-demo-8
method:
- type: action
  name: terminate-pod
  provider:
    type: python
    module: chaosk8s.pod.actions
    func: terminate_pods
    arguments:
      label_selector: app=go-demo-8
      rand: true
      ns: go-demo-8

diff chaos/terminate-pod.yaml \
    chaos/terminate-pod-ssh.yaml

chaos run chaos/terminate-pod-ssh.yaml

echo $?                      

kubectl --namespace go-demo-8 \
    apply --filename k8s/terminate-pods/pod.yaml

chaos run chaos/terminate-pod-ssh.yaml

echo $?

kubectl --namespace go-demo-8 \
    apply --filename k8s/terminate-pods/pod.yaml



summary of chaos/terminate-pod-ssh.yaml:
1)check if number of pod ==1 with selector  app=go-demo-8 in namespace go-demo-8
2)delete the pod with selector  app=go-demo-8 in namespace go-demo-8
3)check if number of pod ==1 with selector  app=go-demo-8 in namespace go-demo-8

SSH=>steady-state-hypothesis(validate the state )

Our experiment should have failed as pod is not re-created by default (if terminated),but SSH after action still passes because it executes immediately after action before the termination of the pod operation is  completed.

 
#########################
# Pausing After Actions #
#########################

cat chaos/terminate-pod-pause.yaml


version: 1.0.0
title: What happens if we terminate a Pod?
description: If a Pod is terminated, a new one should be created in its places.
tags:
- k8s
- pod
steady-state-hypothesis:
  title: Pod exists
  probes:
  - name: pod-exists
    type: probe
    tolerance: 1
    provider:
      type: python
      func: count_pods
      module: chaosk8s.pod.probes
      arguments:
        label_selector: app=go-demo-8
        ns: go-demo-8
method:
- type: action
  name: terminate-pod
  provider:
    type: python
    module: chaosk8s.pod.actions
    func: terminate_pods
    arguments:
      label_selector: app=go-demo-8
      rand: true
      ns: go-demo-8
  pauses: 
    after: 10                                    #there will be a pause of 10 seconds after action is executed ie. before POST SSH execution.

diff chaos/terminate-pod-ssh.yaml \
    chaos/terminate-pod-pause.yaml

chaos run chaos/terminate-pod-pause.yaml

echo $?

kubectl --namespace go-demo-8 \
    apply --filename k8s/terminate-pods/pod.yaml

#########################
# Phases And Conditions #
#########################
So far we were only checking if pod exists but in real world we should actually check if the pod is in ready state(as pod with exists but  not ready is of no use)


kubectl --namespace go-demo-8 \
    describe pod go-demo-8

cat chaos/terminate-pod-phase.yaml

version: 1.0.0
title: What happens if we terminate a Pod?
description: If a Pod is terminated, a new one should be created in its places.
tags:
- k8s
- pod
steady-state-hypothesis:
  title: Pod exists
  probes:
  - name: pod-exists
    type: probe
    tolerance: 1
    provider:
      type: python
      func: count_pods
      module: chaosk8s.pod.probes
      arguments:
        label_selector: app=go-demo-8
        ns: go-demo-8
  - name: pod-in-phase                                           #new probe                            
    type: probe
    tolerance: true                                                  #if tolerance was false the condition check operaton would have become !=(instead of ==)
    provider:
      type: python
      func: pods_in_phase                                       # it checks if pod is in the desired phase
      module: chaosk8s.pod.probes
      arguments:
        label_selector: app=go-demo-8
        ns: go-demo-8
        phase: Running                                               #the pod should be in running phase
  - name: pod-in-conditions                                       #new probe
    type: probe
    tolerance: true                                                      #if tolerance was false the condition check operaton would have become !=(instead of ==)
    provider:
      type: python
      func: pods_in_conditions                                    # it checks if pod is in the desired condition
      module: chaosk8s.pod.probes
      arguments:
        label_selector: app=go-demo-8
        ns: go-demo-8
        conditions:
        - type: Ready                                                #the pod should be in ready state with status ==true
          status: "True"
method:
- type: action
  name: terminate-pod
  provider:
    type: python
    module: chaosk8s.pod.actions
    func: terminate_pods
    arguments:
      label_selector: app=go-demo-8
      rand: true
      ns: go-demo-8
  pauses: 
    after: 10


diff chaos/terminate-pod-pause.yaml \
    chaos/terminate-pod-phase.yaml

chaos run chaos/terminate-pod-phase.yaml

echo $?

kubectl --namespace go-demo-8 \
    logs go-demo-8


Deploy mongodb as our application is dependent on it.

kubectl --namespace go-demo-8 \
    apply --filename k8s/db

kubectl --namespace go-demo-8 \
    rollout status \
    deployment go-demo-8-db

kubectl --namespace go-demo-8 \
    get pods

# Repeat the previous command until the `go-demo-8` Pod `STATUS` is `Running`

chaos run chaos/terminate-pod-phase.yaml

echo $?

#################################
# Making The App Fault-Tolerant #
#################################

We know pod is not fault tolerant so we will replace pod with a deployment to make it fault tolerant.

cat k8s/terminate-pods/deployment.yaml

kubectl --namespace go-demo-8 \
    apply --filename k8s/terminate-pods/deployment.yaml

kubectl --namespace go-demo-8 \
    rollout status \
    deployment go-demo-8

chaos run chaos/terminate-pod-phase.yaml

##############################
# Destroying What We Created #
##############################

cd ..

kubectl delete namespace go-demo-8
