pvars([x,y,z]).
pvar(X):-pvars(V),member(X,V).

power(X):-pvar(X),!.
power(X^Y):-pvar(X),integer(Y),Y>1,!.

coefficient(K):-number(K).

monomial(X):-pvar(X),!.
monomial(N):-number(N),!.
monomial(X):-power(X),!.
monomial(K*X):-coefficient(K),power(X),!.
monomial(X*K):-coefficient(K),power(X),!.

polynomial(M):-monomial(M),!.
polynomial(P+M):-monomial(M),polynomial(P),!.

poly2list(P,L):-reverse(L,L1),poly2list_aux(P,L1),!.

poly2list_aux(X,[X]):-monomial(X).
poly2list_aux(P+X,[X|L]):-monomial(X),poly2list_aux(P,L).
poly2list_aux(P-X,[-X|L]):-monomial(X),poly2list_aux(P,L).

delete(_,[],[]).
delete(X,[X|L],L2) :- delete(X,L,L2),!.
delete(X,[Y|L],[Y|L2]) :- X\==Y,delete(X,L,L2),!.

monparts(X^N,1,X^N) :- power(X^N),!.
monparts(-X^N,-1,X^N) :- power(X^N),!.
monparts(K*P,K,P) :- number(K),!.
monparts(K,K,indep) :- number(K),!.
monparts(X,1,X) :- pvar(X),!.
monparts(-X,-1,X) :- pvar(X),!.

aux_addmonomial(K,indep,K) :- !.
aux_addmonomial(0,_,0) :- !.
aux_addmonomial(1,XExp,XExp) :- !.
aux_addmonomial(-1,XExp,-XExp) :- !.
aux_addmonomial(K,XExp,K*XExp).
addmonomial(K1,K2,K3) :- number(K1),number(K2),!,K3 is K1+K2.
addmonomial(M1,M2,M3) :- monparts(M1,K1,XExp),monparts(M2,K2,XExp),K3 is K1+K2,aux_addmonomial(K3,XExp,M3).

addver([],X,X,[]).
addver([Y|L],X,Z,L2) :- addmonomial(X,Y,Z1),addver(L,Z1,Z,L2).
%caso nao seja possivel somar
addver([Y|L],X,Z,[Y|L2]) :- addver(L,X,Z,L2).  

simpoly_list([],[]).
%L2 -> lista resultante do corte do elemento somado
simpoly_list([X|L],[Z|L1]) :- addver(L,X,Z,L2),simpoly_list(L2,L1),!. 
simpoly_list(L,[0|L1]) :- simpoly_list(L,L1).

simpoly(P,P1) :- poly2list(P,L),simpoly_list(L,L2),poly2list(P1,L2).

multmonoscale(X,M,M1) :- monparts(M,M,indep),M1 is M*X,!.
multmonoscale(X,M,M1) :- monparts(M,A,B),C is A*X,monparts(M1,C,B).

multscale(_,[],[]).
multscale(X,[Y|L],[Z|L1]) :- multmonoscale(X,Y,Z),multscale(X,L,L1).

%transforma em lista, aplica predicado multscale e transforma em polinomio
scalepoly(X,P,P1) :- poly2list(P,L),multscale(X,L,L1),poly2list(P1,L1),!.

%transforma em lista P e P1, junta ambas as listas em L2, simplifica L2 e transforma-a num polinomio em P3
addpoly(P,P1,P3):-poly2list(P,L),poly2list(P1,L1),append(L,L1,L2),simpoly_list(L2,L3),poly2list(P3,L3).

       /**************************SECOND HALF*************************************/
replace(_, _, [], []).
replace(O, R, [O|T], [R|T2]) :- replace(O, R, T, T2),!.
replace(O, R, [H|T], [H|T2]) :- H \= O, replace(O, R, T, T2),!.

atom2string([],[]).
atom2string([X|L],[Y|L1]):-atom_string(Y,X),atom2string(L,L1).

text2poly(T,X):-split_string(T," ","",L),atom2string(L,L1),p(Y,L1,[]),atom_to_term(Y,X,[]),!.

p(X)-->m(X).
p(X)-->m(Y),[plus],p(Z),{atomic_list_concat([Y,'+',Z],X)}.
p(X)-->m(Y),[minus],p(Z),{atomic_list_concat([Y,'-',Z],X)}.
p(X)-->m(Y),[plus],[minus],p(Z),{atomic_list_concat([Y,'-',Z],X)}.

