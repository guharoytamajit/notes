Once we've pushed an image to the Azure Registry, we should verify that we can download and run the image. First, we should still have a tagged version in our local images repository:
docker images | grep hostname

The longer name version (as there may be multiple tags) is the one that's also associated with the Azure Registry that we created in the previous chapter. In order to verify that we can pull from the registry, we'll first remove the image, and then pull the image down from the registry (replacing <registry/image:version> with the registry taged name from the previous command):

docker rmi <registry/image:version>
docker pull <registry/image:version>

It should also then be possible to run the image locally to verify operation:

docker run --rm --name hostname -p 8080:80 <registry/image:version>
curl localhost:8080
docker stop hostname
docker rmi <registry/image:version>

