As ingress tries to match resoruces, and our DNS controller (part of the Azure ingress extension) will automatically add its cluster specific domain, we will need to add the DNS name to our
ingress controller definition.  change the <CLUSTER_SPECIFIC_DNS_ZONE> to the zone discovered
in the previous section with:
az aks show --resource-group myResourceGroup --name myAKSCluster --query addonProfiles.httpApplicationRouting.config.HTTPApplicationRoutingZoneName -o table

or if you stored the name from the previous chapter:
cat ../dns_root.txt

In the file the line is:
  - host: hostname.<CLUSTER_SPECIFIC_DNS_ZONE>

Then apply the ingress to our environment:
kubectl apply -f hostname_ingress.yml

and then we can test for dns update (which may take a few minutes):

while [ 1 ] ; do
curl http://hostname.<CLUSTER_SPECIFIC_DNS_ZONE>
sleep 30
done

Or paste the URL into your browser, and refresh every minute or so until the DNS resource has propogated through the internet.

