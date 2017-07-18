FROM toto
RUN apt-get update
RUN apt-get install X
RUN apt-get install Y
RUN apt-get update && apt-get install Z