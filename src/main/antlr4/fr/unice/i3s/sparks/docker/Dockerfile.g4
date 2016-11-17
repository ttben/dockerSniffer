grammar Dockerfile;

dockerfile: line+;

line: comment (NEWLINE)+ | command (NEWLINE)+;
comment
  :  '#' ~( '\r' | '\n' )* | '/' | '.' | ')'| '('| '%' | '-'
  ;
command: (from | run | env | entrypoint | maintainer | workdir);

from: FROM ws (ANYKEYS);

run: RUN ws body;
body: shellCmd (SHELLAND shellCmd)*;
shellCmd: ANYKEYS (ws+ ANYKEYS)*;

env: ENV (key ws value)+;
key: (LETTER | NUMBER)+;
value: (LETTER | NUMBER)+;

entrypoint: ENTRYPOINT ' ' '["' LIT '"]';

maintainer: MAINTAINER (. | '<' | '>' | '@' | ',')*;

workdir: WORKDIR (. | '<' | '>' | '@' | ',')*;

other: (.)+;

ws: (' '| '\t')+;
NEWLINE: '\n';
SHARP: '#';
FROM: 'from' | 'FROM';
ENV: 'env' | 'ENV';
RUN: 'run' | 'RUN';
ENTRYPOINT: 'entrypoint'| 'ENTRYPOINT';
MAINTAINER: 'maintainer'| 'MAINTAINER';
WORKDIR: 'workdir'| 'WORKDIR';
SHELLAND: '&&';

LETTER: [a-zA-Z];
NUMBER: [0-9];
LIT:(LETTER | NUMBER)+;
ANYKEYS:(LETTER | NUMBER | ':' | '_' | '-' | '/' | '|' | '"' | '=' | '.' | '*' | '\\' | '\'' | '+' | ']' | '[' | ';' | '!' | '~')+;
