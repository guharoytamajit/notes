One thing to consider when deploying your AKS instance is the size of your worker nodes.  For the most part you shouldn't need larger disks than the default for the service, as the normal best practice for Kubernetes is to use remote mapped persistent storage when needed, but if you do have a need for more local scratch space, you can define the default disk size at cluster create time by passing the disk size parameter:

--node-osdisk-size
Size in GB of the OS disk for each node in the node pool. Minimum 30 GB.

You can also use a different default node, which may be appropriate if you are running heavy containers (e.g. containers that are going to consume large amount of compute resource).  By default, the environment is set to deploy a maximum of 30 PODs to a single node, so in addition to the node virtual machine size, you can also specify the maximum number of PODs to deploy to a node.

--node-vm-size
Size of Virtual Machines to create as Kubernetes nodes.
default value: Standard_DS2_v2

Get a list of sizes with the az vm command:
az vm list-sizes -l eastus

You can also limit the number of pods scheduled to a node which is useful in conjuction with the autoscaling extension to force the deployment of more nodes as needed.
--max-pods
Maximum number of PODs to schedule to a node


