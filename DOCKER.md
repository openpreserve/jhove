# JHOVE Docker image

The [`Dockerfile`](Dockerfile) in this directory builds a Docker image for JHOVE. The image is based on the [Debian Linux](https://www.debian.org/) distribution and contains the JHOVE application and a custom Java runtime built using the [Eclipse Temurin Docker image](https://hub.docker.com/_/eclipse-temurin). The JHOVE image is available from [Docker Hub](https://hub.docker.com/r/openpreserve/jhove/) and can be pulled with the command:

    docker pull openpreserve/jhove

Please use [GitHub issues](https://github.com/openpreserve/jhove/issues/new/) to report any problems with the Docker image.

## Examples

### Test JHOVE image by reporting module versions

    docker run --rm openpreserve/jhove

### Validate an XML file in JHOVE project root

    docker run --rm -v $(pwd):$(pwd) -w $(pwd) openpreserve/jhove -m XML-hul -h XML ./docker-install.xml

### Validate a PNG file online

    docker run --rm openpreserve/jhove -m JPEG-hul -h XML "https://openpreservation.org/wp-content/uploads/2019/12/veraPDF-shadow-160x83.jpg"

### Grab output to a local file

    docker run --rm openpreserve/jhove -m JPEG-hul -h XML "https://openpreservation.org/wp-content/uploads/2019/12/veraPDF-shadow-160x83.jpg" - > jhove-output.xml

## Building the Docker image

You'll need Maven installed locally to wrangle the project version, otherwise pass your own. From the project root run:

    docker build --build-arg JHOVE_VERSION="$(mvn -q help:evaluate -Dexpression=project.version -DforceStdout=true)"  -t openpreserve/jhove-test .
