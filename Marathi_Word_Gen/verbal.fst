#include "dicts/verbs.fst"
#include "nominal.fst"

%Verbs show a lots of irregular behaviour. Here, this automata focusses on this irregularity rather than morphotactics. 
%It is very difficult to explain the REs here
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%Generalized string

%बस ( तो ) , ब ( से ) , ब ( सेल ) , ब ( सू ) , ब ( सावा ) , बस ( ता ) , ब ( सायचा ) 
AK_STR1 = ( TA1_AK ( Y | TA ) ? | E_CHANGE_AK1 NA ? | EL_CHANGE_AK1 | U_CHANGE_AK1 | VA_CHANGE_AK1 | TA2_AK | YACHA_CHANGE_AK1 ) 

%खा ( तो ) , खा ( ई ) , खा ( ईल ) , खा ( ऊ ) , खा ( वा ) , खा ( ता ) , खा ( यचा ) 
AK_STR2 = ( TA1_AK ( Y | TA ) ? | E_CHANGE_AK2 NA ? | EL_CHANGE_AK2 | U_CHANGE_AK2 | VA_CHANGE_AK2 | TA2_AK | YACHA_CHANGE_AK2 ) 

%खा ( तो ) , खा ( ई ) , खा ( ईल ) , खा ( ऊ ) , खा ( ता ) 
AK_STR31 = ( TA1_AK ( Y | TA ) ? | E_CHANGE_AK2 NA ? | EL_CHANGE_AK2 | U_CHANGE_AK2 | TA2_AK ) 

%खा ( वा ) , खा ( यचा ) 
AK_STR32 = ( VA_CHANGE_AK2 | YACHA_CHANGE_AK2 ) 

%In case of verbs which express emotions there is only 3rd person, singular, nwuter form मळमळते
AK_STR4 = ( TA1_AK_EMO ( Y | TA ) ? | TA2_AK_EMO | E_AK_EMO NA ? | EL_AK_EMO | U_AK_EMO | VA_AK_EMO | YACHA_AK_EMO | LA_AK_EMO ( Y | TA ) ? ) 

AK_STR5 = ( TA1_AK ( Y | TA ) ? | E_CHANGE_AK1 NA ? | EL_CHANGE_AK1 | U_CHANGE_AK1 | TA2_AK ) 

%%%%%%%%%%%%%%
%Sakarmak
%LA_AK1: इच्छि ( ला ) 
%V1: इच्छ
%V!_KR: इच्छि
AK1 = V1 ( AK_STR1 | ( LA_AK | LA_AK_0 ) ( Y | TA ) ? ) | V1_KR ( ( LA_AK | LA_AK_0 ) ( Y | TA ) ? | TA1_AK ( Y | TA ) ? | TA2_AK ) 

%Akarmak
%Difference is: Sakarmak verbs don't have 1st person forms
%V2: बस
AK2 = V2 ( AK_STR1 | ( LA_AK2 | LA_AK ) ( Y | TA ) ? ) 

%Akarmak
%V3: निघ
AK3 = V3 AK_STR5 | V3_KR ( ( LA_AK | LA_AK2 ) ( Y | TA ) ? | AK_STR32 ) 

%Sakarmak
%V4: माग
AK4 = ( V4 AK_STR1 ) | V4_KR ( LA_AK | LA_AK_0 ) ( Y | TA ) ? 

%Akarmak
%V5: राह
AK5 = V5 ( AK_STR1 ) | V5_KR ( LA_AK | LA_AK2 ) ( Y | TA ) ? 


%Sakarmak
%V6: खा
AK6 = V6 AK_STR2 | V6_KR LA_AK ( Y | TA ) ? 

%Akarmak
AK7 = ( V71 | V111 | V121 | V131 | V191 ) AK_STR31 | ( V72 | V112 | V122 | V132 | V192 ) ( LA_AK2 | LA_AK ) ( Y | TA ) ? | ( V73 | V111 | V121 | V131 | V193 ) AK_STR32

%Sakarmak
AK8 = ( V81 | V91 | V101 | V141 ) AK_STR31 | ( V82 | V92 | V101 | V112 | V142 ) ( LA_AK | LA_AK_0 ) ( ( Y | TA ) | TA ) ? | ( V83 | V93 | V103 | V143 ) AK_STR32

