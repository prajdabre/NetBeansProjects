#include "dicts/other.fst"
#include "nominal.fst"
#include "verbal.fst"

%Start expression
$S$ = 	$NOMINAL$			|\
	$PRONOUN$			|\
	$ADJ$				|\
	$ADVS$				|\
	$INT$				|\
	$CON$ $SSY$?				|\
	$COM$ $SSY$? 			|\
	$AK$ $SSY$? 			|\
	$KRS$				|\
	$CARDINAL$ 			|\
	$PART$			|\
	%$E_OF$ $E_CL$ $SSY$? |\
	[\?\-–\"\,'\$\#@!\%\^\*\(\)\[\]\|‘’“”\:\+\;\=\!]+ <PUN>:<> |\
	$NUM$
	

%$STR$=($S$ (<PUN>:\. | <PUN>:\,)?) | (<PUN>:\. | <PUN>:\,)
$STR$=($S$ ((\. | \,)+ <PUN>:<>)?) | ((\. | \,)* [\?\-–\"\,'\$\#@!\%\^\*\(\)\[\]\|‘’“”\:\+\;\=\!]*)* <PUN>:<> | $S$ ([\-/] <PUN>:<>) $S$ | (\( <PUN>:<>) $S$ (\) <PUN>:<>) | (" <PUN>:<>)? $S$ (" <PUN>:<>)? | (' <PUN>:<>)? $S$ (' <PUN>:<>)?  
$STR$ 