var(X)-->[x],{X=x};
         [y],{X=y};
         [z],{X=z}.

unit(X)-->[zero],{X is 0};
          [one],{X is 1};
          [two],{X is 2};
          [three],{X is 3};
          [four],{X is 4};
          [five],{X is 5};
          [six],{X is 6};
          [seven],{X is 7};
          [eigth],{X is 8};
          [nine],{X is 9}.

tentonine(X)-->[ten],{X is 10};
               [eleven],{X is 11};
               [twelve],{X is 12};
               [thirteen],{X is 13};
               [fourteen],{X is 14};
               [fifteen],{X is 15};
               [sixteen],{X is 16};
               [seventeen],{X is 17};
               [eigthteen],{X is 18};
               [nineteen],{X is 19}.

dez(X)-->[twenty],{X is 20};
         [thirty],{X is 30};
         [fourty],{X is 40};
         [fifty],{X is 50};
         [sixty],{X is 60};
         [seventy],{X is 70};
         [eighty],{X is 80};
         [ninety],{X is 90}.

dezunit(X)-->dez(Y),unit(Z),{X is Y+Z}.

cent(X)-->unit(Y),[hundred],{X is 100*Y}.
cent(X)-->unit(Y),[hundred],{Y2 is 100*Y},[and],unit(Z),{X is Y2+Z}.
cent(X)-->unit(Y),[hundred],{Y2 is 100*Y},[and],tentonine(Z),{X is Y2+Z}.
cent(X)-->unit(Y),[hundred],{Y2 is 100*Y},[and],dez(Z),{X is Y2+Z}.
cent(X)-->unit(Y),[hundred],{Y2 is 100*Y},[and],dezunit(Z),{X is Y2+Z}.

number(X)-->unit(X).
number(X)-->tentonine(X).
number(X)-->dez(X).
number(X)-->dezunit(X).
number(X)-->cent(X).

power(X)-->var(Y),[raised,to],number(Z),{atomic_list_concat([Y,'^',Z],X)}. 
power(X)-->var(Y),[squared],{atomic_list_concat([Y,'^',2],X)}.
power(X)-->var(Y),[cubed],{atomic_list_concat([Y,'^',3],X)}.

coefficient(X)-->number(X).
    
m(X)-->var(X).
m(X)-->number(X).
m(X)-->power(X).
m(X)-->coefficient(Y),[times],var(Z),{atomic_list_concat([Y,'*',Z],X)}.
m(X)-->coefficient(Y),[times],power(Z),{atomic_list_concat([Y,'*',Z],X)}.

polyplay :-
    writeln('Welcome to PolyPlay !!'),polyplay_aux.
polyplay_aux :-
    prompt(_, 'Type a instruction or \'bye\''),
    read(Instruction),
    ( Instruction == bye
    ->writeln('See ya')
    ;split_string(Instruction," ","",L),polyplay_aux1(L,Y),writeln(Y),polyplay_aux).

polyplay_aux1(L,Y):- 
    member("show",L),
    delete("show",L,L1),
    atom2string(L1,L2),
    p(X,L2,[]),
    atom_to_term(X,Y,[]).


    
/*polyplay_aux :-
    writeln("Type an instruction or type 'bye' to quit"),
    read(Instruction),
    ( Instruction == bye
    ->writeln('See ya');
    split_string(Instruction," ","",L),
    member("show",L),
    delete("show",L,L1),
    atom2string(L1,L2),
    p(X,L2,[]),
    atom_to_term(X,Y,[]),
    writeln(Y),
    polyplay_aux;
    member("multiply",L),
    delete("multiply",L,L1),
    replace("by","times",L1,L2),
    atom2string(L2,L3),
    p(X,L3,[]),
    atom_to_term(X,Y,[]),
    writeln(Y),
    polyplay_aux;
    polyplay_aux).*/
    

/*polyplay :- 
    writeln("Welcome to PolyPlay!"),
    writeln("Type an instruction or type 'exit' to quit"),
    read(Inst),
    %(Inst == exit -> writeln('See ya'),!),
    Inst\='exit',
    split_string(Inst," ","",L),
    member("add",L),
    delete("add",L,L1),
    replace("to","plus",L1,L2),
    atom2string(L2,L3),
    p(Y,L3,[]),
    atom_to_term(Y,X,[]),
    simpoly(X,R),
    write("Result: "),
    writeln(R),
    polyplay.*/
    

    
    