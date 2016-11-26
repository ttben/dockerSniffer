grammar Dockerfile;

dockerfile: ((COMMENT | command))+ EOF;

COMMENT
    :   ( '#' ~[\r\n]* '\r'? '\n'
        | '/*' .*? '*/'
        ) -> skip
    ;

command: one_line | run;
one_line: (from | env | entrypoint | maintainer | workdir | add | copy | expose) (NEWLINE);

from: FROM ANYKEYS;
maintainer: MAINTAINER ANYKEYS ANYKEYS;

env: (ENV key '=' LETTER+);
key: (LETTER | NUMBER)+;
value: (LETTER | NUMBER)+;

entrypoint: ENTRYPOINT ANYKEYS;

workdir: WORKDIR ANYKEYS;

add: ADD .*?;

copy: COPY src dest;
src: ANYKEYS | '.';
dest: ANYKEYS | '.';

expose: EXPOSE NUMBER;

run: RUN body NEWLINE;
body: shellCmd (SHELLAND shellCmd)* ;
shellCmd: ANYKEYS+;

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
ANYKEYS: (LETTER | NUMBER | ':' | '_' | '-' | '/' | '|' | '"' | '=' | '*' | '\\' | '\'' | '+' | ']' | '[' | '{' | '}' | ';' | '!' | '~' | '.' | '–' | '$' | '<' | '>' | '@' | ',')+;

NEWLINE: ('\n' | '\r')+;
WS : ((' ' | '\t')+) -> skip;
