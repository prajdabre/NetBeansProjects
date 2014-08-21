#include "dicts/suffixes.fst"
#include "dicts/other.fst"
#include "dicts/pn.fst"

%________________Closing suffixes________________
%Normal closing
%PP_OPN: खाल, माग, पुढ
%SP_CL: ईल, ऊन
%PP_AVY: खालील, पुढून

PP_AVY = PP_OPN SP_CL
%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%CL: पावेतो, त
%PP_CL: मागे, खाली, पुढे
%AD_CL: सारखा, सारखी
%CH_CL: चा, ची 
%VI: case markers applicable to singular and plural both
%CLS_COM: Closing suffixes which behave the same in all the numbers
CLS_COM = CL | PP_AVY | PP_CL | AD_CL | CH_CL | VI
%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%CLS_SG: Closing suffixes attachable to singular forms
CLS_SG = VI_SG | CLS_COM 
%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%Closing suffixes attachable to plural forms
CLS_PL = VI_PL | CLS_COM
%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%________________Oblique forms of the adjectival suffixes________________
%Singular oblique forms of the adjectival suffixes: सारख्या, च्या
OF_SUF_SG = AD_OF | CH_OF 

%Plural oblique forms of the adjectival suffixes: सारख्यां, च्यां
OF_SUF_PL = AD_OF_PL | CH_OF_PL

%All the oblique forms of the adjectival suffixes
OF_SUF_ALL = OF_SUF_SG | OF_SUF_PL
%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%________________Oblique forms of the adjectival suffixes which follow the PPs________________
%PP: समान, योग्य, विना
%PP_OF_SG: मागच्या, साठीच्या
PP_OF_SG = ( PP | PP_OPN | PP_REQ ) CH_OF | PP_OPN L_OF

%PP_OF_SG: मागच्यां, साठीच्यां
PP_OF_PL = ( PP | PP_OPN | PP_REQ ) CH_OF_PL | PP_OPN L_OF_PL
%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%________________Closing forms starting with PPs________________
%A string that start with a proposition and closes: मागच्याने, साठीच्यांनी 
PP_CLS = PP_OF_SG CLS_SG ? | PP_OF_PL CLS_PL | ( PP_OPN | PP | PP_REQ ) CH_CL | PP_OPN L_CL | PP | PP_REQ | ( PP | PP_OPN | PP_REQ ) CH_OF_REQ CLS_SG | PP_OPN L_OF_REQ CLS_SG | CHA_OF CHA_PP_CL
%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%________________A string that starts with an OF of suffix and closes:सारख्याने, सारख्यांनी, सारख्यांसाठी, सारखीने ________________
OF_CLS = OF_SUF_SG CLS_SG ? | OF_SUF_PL CLS_PL | OF_SUF_ALL ( PP | PP_REQ ) | ( AD_OF_REQ | CH_OF_REQ ) CLS_SG


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%NOMINAL SUFFIX STRINGS%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%A string that can follow any oblique form: मागच्याने, साठीच्यांनी, सारख्याने, सारख्यांनी, सारख्यांसाठी, सारखीने
OF_STR_CLS = OF_CLS | PP_CLS

%The string that can follow only an oblique singular form 
OF_STR_SG = ( OF_STR_CLS | CLS_SG ) SSY ? 

%The string that can follow an oblique plural form 
OF_STR_PL = ( OF_STR_CLS | CLS_PL ) SSY ? | SM_PL

% = = = = = = = = = Final Regex = = = = = = = = = = = = = 
%OF_REQ: पांढरी, अशी they require a suffix after them.
NOMINAL = DF SSY ? | OF OF_STR_SG ? | OF_PL OF_STR_PL | OF_REQ OF_STR_SG | OF_CHA ( CH_CL | CH_OF | CH_OF_PL | CH_OF_REQ )	


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%PRONOUN SUFFIX STRINGS%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%The only difference between OF_STR_PL and SW_STR_PL is the presence of SM_PL. Otherwise the SWATAH pronoun behaves very similar to that of nouns.

SW_STR_PL = ( OF_STR_CLS | CLS_PL ) SSY ? 
SW_PRONOUN = SW_OF SSY ? OF_STR_SG ? | SW_OF_PL SW_STR_PL 

%In case of PN2s the forms with vibhakti pratyayas are stored. Hence there is no need of including vibhaktis
%तो, हा
PN_STR = ( OF_STR_CLS | CLS_COM ) SSY ? 
%PN2_ADJ_OF SSY ? is required because त्याने is m/n but त्या is gender indepndant.
PN2 = ( PN2_OF | PN2_OF_PL ) PN_STR | PN2_ADJ_OF SSY ? 

%आपल्यासाठी
PN_STR_OTHER = OF_STR_SG ? SSY ? 
APAN_PRONOUN = AP_OF ( LA_CL SSY ? | LA_OF PN_STR_OTHER )

%आमचा, तुमचा, तुझा, माझा, हिचा, तिचा followed by suffixes
%PN1_OF:तु, मा
%PN1_OF_PL: तुम, आम 
CHA_PRONOUNS = PN1_OF ( JH_CL | JH_OF PN_STR_OTHER ) | ( PN1_OF_PL | PN3_OF | PN2_OF | PN2_OF_PL ) ( CHA_CL SSY ? | CHA_OF PN_STR_OTHER )

PRONOUN = SW_PRONOUN | PN2 | APAN_PRONOUN | CHA_PRONOUNS
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%ADJECTIVES%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%Adj. The oblique form of adjective when followed by suffixes derives noun. When there is only oblique form, it is just adjective OF
ADJ = ADJ_OF SSY ? 

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%ADVERBS%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%CHA_ADV: आता
ADV_STR = CHA_ADV ( ( CH_CL | CH_OF ( PP | CLS_SG ) ? | CH_OF_PL ( CLS_PL ) ? ) | ( AD_CL | CL | PP ( CH_OF | CH_CL ) ? | PP_AVY ) ) ? SSY ? 


PP_CL_WO_REQ = PP_OF_SG CLS_SG ? | PP_OF_PL CLS_PL | ( PP_OPN | PP | PP_REQ ) CH_CL | PP_OPN L_CL | PP | ( PP | PP_OPN ) CH_OF_REQ CLS_SG | PP_OPN L_OF_REQ CLS_SG | CHA_OF CHA_PP_CL
PP_ADV = ( PP_CL_WO_REQ | PP_CL | PP_AVY ) SSY ? 			

NST_OF_SG = NST_OPN CH_OF | NST_OPN L_OF
NST_OF_PL = NST_OPN CH_OF_PL | NST_OPN L_OF_PL
NST_CLS = NST_OF_SG CLS_SG ? | NST_OF_PL CLS_PL | NST_OPN ( CH_CL | L_CL | CH_OF_REQ CLS_SG | L_OF_REQ CLS_SG | CHA_OF CHA_PP_CL )

NST_ADV = NST_CL SSY ? | NST_OPN ( SP_CL ) | NST_CLS

ADVS = ( ADV_STR | ADV | PP_ADV | NST_ADV ) SSY ? 


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%NUMS%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%NUM = [०-९]+( ( \,[०-९]+ )* | ( \.[०-९]+ ) ? ) <NUM>:<> ( ( CLS_SG | CLS_PL | AD_CL | CH_CL | VA_CL | VA_OF ) | \ ) <PUN>:<> ) ? 

CARDINAL = CAR OF_STR_SG ? SSY ? 

CONJ = CON SSY ?

COMS = COM SSY ? | COM CM

EOFCLS = E_OF E_CL SSY ?

PARTS = PART
