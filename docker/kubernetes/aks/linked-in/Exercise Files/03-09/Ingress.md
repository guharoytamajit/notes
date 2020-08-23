There are multiple ways to implement Ingress in an Azure cluster, but we can make use
of an extension that also includes a DNS service extension.

Firstly, let's add the add-on Ingress controller and DNS service:

az aks enable-addons --resource-group myResourceGroup --name myAKSCluster --addons http_application_routing

Then we can check to see what the root of our applications DNS will be:

$ az aks show --resource-group myResourceGroup --name myAKSCluster --query addonProfiles.httpApplicationRouting.config.HTTPApplicationRoutingZoneName -o table

When we create an ingress and assoicate it with our application service, we will add the ingress name to this dns domain to get our DNS 'pointer' back to our application.  e.g. if we create an ingress called hostname, then our new pointer will be:

hostname.{DNS_root}

