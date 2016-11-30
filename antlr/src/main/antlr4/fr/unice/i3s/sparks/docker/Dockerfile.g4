grammar Dockerfile;

dockerfile: ((COMMENT | command))+ EOF;

COMMENT :   ( '#' ~[\r\n]* '\r'? '\n') -> skip ;
WS : ((' ' | '\t')+) -> skip;

command: one_line | run;
one_line: (from | env | entrypoint | maintainer | workdir | add | copy | expose) (NEWLINE);

from: FROM NOTNEWLINE;

maintainer: MAINTAINER NOTNEWLINE+;

env: (ENV envBody);
envBody: (LETTER | NUMBER)+ '=' (LETTER | NUMBER)+;

entrypoint: ENTRYPOINT NOTNEWLINE+;

workdir: WORKDIR NOTNEWLINE+;

add: ADD .*?;

copy: COPY src dest;
src: NOTNEWLINE;
dest: NOTNEWLINE;

expose: EXPOSE NUMBER;

run: RUN body;
body: shellCmd | shellCmd SHELLAND shellCmd;
shellCmd: NOTNEWLINEBCKSLASH '\\' NEWLINE | NOTNEWLINEBCKSLASH;
NOTNEWLINEBCKSLASH: ~[\r\n\\];

SHARP: '#';
FROM: [fF][rR][oO][mM];
ENV: [eE][nN][vV];
RUN: [rR][uU][nN];
ENTRYPOINT: [eE][nN][tT][rR][yY][pP][oO][iI][nN][tT];
MAINTAINER: [mM][aA][iI][nN][tT][aA][iI][nN][eE][rR];
WORKDIR: [wW][oO][rR][kK][dD][iI][rR];
SHELLAND: '&&' | ('\\' NEWLINE '&&');
ADD: [aA][dD][dD];
COPY: [cC][oO][pP][yY];
EXPOSE: [eE][xX][pP][oO][sS][eE];

NUMBER: [0-9];
LETTER: [a-zA-Z];

NEWLINE: ('\n' | '\r')+;
NOTNEWLINE: ~[\r\n];
