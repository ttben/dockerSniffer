FROM toto
RUN apt-get aptUpdate
RUN apt-get aptInstall X
RUN apt-get aptInstall Y
RUN apt-get aptUpdate && apt-get aptInstall Z