grammar Dockerfile;

dockerfile: line+;

line: comment (NEWLINE)+ | command (NEWLINE)+ | ws* command (NEWLINE)+;
comment
  :  '#' ~( '\r' | '\n' )* | '/' | '.' | ')'| '('| '%' | '-'
  ;
command: (from | run | env | entrypoint | maintainer | workdir | add | multipleRun);

from: FROM ws (ANYKEYS);

run: RUN ws body ws*;
body: shellCmd (ws SHELLAND ws shellCmd)*;
shellCmd: ANYKEYS (ws ANYKEYS)*;

multipleRun: (.+?)+;

env: ENV (key ws value)+;
key: (LETTER | NUMBER)+;
value: (LETTER | NUMBER)+;

entrypoint: ENTRYPOINT ws ANYKEYS;

maintainer: MAINTAINER (. | '<' | '>' | '@' | ',')*;

workdir: WORKDIR (. | '<' | '>' | '@' | ',')*;

add: ADD .*?;

ws: (' '| '\t')+;

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

ANYKEYS: (LETTER | NUMBER | ':' | '_' | '-' | '/' | '|' | '"' | '=' | '*' | '\\' | '\'' | '+' | ']' | '[' | ';' | '!' | '~' | '.')+;
LIT:(LETTER | NUMBER)+;
LETTER: [a-zA-Z];
NUMBER: [0-9];