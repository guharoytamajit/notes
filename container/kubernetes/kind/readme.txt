>sudo kind create cluster --name cluster1 --image kindest/node:v1.18.15@sha256:5c1b980c4d0e0e8e7eb9f36f7df525d079a96169c8a8f20d8bd108c0d0889cc4


other versions:
1.20: kindest/node:v1.20.2@sha256:8f7ea6e7642c0da54f04a7ee10431549c0257315b3a634f6ef2fecaaedb19bab
1.19: kindest/node:v1.19.7@sha256:a70639454e97a4b733f9d9b67e12c01f6b0297449d5b9cbbef87473458e26dca
1.18: kindest/node:v1.18.15@sha256:5c1b980c4d0e0e8e7eb9f36f7df525d079a96169c8a8f20d8bd108c0d0889cc4
1.17: kindest/node:v1.17.17@sha256:7b6369d27eee99c7a85c48ffd60e11412dc3f373658bc59b7f4d530b7056823e
1.16: kindest/node:v1.16.15@sha256:c10a63a5bda231c0a379bf91aebf8ad3c79146daca59db816fb963f731852a99
1.15: kindest/node:v1.15.12@sha256:67181f94f0b3072fb56509107b380e38c55e23bf60e6f052fbd8052d26052fb5
1.14: kindest/node:v1.14.10@sha256:3fbed72bcac108055e46e7b4091eb6858ad628ec51bf693c21f5ec34578f6180


>sudo kind create cluster --name cluster2 --config config.yaml

kind: Cluster
apiVersion: kind.x-k8s.io/v1alpha4
nodes:
- role: control-plane
  image: kindest/node:v1.16.4@sha256:b91a2c2317a000f3a783489dfb755064177dbc3a0b2f4147d50f04825d016f55
- role: worker
  image: kindest/node:v1.16.4@sha256:b91a2c2317a000f3a783489dfb755064177dbc3a0b2f4147d50f04825d016f55

ig.yaml
---------------------------------------------
Pointing to a cluster:

Option 1:
>kubectl cluster-info --context kind-cluster2

Option 2:
>sudo kind get kubeconfig --name cluster2 >$HOME/.kube/config


>kubectl config get-clusters
------------------------
delete cluster
>kind delete cluster --name cluster2

