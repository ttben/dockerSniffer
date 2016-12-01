# Description

This project allows one to fetch dockerfiles from the raw github API and analyse them.
It is splitted into maven submodules data, core, analyser that match the differents aspects of such requirements.

# Collecting of dockerfiles

## DockerHub

DockerHub is a repositories of docker images. This was our first target to gather docker images.
Our main requirement was to  avoid \textbf{downloading} images since a docker image can easily weight more than 500MB (the official version 3.5 of python image  weights ~680MB, the official java image weights ~640MB and node ~655MB) the amount of data to store would be too large. We first targeted the largest collections of docker images we known: the DockerHub. This hub hosts both official images (around 120 images)\footnote{\url{http://www.slideshare.net/Docker/dockercon-16-general-session-day-2-63497745}} and open non-official repositories (around 150 000 repositories\footnote{\url{https://www.ctl.io/developers/blog/post/docker-hub-top-10/}}). This hub is based on a registry that list all available images\footnote{\url{http://54.71.194.30:4014/reference/api/docker-io_api/}} through a \textit{catalogue} endpoint. This specification is currently not implemented by the docker company\footnote{https://github.com/docker/distribution/pull/653} therefore we can not list all available images in the hub. We decided to crawl a subset of names of those images and asks the docker API to get a description of layers and rebuild the dockerfile. To do so, we need a pair $(image name, tag name)$, therefore we need to get all tags of a specific image and retrieve the last one. Finally, we can ask the docker registry to get the description of $(image name, tag name)$ and parse the result. Even doing this leads to \textbf{partial} dockerfiles. Indeed, commands such as $ENV$ do not affect docker's file-system therefore do not create \textit{layers} and therefore are lost when building the image. Moreover, the parent-child relationship still needs to be established since the layer do not store explicitly the parent image ID.

![Process to retrieve dockerfiles from DockerHub](DockerImageToDockerFile.png)


## GitHub

Because of the problems encountered by generating the dockerfiles, and because this is not at the core of our contribution, we decided to put aside this and crawl \textit{dockerfiles} from GitHub. This communal platform allowed us to perform more or less specific request on the content of the \textit{dockerfiles} and gave us a random sample of it. This has must been done by crawling too.
We use a chrome-extension to crawl github content. We perform request on those kind or URLs:

https://github.com/search?p=100&q=language%3ADockerfile+FROM&ref=searchresults&type=Code&utf8=%E2%9C%93

to be able to find Dockerfile that, at least, contains a FROM code inside.
