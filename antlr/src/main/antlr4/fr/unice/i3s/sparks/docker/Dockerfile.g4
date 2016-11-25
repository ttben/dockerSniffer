grammar Dockerfile;

dockerfile: line+;

line: comment (NEWLINE)+ | command (NEWLINE)+ | ws* command (NEWLINE)+;
comment
  :  '#' ~( '\r' | '\n' )* | '/' | '.' | ')'| '('| '%' | '-'
  ;
command: (from |  env | run |entrypoint | maintainer | workdir | add | multipleRun | copy);

from: FROM ws (ANYKEYS);



env: ENV (key ws value)+ | (ENV ws key '=' value);
key: (LETTER | NUMBER)+;
value: (LETTER | NUMBER)+;

entrypoint: ENTRYPOINT ws ANYKEYS;

maintainer: MAINTAINER (. | '<' | '>' | '@' | ',')*;

workdir: WORKDIR (. | '<' | '>' | '@' | ',')*;

add: ADD .*?;

copy: COPY src ws dest;

src: ANYKEYS;
dest: ANYKEYS;

ws: (' '| '\t')+;

run: RUN ws body ws*;
body: shellCmd (ws SHELLAND ws shellCmd)*;
shellCmd: ANYKEYS (ws ANYKEYS)*;
multipleRun: (.+?)+;

NEWLINE: '\n';
SHARP: '#';
FROM: [fF][rR][oO][mM];
ENV: [eE][nN][vV];
RUN: [rR][uU][nN];
ENTRYPOINT: [eE][nN][tT][rR][yY][pP][oO][iI][nN][tT];
MAINTAINER: [mM][aA][iI][nN][tT][aA][iI][nN][eE][rR];
WORKDIR: [wW][oO][rR][kK][dD][iI][rR];
SHELLAND: '&&';
ADD: [aA][dD][dD];
COPY: [cC][oO][pP][yY];

ANYKEYS: (LETTER | NUMBER | ':' | '_' | '-' | '/' | '|' | '"' | '=' | '*' | '\\' | '\'' | '+' | ']' | '[' | '{' | '}' | ';' | '!' | '~' | '.' | '–' | '$')+;
LIT:(LETTER | NUMBER)+;
LETTER: [a-zA-Z];
NUMBER: [0-9];