%Akarmak
AK9 = V151 AK_STR1 | V152 ( LA_AK2 | LA_AK ) 

AK10 = ( V161 | V211 ) AK_STR1 | ( V162 | V212 ) ( LA_AK1 | LA_AK | LA_AK_0 | LA_AK_1 ) ( ( Y | TA ) ? | TA ) ? 

AK11 = V171 ( TA1_AK Y ? | TA2_AK ) | V172 ( LA_AK | LA_AK_0 ) ( ( Y | TA ) ? | TA ) ? | V173 ( E_CHANGE_AK1 NA ? | EL_CHANGE_AK1 | U_CHANGE_AK1 | VA_CHANGE_AK1 | YACHA_CHANGE_AK1 ) 

AK18 = V18 ( AK_STR1 | LA_AK2 ) 

AK19 = V20 AK_STR4

AK_LIH = V222 ( E_CHANGE_AK1 NA ? | EL_CHANGE_AK1 | U_CHANGE_AK1 | VA_CHANGE_AK1 | YACHA_CHANGE_AK1 | LA_AK1 ( ( Y | TA ) ? | TA ) ? ) | V221 ( TA1_AK ( Y | TA ) ? | TA2_AK ) 

AK = AK1 | AK2 | AK3 | AK4 | AK5 | AK6 | AK7 | AK8 | AK9 | AK10 | AK11 | AK18 | AK19 | AK_LIH

AKYS = AK SSY ?

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%_________________________________Krudantas_________________________________

VERBS = V1 | V1_KR | V2 | V3 | V4 | V5 | V6 | V71 | V81 | V91 | V101 | V111 | V121 | V131 | V141 | V151 | V161 | V171 | V18 | V191 | V20 | V211 | V221

VERBS1 = V1 | V2 | V3 | V4 | V5 | V151 | V161 | V18 | V211 | V20 | V222

VERBS2 = V6 | V71 | V81 | V91 | V101 | V111 | V121 | V131 | V141 | V171 | V191 | V20 


VERBS_LE = V1 | V1_KR | V2 | V3_KR | V4_KR | V5_KR | V6_KR | V72 | V82 | V92 | V101 | V122 | V132 | V142 | V152 | V162 | V172 | V18 | V192 | V20 | V212 | V221

VERBS2_YALA = V6 | V73 | V83 | V93 | V103 | V111 | V121 | V131 | V143 | V193 

KR_DF = VERBS ( NE_DF | NARA_DF | NAR ) | VERBS_LE ( LELA_DF | LE_DF ) | ( VERBS1 | V173 ) ( VA_KR1_DF | YACHA_KR1_DF ) | VERBS2_YALA ( VA_KR2_DF | YACHA_KR2_DF ) 

KR_OF = VERBS NE_OF | VERBS_LE LE_OF 

KR_ADJ_OF = VERBS NARA_ADJ_OF | VERBS_LE LELA_ADJ_OF | ( VERBS1 | V173 ) ( VA_KR1_OF | YACHA_KR1_OF ) | VERBS2_YALA ( VA_KR2_OF | YACHA_KR2_OF ) 

KR_ADJ_OF_REQ = VERBS NARA_ADJ_OF_REQ | VERBS_LE LELA_ADJ_OF_REQ

%KR_OF_PL = VERBS ( NARA_OF_PL | LELA_OF_PL ) 
KR_OF_PL = VERBS NARA_OF_PL | VERBS_LE LELA_OF_PL

KR_ADV = VERBS ( T | TANA ) | VERBS1 ( OON1 | OO1 ) | VERBS2 ( OON2 | OO2 ) | ( VERBS1 | V173 ) YALA_KR1 | VERBS2_YALA YALA_KR2 | ( V1_KR1 | V14_KR1 | V16_KR1 | V17_KR1 | V21_KR1 | V22_KR1 ) T

KRS = ( KR_DF | KR_ADV ) SSY ? | KR_OF OF_STR_SG | KR_OF_PL OF_STR_PL | ( KR_ADJ_OF ) ( OF_STR_SG | SSY ) ? 	 | KR_ADJ_OF_REQ ( OF_STR_SG ) 	 
