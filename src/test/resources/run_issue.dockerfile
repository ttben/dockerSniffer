FROM toto
RUN apt-get upgrade
RUN apt-get install X
RUN apt-get install Y
RUN apt-get upgrade && apt-get install Z