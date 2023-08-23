% Math Ideas for programming
% 2010 - 2023
% Collected by M. Gr.

# 0. Smaller ideas

## Hamming distance
The Hamming or editing distance between to character sequences is the number of characters we have to change in one sequence to transform it into the other sequence.

Example 1: Between "abcdefg" and "abbdeeg" it is 2: 'c'->'b' and 'f'->'e' (replace)
Example 2: Between "Hallo" and "Hell" it is 2: 'a'->'e', 'o'->'' (delete)
Example 3: Between "aloa" and "Hallo" it is 3: (0)''->'H', (2)''->'l', (4)'a'->'' (add and delete)

Given two strings, determine their Hamming distance!

### Twist
Given two integers, determine the Hamming distance of their binary representations.

### Follow up Questions:
1. What is your algorithm's complexity?
2. Can you do that in $\mathcal{O}(1)$?


## Fractional School

In the fractional school every child is given $n$ fractions (of numerator and denominator with natural numbers, the denominator not being 0).  When a child claps once, it can multiply either one numerator or one denominator with a natural number (not 0 for denominators).  What is the minimum number of claps that is needed to make all fractions the same value?


## Reflective Numbers

A positive integer is called reflective iff it reads the same when point reflected through its middle.  The reflective digits are 0, 1, 2, 5, 8.  E.g. 69 is reflective too.  You are to compute the (number of) reflective numbers in an interval of non-negative integers (given by inclusive lower bound and exclusive upper bound).

### Improvement
Can you make the algorithm efficient as to provide the answer in the range $0\ldots10^{42}$?

## Formula 1
You missed the Formula 1 live broadcasting last weekend, but after digging a while, you come up with the list of events for that race.  The players are numbered from 1 to 100 when starting and during the race a list of events happen: An event is a pilot ID followed by a type and an optional parameter. There are 3 types of events:

'O': a pilot is overtaking the one just in front of him
'R': a pilot does a box stop for refuelling and retuning his machine, the number afterwards tells after which player he re-enters the race.
'I': a pilot had an incident... and is forced to quit the race (and subsequently all pilots after this one gain one rank)

goals:

1. Determine the TOP 10 positions at the end of the race,
2. Given a participant (via his number), determine his rank (at the end of the race),


## Generating Combinations and Permutations

### Combinations

Given a set $S$ of $n$ elements.  Compute the "set" (e.g. `list`) of all length $d$ combinations of it.  Note that the goal has $n^d$ elements.  You may wish to limit the maximum size of $d$.

### Power set

Compute the power set, i.e. the "set" (maybe `list`) of all subset of $S$.  Note that the cardinality of $2^S$ is $|2^S|=2^{|S|}$, i.e. also here you may wish to limit the input size.

### Permutations

Note that the number of permutations of $n$ elements is $n!\sim \sqrt{2\pi n}(n/\mathrm{e})^n+\mathcal{O}\left(\mathrm{e}^{1/n}\right)$.  You may also with to limit the size of the input set.

### Permutations with repeating elements

Instead of a set, you are given a multiset, maybe represented as a map from some input elements to natural numbers.


## Writing out numbers

Write a function that maps a number (represented as `int`) to its text representation (e.g. as `string`).  For an easy start, you may begin with the Chinese number system.

### Chinese

<pre>
  0:零, 1一:, 2:二, 3:三, 4:四, 5:五, 6:六, 7:七, 8:八, 9:九,
  10:一十, 11:一十一, 12:一十二, ...
  20:二十, 21：二十一, ...
  30:三十, 40:四十, 50:五十, ...
  100:一百, 101：一百一, ..., 110:一百一十, 111:一百一十一, ...
  200:二百, 300:三百, ...
  1000:一千, 1001:一千一, ..., 2000:二千, 3000:三千, ...
  1'0000:一万, 1'0001: 一万一,... 1'1000:一万一千, ..., 2'0000:二万, ...
  10'0000:一十万, 10'0001:一十万一, ..., 10'0010:一十万一十, ..., 11'0000:一十一万, ...
  100'0000:一百万, ...
  1000'0000:一千万, ...
  1'0000'0000:一亿, 1'0000'0001:一亿一, ...
</pre>

### English

That is more tricky.  If you have learned the language well, you may remember the tricks:

<pre>
  0: zero, 1: one, 2: two, 3: three, 4: four, 5: five, 6: six, 7: seven, 8: eight, 9: nine, 10: ten, 11: eleven, 12: twelve, 13: thirteen, 14 fourteen:, 15: fifteen, 16: sixteen, 17: seventeen, 18: eighteen, 19: nineteen,
  20: twenty, 21: twentyone, 22: twentytwo, ...
  30: thirty, 40: fourty, 50: fifty, 60: sixty, 70: seventy, 80: eighty, 90: ninety,
  100:onehundred, 101:onehundredone, ..., 110:onehundredten, 111:onehundredeleven, ..., 120:onehundredtwenty, ...
  200:twohundred, 300:threehundred, 400:fourhundred, ...
  1000:onethousand, 1001: onethousandone, 1002: onethousandtwo, ..., 2000: twothousand, 3000: threethousand, 4000: fourthousand, ...
  10'000: tenthousand, 10'001: tenthousandone, ... 20'000: twentythousand, ...
  100'000: hundredthousand, ...
  1"000'000: one million, 1"000'001: one million one, ..., 1"001'000: one million one thousand, ...
  10"000'000: ten million, 100"000'000: hundred million,
  1'000"000'000: one billion, ...
  1"000'000"000'000: one trillion, ...
</pre>

### Khmer

This approaches a mastership:
<pre>
  0: sauny, 1: muoy, 2: pir, 3: bei, 4: buon, 5: bram, 6: bramuoy, 7: brapir, 8: brabei, 9: brabuon,
  10: db, 11: dbmuoy, 12: dbpir, ...
  20: mphei, 21: mphei muoy, ...
  30: samseb, 40: seseb, 50: haseb, 60: hokseb, 70: chetseb, 80: betseb, 90: kawseb,
  100: ry, 101: ry muoy, ..., 110:ry db, ...
  200: pir ry, 300: bei ry, ...
  1'000: pean, 1'001: pean muoy, ..., 2'000: pir pean, 3'000: bei pean, ...
  10'000: muoy meun, 10'001: muoy meun muoy, ... 20'000: pir meun, ...
  100'000: muoy sen, muoy sen muoy, ..., muoy sen db, ..., muoy sen ry, ..., muoy sen muoy pean, ..., muoy sen muoy meun, ...
  1"000'000: muoy lean, 1"000'001: muoy lean muoy, ..., 1"001'000: muoy lean muoy pean, ...
  10"000'000: db lean, 100"000'000: ry lean,
  1'000"000'000: pean lean, ...
</pre>


### German

Half of the mastership is German:
<pre>
  0: null, 1: eins, 2: zwei, 3: drei, 4: vier, 5: fünf, 6: sechs, 7: sieben, 8: acht, 9: neun, 10: zehn, 11: elf, 12: zwölf, 13: dreizehn, 14 vierzehn:, 15: fünfzehn, 16: sechzehn, 17: siebzehn, 18: achzehn, 19: neunzehn,
  20: zwanzig, 21: einundzwanzig, 22: zweiundzwanzig, ...
  30: dreißig, 40: vierzig, 50: fünfzig, 60: sechzig, 70: siebzig, 80: achzig, 90: neunzig,
  100: einhundert, 101:einhunderteins, ..., 110:einhundertzehn, 111:einhundertelf, ..., 120: einhundertzwanzig, ...
  200: zweihundert, 300: dreihundert, 400: vierhundert, ...
  1000: eintausend, 1001: eintausdeins, 1002: eintausendzwei, ..., 2000: zweitausend, 3000: dreitausend, 4000: viertausend, ...
  10'000: zehntausend, 10'001: zehntausendeins, ... 20'000: zwanzigtausend, ...
  100'000: (ein)hunderttausend, ...
  1"000'000: eine Million, 1'000'001: eine Million eins, ..., 1'001'000: eine Million eintausend, ...
  10"000'000: zehn Million, ..., 100'000'000: hundert Million, ...
  1'000"000'000: eine Milliarde, ...
  1"000'000"000'000: eine Billion, ...
</pre>

### French

The other half are French numbers:

<pre>
  0: zero, 1: un, 2: deux, 3: trois, 4: quatre, 5: cinq, 6: six, 7: sept, 8: huit, 9: neuf, 10:dix, 11: once, 12: douze, 13: treize, 14: quatorze, 15: quince, 16: seize, 17: dixsept, 18: dixhuit, 19: dixneuf,
  20: vingt, vingt-un, ...
  30: trente, 40: quarante, 50: cinquante, 60: soixante, 70: soixante-dix, 71: soixante-once, ...
  80: quatre-vingt, ..., 90: quatre-vingt-dix, 91: quatre-vingt-once, ...
  100: cent, 101: cent-un, ..., 110: cent-dix, ...
  200: deux-cent, ...
  1000: mille, 1001: mille-et-un,..., 1010: mille-dix, ..., 1100: mille-cent, ...
  2000: deux-mille, ..., 3000: trois-mille, ...
  10'000: dix-mille, ..., 100'000: cent-mille, ...
  1"000'000: un million, ..., 2"000'000: deux million,..., 10"000'000: dix million, ..., 100"000'000: cent million, ...
  1'000"000'000: un milliard, ...
  1"000'000"000'000: un billion, ...
</pre>


## Max Trade
Given a list of numbers (e.g. integers), find the 2 ordered numbers whose difference is maximal (i.e. second minus first).

Examples:

1. [1,2,3,4] -> (1,4)
2. [1,4,2,3] -> (1,4)
3. [2,3,1,4] -> (1,4)
4. [4,3,2,1] -> Ø

## Total area covered by rectangles

*Input:* A list of Rectangles represented by integer coordinates (x, y, w, h)

that may be partially overlapping

*Result:* The total area covered by the rectangles, every covered grid point only counted once

*Hint:* You could go for the inclusion-exclusion formula, i.e.

$$ A = \sum_i A_i -\sum_{i<j} A_i\cap A_j +\sum_{i<j<k} A_i\cap A_j\cap A_k -\sum\dotsm $$


## Gray code

Can you represent the numbers $0\dots2^d$ with digits '0' and '1' such that neighboring numbers differ only in one digit?

A simple solution is the [Gray code](https://en.wikipedia.org/wiki/Gray_code).  The trick is to change the positional system from

$$ n = \sum_{d_k=1} 2^k $$

to

$$ n = \sum_{d_{k_i}=1, i\ge0} (-1)^i(2^{k_i+1}-1), $$

which starts the numbers with

+---+---+----+----+-----+-----+-----+-----+------+---------+
| 0 | 1 |  2 |  3 |  4  |  5  |  6  |  7  |   8  | $\dots$ |
+===+===+====+====+=====+=====+=====+=====+======+=========+
| 0 | 1 | 11 | 10 | 110 | 111 | 101 | 100 | 1100 | \dots   |
+---+---+----+----+-----+-----+-----+-----+------+---------+

i.e. $6 = 7-1$.

### Counting

Given the above definition of Gray code, can you find the bit to flip in order to increment the number by 1?  (That is the advantage of Gray code compared to binary code when using circuits.)


## Brightest Color

Given colors in hex coding, i.e. "RRGGBB" e.g. "FF00FF", the value is defined as the maximum over the R, G or B component.  Write a command line program that reads a couple of colors from the command line (as program parameters) and return the first brightest color.

Warning:  Colors should be encoded with capital letters 0123456798ABCDEF, but sometimes could also be lowercase letters abcdef.

### HSV encoding
Find out the conversion formulas from RGB to the HSV (Hue, Saturation, Value) color space and write a program for it (without relying on the AWT/Swing/FX or MDN library):

$Cmax = \max(R, G, B)$

$Cmin = \min(R, G, B)$

$\Delta = Cmax - Cmin$

$$ \mathrm{hue} = \begin{cases}
  0         &\text{for } \Delta=0, \\
  60^o*\Big(\frac{G-B}{\Delta}\Big) &\text{for } Cmax=R', \\
  60^o*\Big(\frac{B-R}{\Delta}+2\Big) &\text{for } Cmax=G', \\
  60^o*\Big(\frac{R-G}{\Delta}+4\Big) &\text{else}  \end{cases}
$$

$$ \mathrm{sat} = \frac\Delta{Cmax} $$

$$ \mathrm{val} = Cmax $$


## Clique finding
Given a class with $N$ people everyone can have 0 or more friends.  The property of friendship is that if A is an (immediate) friend of B and B is an (immediate) friend of C, then also A is a friend of C.

Given an $N\times N$ matrix of immediate `friends` with entries 'Y' or 'N', where `friends[i][j]=='Y'` means that `i` is an immediate friend of `j`.

Write a program that computes the friendship cliques, given the matrix.

[Solution](https://www.youtube.com/watch?v=1XuCGYE56T0)

## Union Find
Given a collection, find a specific element knowing:

 a) the element by equality
 b) the element by an ordered value
 c) the min/max element (according to an ordering)
 d) What are efficient structures for repeated queries?
 e) What if we need the element closest to an ordered value from 2 collections?


## Countable Tournament

Given a list of $n$ players/teams of which each 2 have a different strength and according to strength they win against each other (no ties).  In the beginning they are arranged in random order.  A tournament is as follows:

The first two players compete against each other and the loser goes to the end of the row while the winner is sorted into the middle of the row.

Q1: How many steps are necessary to determine the best player (in worst case and in average case)?

Q2: What happens after a while?

Q3: Given the initial order (a permutation of integers) and $g$ games, can you determine who won&lost how many times?


## Sieve of Erathostenes
Find all primes between 1 and an upper bound $N$.

A prime is an integer that has exactly 4 divisors.

Examples:

0. ±1 are units, because every integer can be divided by them;
1. the smallest positive primes are 2, 3, 5, 7, 11, 13, ...

The idea of Erathostenes is as follows:

0. write down all numbers from 1 to $N$;
1. strike out 1, because it is a unit;
2. the next non-striked out number is a prime $p$, strike out all its proper multiples;
n. you may stop when you reach $p*p>N$; all non-striked out numbers are now primes.

### Polynomial sieves
Given a prime $p$, determine all monic irreducible polynomials of degree $d$ modulo $p$.

Hint: as a warm-up, you can determine all irreducible polynomials upto degree $d>0$ modulo $p$.

Hint2: What is the use of $d/2$ for factors?


## Fibonacci Numbers

The Fibonacci numbers are defined recursively as follows:

0. $f_0 = 0$,
1. $f_1 = 1$,
n. $f_{n+1} = f_n +f_{n-1}$.

### 1.A Write a recursive function
that computes the Fibonacci numbers.  Note that the numbers grow exponentially, so you may need to either use `BigInteger` or `double` for the result.

### 1.B Write an iterative function
that computes the numbers.

### 2.A Write a function that determines the Fibonacci index

of a given input.  You may either return -1 or the biggest index $n$ whose Fibonacci number $f_n$ is less or equal than the input.

Can you optimize the algorithm for large numbers? s.a. next section:

### 2.B Exponential formula

Can you find an explicit formula (like computing a fixed number of sums, products, powers) for the Fibonacci numbers?

Start with the ansatz: $f_n = a^n$

and try to find 2 different $a$s nontrivial ($a\ne0$) that solve the recursion for all $n$ ignoring the initial conditions (0) and (1).

Show that given 2 solutions $\{f_n\}$ and $\{g_n\}$, then also

$$ \{Cf_n\} $$

$$ \{f_n+g_n\} $$

are solutions (linear combinations).  What does that tell us about the general
solution of the recursion (n)?

Given the general solution for (n) with 2 free parameters, solve the linear system (0) and (1) to find an explicit formula for $\{f_n\}$.

### 2.C Approximation for large $n$
Out of the 2 summands which one can be neglected for large $n$?

What is the meaning for $n<0$?

### 3. Repeat the same for Tribonacci numbers

-1. $t_{-1}=0$,

0. $t_0=0$,

1. $t_1=1$,

n. $t_{n+1} = t_n+t_{n-1}+t_{n-2}$.


## Ackermann Function

The Ackermann function is defined recursively as follows:

0. $a(0,n) = n+1$,
1. $a(m+1,0) = a(m,1)$,
2. $a(m+1,n+1) = a(m,a(m+1,n))$

Alternate take:

1. $t(1,y) = y+2$,
2. $t(x,1) = 2$,
3. $t(x+1,y+1) = t(x,t(x+1,y))$.

### 1. Write a recursive function
that computes a particular $a(m,n)$.  The growth is $\uparrow^m$ in $m$ (way
faster than exponential).  Try it for $m=$ 0, 1, 2, 3, 4.  You may have to speed
up $a(2,n)$ in order to compute $a(4,1)$.

### 2. Can you guess the formulas for $m=$ 0,1,2,3?

As an easier exercise, you may consider the form of $t(n,y)=2\uparrow^n y$.

Can you prove them (induction)?


## Chemistry in Kotlin

You are to write a set of functions (preferably in a module) that allow to manipulate atoms into molecules.

You can start with an enum of atom(trunks), e.g. oxygene, hydrogene, carbon, nitrogene, sulfur, ferrum, aluminium, potasium, sodium, chlorine, ... and allow for

2. binary and ternary bonds (of 2 or 3 atom types only) and determine their ratio and character (unpolar, polar, ionic bond) ...;

3. organic bonds (of 3 atom types only, C, H, ...) and determine their structure, sum formula and solubility in water or oil, ... -- start with linear hydrogene carbonates, then branching (handling moities), then polycyclic;

4. complexes, i.e. consisting of a known core (e.g. ferrum-n-oxide, copper-oxide, zink-oxide, ...) and counter-ions and determine their sum-formula, ...;


## Linear Probing
Suppose you have $N$ distinct value objects and you wish to store them such that you can find an element with complexity O(1) as well as establish the structure with $\mathcal O(N)$, then a `HashSet` of capacity $\mathcal O (N)$ is most efficient.

In order to form the HashSet, you need a hashing function:

 `h(Value) -> Int` that does not have too many collisions even when modding out $p >2N$ or bigger.

### Collision avoidance
Since `h` is a function, you know that `x==y` implies `h(x)==h(y)`, i.e. you will find the specific object `x` under the address `h(x)` if it is contained.  The tricky part is to deal with the collisions.  If you know that `h(x)!=h(y)` for all pairs `x, y` in your (finite) set, then increasing the container size may help.  But in the worst case, different objects can have the same hash value (remember that the range of `Value` can be bigger than that of `Int`).  So in that case you need something like a sub-collection for every hash value (which can then either contain no, one or multiple elements).

### "Open addressing" by Linear Probing
One strategy is to just chain the collisions to the places behind the intended place, i.e. you store in something like an array (of length $p$ for some prime) and if the place $t := h(x)\%p$ is already taken, then you try $t+1$ or $t+2$, \dots until you find some free space.  Conversely, that means when searching for an element `x`, if the place $t = h(x)\%p$ does not contain `x`, but is taken, then you proceed to $t+1$, $t+1$, \dots until either you find `x` or the place is empty (which tells you that `x` is not contained).

The real trouble starts when you wish to erase `x` from the place $t+k:= h(x)\%p+k$ (even for $k=0$), because you have to check whether $t+k+1$ is occupied and whether you have to move it (or any later item) to $t+k$.  If the array is well filled, this can easily degrade the performance to $\mathcal O (N)$.

### Disjoint Chaining
An alternative is to keep lists in each place $t:=h(x)\%p$ and if there is more than one element to be kept at $t$, then you chain them up in this list.  This way you just have to operate with inserting into / removing from / searching in lists (once you have narrowed to position $t$).  This performs better even when the table is considerably full.  But that too depends on the quality of the hashing function.


## Interval calculus
Write a `class Interval` that encodes the length and whether the end points are contained of an interval of real numbers. Write a wrapper class for `set<Interval>` such that sets can contain unions of finitely many `Interval`s (and points).

### Extension 1
Add the possibility to handle unbounded intervals.

### Extension 2
Write a wrapper `class map<Interval,extended>` that represents piecewise constant functions.


## Implement the text game `SmallWorld`
(from the Ada tutorial Lovelace) in another OO language and make it dynamically reading the world from a Json file.


## Cards with doubles
Given $N$ different symbols and every playing card contains exactly $k$ different of these symbols, how many cards can you form such that every 2 cards have exactly 1 symbol in common?

Given a fixed $k$ what are the optimal numbers $N$ such that every symbol is on the same number of cards?  How many cards will it produce?

Given a fixed $k$ and $N$ a bit smaller than the above optimum.  How many cards with the required property can you produce?

What happens if for a fixed $k$ you have more than the optimal number $N$ of different symbols?

## Analyze $\sum_{n\ge1} \tan n$

Draw the first few partial sums, draw higher partial sums,

Is it bounded? Verify by increasing the search range!

Remember Alon's argument (irrational number)


## Color Reduction in Paintings

Given a painting of size $n$, i.e. an $n\times n$ square of integers which we call colors, we wish to modify it in steps with as low as possible total weight such that the result has only $r$ remaining colors (i.e. different integers).  A step consists of choosing a sub-square $1\le x_1\le x_2\le n$ and $1\le y_1\le y_2\le n$ with $y_2-y_1=x_2-x_1$ and an integer $c$, replacing all cells of the subsquare by the new color $c$.  The weight of this step is $(x_2-x_1)(y_2-y_1)$.

Hint: First focus on the total number of pixels to be changed.

### Variation
Instead of sub-squares we allow for arbitrary sub-rectangles.

### Variation 2
The penalty for small rectangles is increased, it is now $(x_2-x_1)(y_2-y_1)+1$.  What is now the optimal strategy?


## Corner Reduction

Given a painting, i.e. a matrix of size $m\times n$ filled with 0s and 1s.  A corner is a $2\times2$ square filled with 3x 1s and 1x 0 or 3x 0s and 1x 1.  An operation consists of taking any non-homogeneous $2\times2$ subsquare and make it homogeneous, i.e. with either 4x 0s or 4x 1s.

Q1:  What is the minimum number of corners you can produce in any painting?

Q2:  What is the minimum number of steps you need reduce the number of corners to that?

Q3:  Given only $s$ steps what is the minimum number of remaining corners?


## Beauty lies in the Eye on a Partition

Given a finite sequence of integers and a partition of them into $a_1,\ldots,a_m$ and $b_1,\ldots,b_n$.  The beauty of this partition is

$$ \max a_i -\min a_i +\max b_j -\min b_j $$

Q1: Given a sequence, compute its maximum beauty.

Q2:  Given a sequence, compute its maximum beauty if the part $a_1,\ldots,a_m$ must be a segment of the original sequence.

Q3:  Given a modulus $m$, we allow for partitions by remainders modulo $m$.  What is the maximum beauty of any partition (for a fixed $m$)?


## Finding a valley
Write functions that can find all valleys in an elevation profile.

### 1D
Given a list of integers, partition it into all maximal valleys, i.e. maximal convex segments.  What is your solution for overlapping segments?

### 2D
Given a grid of integers, find all valleys, i.e. maximal convex domains.  A domain must be connected, but there is no need for it to be neither convex nor simply connected.


## Ancestoral tree
Write a `class Person` that contains data of a person (e.g. *familyName*, *givenNames*, *date of birth*, *deathDay*?, \dots) and links to the nearest relatives (*spouse*, *parents*, *children*). Write a recursive xml/json parser that reads the data from a file with DTD `rels.dtd`.

_Implementation:_  Kotlin, Java, C++, Haskell, Python


## Memory
Implement a strategy for the memory card game:  $2p$ cards of $p$ pairs are mixed and put face down on the table.  2 Players take turns.  When it is your turn, you are to flip 2 cards.  When they belong to a pair, you may keep them and it is your turn again.  If they are from different pairs, you have to revert them (but everybody has seen to which pairs they belong).

The goal is to win more pairs than the opponent.  A continuous value is to pay out the difference in won pairs.

Obviously, the current game position is fixed by $(p,o)$ where there are still $p$ pairs in the game and $o$ cards were already revealed (assuming everybody remembers their faces).  The strategy needs to maximize the future value of a position, by choosing 2 consecutive moves.  Note that the optimal second move may depend on the outcome of the first move.

The trivial game is with only one pair $(1,*)$, because whoever's turn it is, they win 1 point.

The simplification rules also state that when a pair was revealed, then $p$ reduces by one and so does $o$.

The possibilities of 2 pairs has the following 3 cases:

2. $(2,2)$ then the player moving wins 2 points by playing NO, i.e. a new and then an old card;

1. $(2,1)$ then the player moving should play NO again.  In 1/3 of cases they win 2 points, in 2/3 of cases they lose 2 points.  Thus the expectation value is -2/3;

0. $(2,0)$ then the player can only play NN.  In 1/3 of cases they win 2 points, in 2/3 of cases they lose 2 points.  Thus expectation value is again -2/3.

Obviously you need to iterate over increasing number of pairs and within that over decreasing number of revealed cards.



# 1A Graph Algorithms
## 0. Representation on the computer

  - directed/ undirected, simple/ multigraph (as two template parameters)
  - operations: addEdge[s], eraseEdge[s], addNode[s], eraseNode[s], operator +/+=, operator -/-=, res (induced subgraph)

Applications:

1. Implement elementary graph algorithms, e.g.

  * partition into connected components
  * Euler path,
  * Dijkstra algorithm (distance of points from a given point),

2. graphical presentation, i.e. as raster/vector graphics.

Hint: There are at least 3 ways to represent graphs on the computer:

1. Adjacency matrix, i.e. the edges of a (directed) graph are stored in a matrix of *start* and *end* vertex
2. Adjacency lists, i.e. an array of starting vertices and every entry contains a list of edges from that vertex (e.g. by storing the end vertex)
3. Edge list, i.e. all edges in the graph are stored as pairs in a _list_

Create one common interface together with 3 implementations that each use one representation.  How would you convert between those 3?

## 1. reachable vertices
Given a start vertex `s` determine

1. whether another vertex `v` is reachable from `s` (breadth first search)
2. the set of all vertices that are reachable from `s`

Efficient solutions for the latter are the Dijkstra algorithm.  It turns out that there is no efficient algorithm that always determines 1 without computing most of 2.


## 2. Networks
A network is an edge weighted graph, i.e. a (directed/undirected) simple graph $g=(V,E)$ together with a function $w\colon E\to\bar{\mathbb{R}}$.  Design an API for (directed/undirected) networks and implement it for each of the graph implementations from above.

### Example: Implement again the shortest paths
Given a starting vertex `s` in a graph $G=(V,E)$ and

1. a target vertex `v`, determine a shortest path from `s` to `v`
2. determine shortest paths/distances to all other vertices in $V$ (Dijkstra algorithm)

The hard case are negative edge weights.  Particularly bad is if there are negative cycles, because in this case shortest can have length $-\infty$.  A solution to that is the Floyd-Marshall algorithm (which will produce a `NegativeCycleException` in this worst case).

## 3. Find Minimal Spanning Trees
i.e. a maximal tree contained in $N=(V,E,w)$, $w\ge0$ and connected such that the total length is minimal.

Compare your solution to the algorithms of Kruskal and Prim.


## 4. Solve the TSP with a greedy algorithm
Given a graph (simple, undirected, connected) with positive edge weights (that fulfill the triangle inequality), find a circle that passes every vertex and whose total length is minimal.

Compare your result to the A* algorithm.


## 5. Planarity
Given an (undirected simple) Graph, try to draw it in a GUI.

1. start with complete graphs $K_1=(\{1\},\emptyset\,)$, $K_2=(\{1,2\}, \{(1,2)\})$, $K_3=\Bigg(\{1,2,3\},{\{1,2,3\}\choose 2}\Bigg)$ (containing all possible edges), $K_4$ (4 vertices, all edges), $K_5$, ... -- Where is the limit?

2. Continue with complete bipartite graphs: $K_{2,2}=(\{1,2, A,B\},\{(1,A),(1,B), (2,A), (2,B)\})$, $K_{3,3}=(\{1,2,3, A,B,C\},\{1,2,3\}\times\{A,B,C\})$ (all edges from every vertex in the first set to every vertex in the second set), ... -- Where is the limit?

**Theorem.**  A simple (undirected) graph is plane iff it does not contain any subgraph contractible to a $K_5$ or to a $K_{3,3}$.

*Rem:* Given a graph $g=(V,E)$.  A subgraph $g'=(V',E')\subseteq g$ consists of a subset $V'\subseteq V$ of vertices and a subset $E'\subseteq E|V'$ where $E|V' = \{e\in E: start(e),end(e)\in V'\}$.  The induced subgraph is $g'=(V', E|V')$.

One possibility is to

0. separate into connected components and then for each
1. find a maximal non-touching tree (a Trémaux tree): e.g. via a depth-first search, arrange them equidistant (on a geometrical line segment/circle)
2. add the remaining edges: non-intersecting edges can be drawn on the same side, crossing edges need opposing sides, until either the CC is complete or the graph is no longer plane.


## 6. Toll system
Given an undirected graph $(V, E)$ of streets $e\in E$ that connect the residential area $v_0\in V$ with the working area $v_n\in V$ together with the time costs $w\colon E\to [0,\infty)$ of a street $e\in E$. Determine tolls $t\colon E\to [0,\infty)$ such that the cost for every (loop free) path from the residential area to the working area is the same.

Advanced: assign at most one toll per route.


## 7. Balanced Trees

* AVL,

* (2,3)-trees, Red-black trees


## 8. B+ Trees

* heavy-light decomposition


## 9. Min Cut Problem

Given a connected graph.  Determine the minimum number (and a particular solution) of edges to be removed (called cuts) such that the graph is no longer connected.

Rem.:  A graph is called $k$-connected, if you need to remove at least $k$ edges before it becomes disconnected.

Example:  The complete graph $K_k$ is $k$-connected.


## 10. Max Flow Problems

Given a network $(V,E,c)^\dagger$ where $c\colon E\to[0,\infty)$ are called capacities of the edges.  We wish to find a production configuration $p\colon E\to[0,\infty)$, $0\le p(e)\le c(e)$ such that for every vertex (except a source and a drain)

 $$ \forall v\in V\colon \sum_{e_{in}\text{ of }v} p(e_{in}) = \sum_{e_{out}\text{ of }v} p(e_{out}).$$

$^\dagger$ with neither parallel nor anti-parallel edges.

Given a single source $s\in V$ and a single drain $d\in V$, the flow value from source to drain is

$$ |p| = \sum_{e_{out}\text{ of }s} p(e_{out}) -\sum_{e_{in}\text{ of }s} p(e_{in})
 = \sum_{e_{in}\text{ of }d} p(e_{in}) -\sum_{e_{out}\text{ of }d} p(e_{out}) $$

Given any flow configuration $p$, then its free capacity is $f\colon E\to[0,\infty)$,

 $$ f(e) = c(e) -p(e)\ge0.$$

We can increase the total flow iff we find a path with positive minimal free capacity, i.e. $s\longrightarrow{P}d$, then $f(P):=\min_{e\in P} f(e)$.  Equivalently, we can increase the flow if there is a positive minimum cut capacity, i.e. $f(C):=\sum_{e\in C} f(e)$ with $C\subset E$ such that $(V,E)\setminus C$ separates $s$ from $d$.

Note that the solution may not be unique.  One way to produce a solution is to start from any flow, e.g. the 0-flow, and determine any path $s\longrightarrow{P}d$ with positive minimum free capacity $f(P)>0$ and increase the flow along this path by this $f(P)$.  Iterate over all paths until the minimum free capacity along all paths is 0.

If in addition you want to minimize the transport in the network, you can reduce the flow through all cycles $C\subset E$ by $p(C):=\min_{e\in C} p(e)\ge0$ at each edge of the cycle.


## 11. Maximum Matchings

Given a bi-partite graph $(M,E,F)$ where $V:=M\cup F$ and edges only connect $M$-vertices with $F$-vertices.  A matching $m\subset E$ is a set of edges such that every $v\in M$ and every $f\in F$ lies on at most one edge $e\in m$.  A maximal matching is one to which you cannot add edges such that it remains a matching.  The maximal matching is a matching with the maximum number of edges.

Hall's Marriage problem is a bi-partite network $(M,E,F,w)$ with $w\colon E\to[0,\infty)$ such that $w$ means the quality/happiness of that marriage.  Hall's Marriage is a matching with maximum sum weight.

[The Hungarian Algorithm](https://en.wikipedia.org/wiki/Hungarian_algorithm#The_algorithm_in_O(n3)_time) is able to find a maximal matching of minimum sum weight in a bipartite graph in $\mathcal{O}(|F|^2|M|)$ if there are at most as many females $|F|$ as there are males $|M|$.


## 12. $k$-connected components

Given any undirected graph $(V,E)$, then the $k$-connected components $CC_k$ are a partitition of $V$ such that for every $C\in CC_k$, the graph $(C,E|C)$ is $k$-connected.

Examples:

0. The regular $K_k$ is one $l$-connected component for every $l\le k$.

2. A graph of triangles that are simply connected via edges breaks up into the triangles as its $CC_2$ components.

Find a greedy algorithm that extracts a $CC_k$ component from a graph and use it to partition the whole graph into $CC_k$ components.

Q: Is that unique?

### Application: Tournament plans

Given $N$ players that play $k$-player games, what is a minimal plan such that every player has played with every other player in a game.

Hint:  Obviously, you need at least $r:=\lceil (N-1)/(k-1)\rceil$ rounds in order to reach that.  But can you actually find a partition with $r$-fold repetition of the $N$ players in $rN/k$ groups such that always $\lceil N/k\rceil$ groups can play simultaneously and nobody has to play more than $r$ games?


## 13. String Matching Algorithms

Suppose that you are given a (short) string $m\in\Sigma^*$ that you are supposed to find in a long string $s\in\Sigma^*$  How would you efficiently go over that?

1.  Suppose $s=c\in\Sigma$ is just a single character, then all you need to do is iterate over $l$ and check every position whether it coincides with $c$.

2.  Suppose now that $s=c_0:s'$ then you could first check every position whether it coincides with $c_0$ and if so, check whether the following characters coincide with $s'$.

But is that most efficient?  What if you are to produce all matching points in many strings $l_1,\ldots,l_N$?

An alternative (and better scaling) method would be to construct a matching graph as follows:

1. a DFA that starts with a vertex $s_0$ and for every character in $s$ there is an edge from the previous vertex $s_n$ to a new vertex $s_{n+1}$ annotated with the character $s$.  The last vertex is the accepting vertex $z$.

### 13.2 Simultaneous matching (Knut-Morris-Pratt algorithm)

If instead of 1 substring $s$, we are given a set $S\subset\Sigma^*$ of strings or a regular expression, and we wish to figure out whether any one of them matches, then we can construct an NFA as follows:

0. Start with a single state $s_0$ for every different first character $c$ of any $s\in S$ draw and edge from $s_0$ to a new vertex and annotate it with $c$.  If at some point a substring is complete, annotate the vertex with that substring and take it to the accepting states $Z_f\subset Z$.

1. Construct the state transition DFA as follows:  $Z_{DFA}=2^Z$ and the edges are annotated with those characters that transition every state $s\in z\subset Z$ to every other state $s'\in z'\subset Z$. The initial state is $s_{DFA}:=\{s_0\}\subset Z$.  Now parsing an input string $l$ corresponds to computations in the DFA, but it is much bigger than the previous NFA.  You may discover the reachable graph by starting from the initial state $s_{DFA}$ and only add those vertices $z\subset Z$ that are reachable by an immediate edge.

If you want to make it a tokenizer, then you need to decide how to handle the completed matches, e.g. with a longest match you output that token and continue parsing with the token-violating character.  In this case, you also have to decide how you handle mismatches, i.e. unexpected characters.

### 3. Rabin–Karp Algorithm?

### 4. Tries and Compressed Tries

The compression works mostly in C with some bit manipulations.

### 8. Prefix Trees, Suffix Trees, Suffix Automation (Ukkonen Algorithm)


# 1B Hypergraphs on the computer
## 0. Implementation
A hypergraph is a pair $h=(V,E)$ where $V$ is a finite set – the vertices – and $E\subseteq 2^V\setminus\emptyset$ are the hyper edges. Condition: with $s\in E$ also all faces $\partial s$ are in $E$.  Given any hyperedge $s=\{v_0,v_1,\dots,v_d\}$, then its faces are the hyperedges $s_k=\{v_0,v_1,\dots,\hat{v}_k,\dots,v_d\}$ (and for $d=0$ there are no such faces).

Implement the following:

  - operations: `addEdge[s]`, `eraseEdge[s]`, `eraseNode[s]`, `operator +`,`+=` (union), `-`,`-=` (difference), `cap` (intersection), `*`, `*=` (cartesian product, needs a triangulation mechanism)

## 1. Dual Hypergraph
i.e. the hyper graph $h':=(E, V')$ such that $X\in V'\subseteq 2^E$ iff there is an $x\in V$ such that for all $e\in X$: $x\in e$;

Hint: You may have to use an enumeration that maps $E$ to integers.

### 1.0 Edge graph

Given any graph $(V,E)$, then its dual graph is the graph $(E,V')$ such that $X\in V'\subset 2^E$ iff there is a $v\in V$ such that for all $e\in X$: $v\in e$.


## 2. Implement chain complexes on Hypergraphs
Find a definition of hyper-orientation.

operations: `operator` `+`,`+=`, `-`,`-=`, `cap` (intersection), `boundary` (such that `boundary`$^2=0$)


# 1C. Finite Incidence Geometries
## 2D

Def.: A finite plane incidence geometry is a triple $(P,I,L)$ consisting of finite sets $P$ (the points), $L$ (the lines) and a relation $I\subseteq P\times L$ such that the following axioms hold:

1. There are at least 3 points;
2. Through every pair of points there is exactly one line;
3. There is a line and a point not on that line;
4. To every line and a point, there is a unique parallel line (i.e. either no common points or the same line) through that point;
5. The 3 paralleles axiom.

Thm.: A finite incidence geometry is characterized by the number $n$ of points on every line.  There are in total $n^2$ points, through every point pass $(n^2-1)/(n-1)=n+1$ lines and in total there are $n^2+n$ lines.

Write a class that can hold a finite incidence geometry.  Write satellite classes that represent a line and a point.

Thm.:  Given a prime power $n=q=p^k$, then the isomorphisms are $\mathrm{Gl}_2(\mathbb{F}_q)\ltimes\mathbb{F}_q^2=:\mathrm{Aff}(\mathbb{F}_q^2)$.


## 2P Projective Geometries

Such as $\mathbb{P}^2\mathbb{R}$ or $\mathbb{S}^2\approx\mathbb{P}^1\mathbb{C}$, i.e. every 2 lines intersect in a point.

Find the analogon of the characteristic theorem and implement it in a class with points and lines as satellite classes.

Thm.: Given an odd prime power $n=q=p^k$, then the isomorphisms are $\mathrm{PSl}_2(\mathbb{F}_q)\ltimes\mathbb{P^2F}_q$.


## 2H Hyperbolic Geometries

Such as $\mathbb{H}$ the upper half-plane or Poincaré Disk $\mathbb D^2$ where to every point and line not through the point, there are many parallel lines.

Find the analogon of the characteristic theorem for regular finite hyperbolic geometries and implement it in a class with satellite classes for points and lines.

Given an odd prime power $n=q=p^k$, then the isometries of the hyperbolic plane are $\mathrm{O}_{\mathbb{F}_q}(1,2)$, the stabilizer is $\mathrm{O}_{\mathbb{F}_q}(1)\times\mathrm{O}_{\mathbb{F}_q}(2)$ and thus the space has the same cardinality as $\mathbb{F}_q^2$??

### Example $\mathrm{O}_{\mathbb{F}_3}(1,2)/\mathrm{O}_{\mathbb{F}_3}(1)\times\mathrm{O}_{\mathbb{F}_3}(2)$

$\eta^{1,2} = \mathrm{diag}(1,-1,-1)$,

+----------------------------------+------+--------------------------------------------+
| Group                            | Size | Elements                                   |
+==================================+======+============================================+
| $\mathrm{O}_{\mathbb{F}_3}(1)$   | $2$  | $\{\pm1\}\cong C_2$                        |
+----------------------------------+------+--------------------------------------------+
| $\mathrm{O}_{\mathbb{F}_3}(2)$   | $4$  | $\{\pm1\!\!1, \pm[0,1; -1,0]\} \cong C_4$  |
+----------------------------------+------+--------------------------------------------+
| $\mathrm{O}_{\mathbb{F}_3}(1,2)$ | $8$  | $\left\{\pm1\!\!1, \pm\begin{pmatrix}1&0&0\\ 0& 0&1\\ 0&-1&0\end{pmatrix}, \pm\begin{pmatrix}0&1&0\\ -1&0&0\\ 0&0&1\end{pmatrix}, \pm\begin{pmatrix}0&0&1\\ 0&1&0\\ -1&0&0\end{pmatrix}\right\} \cong OQ$ |
+----------------------------------+------+--------------------------------------------+


## 3D

Def.: A finite spatial incidence geometry consists of a pentuple $(P,I_1,L, I_2, E)$ where $P$ (points), $L$ (lines) and $E$ (planes) are finite sets and $I_1\subset P\times L$, $I_2\subset L\times E$ are the incidences such that

1. There are at least 4 skewed points;
2. Through every triple of non-collinear points there is exactly one plane;
3. For every plane, the points and lines incident with it form a plane incidence geometry;
4. Two intersecting lines have exactly one common incident plane;
5. For every pair of a plane and a point there is exactly one parallel (i.e. either disjoint or identical) plane through the point;
6. ?

Thm.:  The finite spatial incidence geometries are characterized by the number $n$ of points on every line.  There are $n^3$ points in total.  Through every point there are $(n^3-1)/(n-1)=n^2+n+1$ lines, through every line there are $(n^2-n)/(n-1)=n$ planes, the planes behave as in 2D.  There are $n^3$ planes in total.

Write a class that represents a finite spatial incidence geometry and satellite classes that represent points, lines and planes in this.

## 3P, 3H

Define the analoga of projective and hyperbolic geometries.  How can they be characterized in the finite case?


# 2 Algebra
## 2.1 Categories on the computer
0. Define a `class Cat` with

* the `name`
* typedefs to the `Object` and `Morphism` satelite classes
* a method that returns the `id`entity morphism of an `Object`.

1. Instead of a `class Object` implement an `abstract class Term<Object>` that represents a Term with value an `Object` and has the concrete subtype: `Variable`.
   A `Term` should have an assigned `Cat`egory.

2. Instead of a `class Morphism` implement an `abstract class Term<Morphism>` with concrete subtypes `Variable` and `Product`.
* A `Morphism` has assigned `source` and `target`.
* Implement the `operator *` according to the rules of composition of morphisms in `Cat`egories.
* Extend `Term<...>` such that one can add expressions for source, target, and identity.

### Extension 2-Categories
A `class Cat2` with

* `Object`s,
* `Morphism`s as above, and
* `TwoMorphism`s analog to `Morphism`s.


## 2.2 Warring's Problem ($n=a^d+b^d+\dotsm+z^d$)
Find out whether the lowest positive integers can be written as sums of $g$, $d$-th powers.

For example Lagrange's 4 Squares Theorem states that for $d=2$, $g(2)\le4$.  On the other hand for $n=7$ we see that $g=1,2,3$ are not sufficient.

### (3,3) Sum of Three Cubes ($n=a^3+b^3+c^3$)
Write a program that finds a solution of the [equation](https://en.wikipedia.org/wiki/Sums_of_three_cubes)
  $$ n = a^3 + b^3 + c^3 $$
in integers $\mathbb{Z}$ for given $n$.  The 3 is crucial here, i.e. starting from $a=(3a'+s)$ where $s\in\{-1,0,1\}$, we can show that $a^3\equiv s \pmod{9}$.  Therefore there is only a solution for $n\not\equiv \pm4 \pmod{9}$.

Starting from these observations, you may run through all suitable pairs $(a,b)$ and see if the real solution $c$ fits as an integer.  You may need a function `icbrt` that computes the approximate cube root of a (large) integer.

*Implementation:* should be fast, thus use e.g. C++, Kotlin native or Rust.

See if you can produce most solutions for $n=0,\dots,32$ in moderate time.

### (3,4) Sum of Four Cubes ($n=a^3+b^3+c^3+e^3$)
Repeat the same with four cubes.

### (4,5) Sum of 5 Bisquares ($n=v^4+w^4+x^4+y^4+z^4$)
Repeat the same with 5 bisquares.

The current conjecture is that

 $$ g(d) = 2^d +\lfloor(3/2)^d\rfloor -2. $$


## 2.3 Diophantine Equations
### 1. Linear Diophantine Equations

Write Gaussian eliminations over the integers and implement a solver for linear Diophantine Equation systems.  You may start with a single equation and consider it modulo various remainders.  Then compose via the Chinese remainder Theorem.

### 2.12 Quadratic Diophantine Equations

Example: $$X+Y=XY$$

or in homogeneous form: $(X+Y)Z=XY$ which is a compact hyper surface in $\mathbb{P}^2$.

### 3.13 Cubic Diophantine Equations

Example: $$X+Y+Z=XYZ$$

or in homogenization $(X+Y+Z)T^2=XYZ$ for rational numbers $X,Y,Z,T$ not all vanishing simultaneously.  Thus the solution is a compact hyper surface in $\mathbb{P}^3$.


## 2.4 Implement mathematical expressions on the computer
i.e. good enough for a CAS (Computer Algebra System). Start with an `abstract class Term` that encodes the type of the term and has abstract (i.e. purely virtual) methods `show`/ `toString`, `evalf`, `subst`, `compare`, `pgroup`, `sgroup`. Have five concrete implementations:

#. `class Constant` that encodes real (complex), rational, and named constants. A constant can be `evalf`-ed and the usual operations for a field are defined on constants giving a real constant if one of the components is a real constant. Constants are compared/ grouped by the above hierarchy and constants of the same type by their natural order.

#. `class Variable` that encodes a real (complex) named variable. Variables are `compare`d/ `group`ed by their `name`.

#. `FCall` a `Function` call consisting of a *pointer* to a real function implementation, the function `name`, and the `argument` of type `Term`.  `FCall`s are `compare`d/ `group`ed first by their function `name`, and then by their `argument`. One particular function is the `power` function (with two arguments).

#. `Product` contains products of two or more factors. The factors are sorted by the `pgroup` comparing method. `Term`s that are `pgroup`ed together should be simplified to one factor (such as rational / real constants or powers of a variable / named constant).

Implement an `operator * (Term t2)` in the class `Term` that handles multiplication correspondingly and generates `Product`s iff necessary.


#. `Sum` contains sums of two or more summands. The summands are sorted by the `sgroup` comparing method. `Term`s that are `sgroup`ed together should be simplified to one summand (such as rational / real constants or multiples of one monomial).

Implement an `operator +(Term t2)` in the class `Term` that handles addition correspondingly and generates a `Sum` iff necessary.

#. Modify the class `map<Interval,Term>` such that it can encode piecewise defined functions.

#. Write simplification, such that expressions, e.g. `Var("x")+Const(0)`, `Var("x")+Var("x")`, `Var("x")*Var("x")`, ... are simplified accordingly.

### Extension to vector-valued terms
Write an `abstract template class Termn` that takes the dimension $n$ as parameter. Implement concrete subclasses:

#. `Tuple` that takes $n$ `Term`s,
#. `Funn1` that takes a `Termn`, an integer $0\ldots(n-1)$ and is a `Term`.


If you implement in C++, write a wrapper class for `Term`s that handles the subtype change in the argument under assignment and frees the application programmer from handling pointers.

### Application: integration of linear ODEs (with constant coefficients)

### Application: Laplace transform.

### Application: Draw implicit functions
You could start with polynomial curves in 2 variables: $0 = F(x,y)$

1. An elegant way is if you find a parametrization $I\to\mathbb{R}:t\mapsto (x,y)$.

0. A brute-force solution is to iterate over *all* $x\in\mathbb{R}$ and for each to solve the equation for $y\in\mathbb{R}$.


## 2.5 [Finite groups](https://en.wikipedia.org/wiki/group_(mathematics)) on the Computer
Start by implementing

1. cyclic groups
2. fin.gen. abelian groups
3. Permutation groups
4. semi-direct products

The implementations of groups should have:

* standard constructor ($C_n=\mathbb{Z}/(n)$, $S_n$),
* neutral, inverse element,
* Group operation, inverse element
* simple group comparison, element comparison,
* `String` (Input and) output,

_Implementation:_ an abstract `class Group` and a `template <...> class Element` whose implementation depends on the group.

_Languages:_ C++, Kotlin, Scala, Haskell, Java, Ruby

### 2.5.1 Direct products

The direct product of 2 (non-necessarily abelian) groups is the set of pairs under component-wise multiplication.  In particular, the components commute.

### 2.5.2 Semi-direct Products

A semi-direct product consists of pairs of elements, but the multiplication of the right elements is twised by an (outer) automorphism parametrized by the first left element, i.e.

 $$ G{}_\rho\ltimes H:= G\times H, (A,v)\circ(B,w) = (AB,v+\rho(A)w) $$

 The inverse elements are correspondingly $(A,v)^{-1}=(A^{-1}, \rho(A^{-1})v^{-1})$.


## 2.6 [Rings](https://en.wikipedia.org/wiki/ring_(mathematics)) on the Computer
Start with implementing

### finite cyclic rings $\mathbb{Z}/(n)$
that

* hold $n$

Together with a `template<...> class Element` that holds particular elements and has

* `operator`s: `=`,`==`,`+`,`+=`, (unary)`-`, (binary)`-`, `-=`, `++`, `--`, `*(int)`, `*=(int)`, (binary)`*`, `*=`

### Polynomials
as `template class`, parameter is the base ring for the coefficients.


## 2.7 Finite dimensional [Fields](https://en.wikipedia.org/wiki/Field_of_fractions)

#. Implement the `template class Rational` which has the Euclidean domain as a parameter.

#. Continue with extension by algebraic elements.

#. Implement an irreducibility test.

_Implementation languages_: Kotlin, Scala, C++, Haskell, Java, Ruby


## 2.8 Primality Testing

Def.:  Given an integer $n$, it is a prime iff it has exactly 4 factors, $\pm1$ and $\pm n$.  The element $0$ is a 0-divisor, the elements $\pm1$ are called the units.

Example: The first positive primes are $2,3,5,7,11,13,17,19,\ldots$ which you can produce with Eratosthenes sieve algorithm.

For some applications, e.g. RSA you need however large primes, such that the factorization of a product $p=p_1p_2$ is virtually impossible.

What you do is start with a candidate $p_1 = p_{i_1}\dots p_{i_n}+1$ and need to check whether it is a prime.  The reason is that if it is not a prime, your algorithm for computing $\phi(p)$ fails and the factorization of the final $p$ becomes simpler.

### 0. brute force testing

The simplest test is to produce the first $n$ primes and to check that $p_1$ is not divisible by any of those.  If indeed you checked upto $p_n\ge\sqrt{p_1}$, then you know that $p_1$ is a prime.

### 1. Pollard-rho test

This is not a deterministic test, e.g. it can fail despite the number not being a prime.  But given the repetition of the test, then the probability of your number being prime is bigger and bigger.

### other tests

## 2.9 Algebraic Approximation

### 1. Rational Approximation
Remember the algorithm to approximate some dyadic number (finite binary fraction) by a rational number with small numerator and denominator.  The core is the following chain fraction expansion:

**Given:** the value $x$ and error $\epsilon$

1. compute: $a_0 = \lfloor x\rfloor$
2. while $\epsilon<1$

    a. $r\leftarrow x-a_k$
    b. if ( $r<\epsilon$ )  stop
    c. $x\leftarrow 1/r$, $\epsilon\, *\!= x*x$
    d. $a_{k+1}\leftarrow \lfloor x\rfloor$

**Output:** the best approximation for the given precision is $x\approx [a_0; a_1,a_2, ...]$ as a continued chain fraction.


### 2. Lattice approximation

According to Alon Amit, the analogon for an algebraic approximation of a dyadic number can be achieved as follows:  You need to choose a large integer $M$, e.g. $\mathrm{gcd}_{k=2}^n k$ and a maximum degree $D$.  Then you form the lattice vectors

 $$\begin{array}{cccccrr}
   v_0 &= (1,&0,&0,&\ldots,&0,&M),\\
   v_1 &= (0,&1,&0,&\ldots,&0,&\lfloor Mx\rfloor),\\
       &\vdots \\
  v_D &= (0,&0,&0,&\ldots,&0,1,&\lfloor Mx^D\rfloor).
  \end{array}$$

And use the [Lattice reduction algorithm](https://en.wikipedia.org/wiki/Lenstra%E2%80%93Lenstra%E2%80%93Lov%C3%A1sz_lattice_basis_reduction_algorithm) on them to find a minimal vector in this lattice.  If the minimal vector is the combination $v_m=a_0v_0+\dotsm+a_Dv^D$, then $x$ is an (approximate) root of the polynomial $p(x)=a_0+a_1x+\dotsm+a_Dx^D$.  If $x$ can fulfill a simple lower degree polynomial, then the higher coefficients will be 0.

Limits:  Note that if $x$ has an absolute error of $\epsilon$, then you need to make sure that $M\max(1, |x|^{D-1}D)\epsilon<1$ in order for the approximation to be well-defined.  It is better to have some preliminary information on the maximum degree $D$ of the polynomial.  If the minimum lattice vector is properly determined, then the polynomial should be a minimal polynomial.



# 3. Optimization and large-scale Combinatorial problems I
## 3.-1 Fabergée eggs

Given a set of `n` Fabergée eggs, you wish to find out from wich maximum height in a tower you can drop them such that they do not break.  Even giving only 1 egg, you could start dropping it from the 1F, then 2F, then 3F and so on, until it breaks, which would then tell you the maximum height.

Given however the second constraint that you may throw eggs at most `m` times in total, what is the maximum height of a skyscraper for which you can find out the maximum dropping height for the eggs?

Variant 1:  Given the number of eggs and the maximum number of drops, what is the maximum height of the skyscraper?

Variant 2: Given the number of eggs and the height of the skyscraper, what is the minimum number of drops that is guaranteed to solve the question?

Approach 1:  Of course, you can do that with dynamic programming and it solves the problem in reasonable time for moderate input (6531 eggs and 11500 trials in 140s).

Approach 2: Can you analyze the results and optimize the solution such that it solves in faster time (say the same 6531 eggs and 11500 trials in 2s)?  You may no longer want to use dynamic programming from 0, 0.


## 3.0 CS Roulette
Given $n$ players who stand in a circle (such that the situation looks exactly the same for every player) and every *s* seconds every living player shoots at some other living player.  Who is dead immediately leaves the game (and the circle is reduced in size).  Then after a while, there are 2 possibilities: either there is exactly 1 player left or there is no player left.  We call $p_1(n)$ the probability that there is exactly 1 surviving player when starting with $n$ players.

Estimate the probabilities $p_1(n)$ for small $n$ with moderate accuracy, i.e. starting from the trivial solutions $p_1(0)=0$, $p_1(1)=1$, $p_1(2)=0$ increase $n$ and for each $n$ run the same $N$ times each time only one round.  If there are $n_1$ players surviving after the first round, fill in $p_1(n_1)$ for this round.

To observe convergence quality, you may check the ratio $\langle n_1\rangle/n$ which should converge to $1/$e.

Compare with the expected result
$$ \lim_{x\to\infty} |p_1(\exp(x))-f(x)| = 0 $$
for some strictly periodical function $f\colon\mathbb{R}\to[0,1]$.

[BKM17] T.v.d. Brug, W. Kager, R. Meester: *The Asymptotics of Group Russian Roulette*, in Markov Processes and Related Fields, vol. 23, pp.35-66 **(2017)** [arXiv:mathPR/1507.03805](https://arxiv.org/pdf/1507.03805.pdf).

## 1. Tower of Hanoi
The actual problem was first stated and popularized by É. Lucas in 1884, but it reads nicer in the following way:  According to legend there are three pegs in the dungeon of some Brahmin temple (in Hanoi, Vietnam). They were created with 64 golden disks of decreasing size on the first peg at the beginning of the universe. The monks have to move them by the rules

* only one disk per second,
* Never a bigger disk on a smaller one,
* The goal is to move all disks from the first peg to another one.

When the task is finished the world will end.  The question is now how long does it take?

It turns out that there is a *recursive algorithm* by first moving $n-1$ disks to the third peg, then moving the $n$th disk, and again moving the $n-1$ disks. Thus it takes $2^n-1$ moves for $n$ disks, i.e. 18'446'744'073'709'551'615 seconds (585 billion years) to move the 64 disks.

Actually, there is also an *iterative algorithm* consisting of moving the smallest disk every second move and in the other move to put the smaller disk on the bigger one.

### 4. Variation (Reve's puzzle)
Use 4 instead of 3 pegs. You may assume that the *Frame-Stewart algorithm* gives the fastest solution, i.e. move $n-k$ disks using all 4 pegs to one auxiliary peg, then move the remaining $k$ disks using the other auxiliary peg to the target peg, and finally move the smallest $n-k$ disks using all 4 pegs to the target peg. You can easily guess a formula for the number $k(n)$ by looking at the optimal choices for small numbers of disks.

### 5. Higher number of Pegs
Again assume that the *Frame-Stewart algorithm* gives the optimal solution, i.e. move $n-k_t$ disks using all $t$ pegs to one auxiliary peg, then move the remaining $k_t$ disks us the remaining auxiliary pegs to the target peg, and finally move again the smallest $n-k_t$ disks using all $t$ pegs to the target peg.  Again try to guess a formula for the number $k_t(n)$ by looking at the optimal choices for small numbers of disks.

-----

More problems under [International College Programming Contest](https://codeforces.com/problemset/), e.g.


## 2. Repeated Multiplication

Suppose you want to compute the higher power $b^e\pmod{n}$ with natural exponent $e$ with only elementary multiplications.  What is the most efficient way to do that?

### 2.1 quickPower algorithm

You could start writing the exponent $e$ as a sum of powers of 2 and then start from $f=b$ and keep squaring $f$ until the highest power of 2 in $e$ is reached.  Then you would multiply all those powers of $b$ that make up $e$.

### 2.2 But is that most efficient?

You could do an optimization, i.e. starting from $b^0\equiv1 \pmod{n}$ and $b^1\equiv b\pmod{n}$ you start finding greedily a shortest sequence of powers that are built one after the other and lead up to $b^e\pmod{n}$.  Obviously $b^2\equiv b\cdot b \pmod{n}$, but already for $e=5$ there are multiple ways and you need to somehow canonize the results.

Effectively you would hash the last step for reaching $b^e\pmod{n}$ and in order to find the full sequence, you would descend down to $b^1\equiv b\pmod{n}$.

When you optimize, you will have to bootstrap from 1 up to $e$.

Q: What is the first exponent $e$ where the greedily single shortest path is shorter than the quickPower path?

### 2.3 Even better

Instead of picking arbitrarily one path up to $e$, you try to find all shortest paths up to $e$ and after the bootstrap, you find the shortest sequence from $1$ to $e$ by optimizing over all products that lead to $b^e\pmod{n}$.  The reduction comes when merging the paths for $s_1+s_2=e$, i.e. considering them as sets and computing their union.  This way it may sometimes be more efficient to use another path up to $s_1$ if that path overlaps with a path up to $s_2$.

Q: What is an exponent whose intermediately optimized shortest path is shorter than the greedily canonized shortest path?

### 2.4 Full optimization

It could be even more efficient not to use a shortest path up to $s_1$ in $e=s_1+s_2$, but that path that overlaps the most with any path up to $s_2$.  Thus it is not clear that such a path passes through an optimal path up to $s_1$ (or $s_2$).  In order to avoid searching for too long, you can prune these paths once you have found any one path by that path's length.

Q: What is an exponent whose fully optimized shortest path is shorter than any intermediately optimized shortest path?

Q2:  How much is the gain compared to the intermediate optimization?  Compared to the canonized greedy optimization?  Compared to the quickPower?

## 3. Chocolate problem
Given a $w\times h$ bar of chocolate and $n$ friends that desire $s_i$ pieces of chocolate ($i=1,\dots,n$). Determine whether it is possible (and how) to break[^1] the chocolate bar into these pieces without remainder and each piece entire.

0. The target pieces obviously have to be convex thus are rectangles.
1. The obvious constraint is $\sum_i s_i = w h$, but also
2. For every piece there must be positive integers $a_i,b_i$ such that $a_ib_i=s_i$ and $a_i\le w$, $b_i\le h$.
3. 1: breaking means a partition along a horizontal or vertical line through one whole piece.

In a back-tracking search algorithm try to find a splitting of the bar such that the biggest piece fits into the first part and the other pieces can be split evenly. Then work recursively from there until all pieces are fit.


## 4. Black-white Tiling
Given an $n\times n$ grid, we wish to find all possibilities of shading the cells black or white such that there is no $m\times m$ square ($m<n$, fixed) that is completely single-colored.

### 3 Warm up
#### a) Find all possibilities for $n=3$ and $m=2$
That do not have any white $m\times m$ square (so a black $m\times m$ square is Ok).

#### b) Given that the top-left cell is black
How many possibilities are there?

#### c) Given that the mid-left cell is white

#### d) Now implement symmetric in black and white

### 4 Expand
Do the same 4 parts on a $4\times4$ grid, $m=2$ and $m=3$, respectively.

#### e) Given that the center cell is black

### 5 Generalize
Do the same for arbitrary $n\ge3$ and fixed $1<m<n$.

#### f) What about 3 Colors (e.g. +red)

## 5. Write a Server and strategies for the Iterated Prisoner dilemma

### 5.2 Prisoner Dilemma

Two prisoners sit together in a cell/before the judge.  They are each simultaneously offered to either blame the other and if successfull get 5 years earlier released, they can stick together and get released 3 years earlier for lack of evidence or if they both betray each other get no early release due to conviction of the crime.  Unfortunately they may not communicate with each other, except that they will see the other's choice after the round.

The game repeats every day and the question is what is a good strategy to spend as little time in prison as possible.

### 5.n Iterated Prisoner Dilemma

Instead of just 2 prisoners, there are $n$ prisoners which play in all possible pairs for $t$ turns each game and in the end the prisonser with the highest reduction of prison time wins.

1. Write a server that can be handed `n` prisoners and then plays the tournament and shows the results;

2. Write some prisoners, e.g. `Good` who always cooperates, `Evil` who always tricks, `Pattern(p :List<Choice>)` who plays a pattern of Choices repeatedly, `Random(bias :Double)` who flips a coin with `0≤bias≤1`, `Adjusting` who is a `Random` prisoner starting out with fair `bias=0.5` but for every trick lowers its `bias` and for every cooperation rises its `bias`, a `TitForTat` that starts out collaborating and then always does what the opponent did in the last move, ...

3. Observe the winning chances/ranking in different social environments, e.g. `Evil` among only `TitForTat`, `Evil` among `Good`s, `TitForTat` amoung `Adjusters` and the like.

### 5.q Option to Quit for Good

It is certainly frustrating to play against `Evil`.  Therefore the `Prisoner`s are at each step also given the option to `quit` which means that for this and every remaining step they will each get 2 points.  This way you can make more than 0 points against `Evil`, but may lose against `TitForTat` if you just always quit.

1. Some new strategies are `Brutal` who plays trick until he is tricked when he quits, `TitForTatBilanced` who plays `TitForTat` until the bilance drops below 2/round when he quits, ...


## 6. Implement a Player Strategy for the *Groups* Game
See [*Groups* Game](#groups-game) for its definition (cards with 4 features, a group are 3 cards that do not split in 2 by any 1 feature) and implement an automated player that receives cards as they pop up and finds any set contained.  Check that your algorithm works in real time, i.e. assuming that a new card pops up every 10s it can identify a new arising *Group* latest 1s after it pops up.

If you implemented a server in the easy part, you may now add an automatic player that identifies *Groups* with a random delay, Poisson distributed with $\mathbb{E} t = 4$s (i.e. sometimes *Groups* are overlooked and notified in the next pop up).

Consider making the game a client server application (where players can subscribe to one of 4 tables and are kicked out after 20s without heartbeat of their client).


## 7. Implement an App and a Strategy for the Reduction Game
This is a 1 player game, but you can compete with multiple players by playing in parallel.

0. Start with an integer ≥2
1. In every step you can
  a. decrease the number by 1, penalty 1
  b. increase the number by 1, penalty 1
  c. divide the number by a factor at most its square root.
9. The game ends when you reached 2.
10. The scoring is negative by the amount of penalty.

### Step 1: Implement an app that generates a random integer > 2 and lets you choose moves.

### Step 2: Write a dynamic programming solution that produces the valuations and optimal moves for an input number.

Hints:

0. There are infinitely many numbers of penalty 0, e.g. $2^n$;
1. There are infinitely many numbers of penalty 1, e.g. $3^n$;
2. In the asymototic limit, almost all integers have penalty 2;
3. Between neighboring integers, the penalty can change by at most 1;
4. The first integer of penalty 4 is 3'976'733;
5. An integer of penalty 5 is expected at $\sim10^{18}$.

### Step 3: Visualize the growing tree of 0-penalty numbers

Hint: reverse the computation w/o penalty, i.e.

1. starting from 2 multiply with primes that are less than the current number;
2. make the tree contain every number only once: starting from 2, you pass on paths on non-decreasing factors.


## 8. Logic Puzzles
### 8.1 Skyscrapers/Wardrobes

In a grid of 7 by 7 squares you want to place a skyscraper in each square with only some clues:

* The height of the skyscrapers is between 1 and 7
* No two skyscrapers in a row or column may have the same number of floors
* A clue is the number of skyscrapers that you can see in a row or column from the outside
* Higher skyscrapers block the view of lower skyscrapers located behind them

Can you write a program that can solve this puzzle in time?

This kata is based on 4 By 4 Skyscrapers and 6 By 6 Skyscrapers by FrankK. By now, examples should be superfluous; you should really solve Frank's kata first, and then probably optimise some more. A naive solution that solved a 4×4 puzzle within 12 seconds might need time somewhere beyond the Heat Death of the Universe for this size.

### 8.2 Ghosts, Vampires and Mirrors

### 8.3 Camping Sites


## 9. AI bot for playing pacman

Given an implementation of [Pacman](#grazing-maze) with an API (point 4), implement a strategy that explores the unknown grid and collects as many fruits as possible, avoiding collisions with evil bots.

### 9.1 Hoarding

Write an AI bot for a hamster that walks through a maze and collects randomly scattered grains and hoards them at its nest.  The constraints are to only move along the paths in the maze and with an upper bound on the speed.  You get bonus points for every hoarded grain, but a negative point for every step taken.  The cheeks of the hamster have finite capacity, so you need to either spit them out or return to the home from time to time.


## 10. Puzzling Computer
Write an algorithm that finds a solution of a puzzle.  You may assume that the total shape is a rectangle and the parts have "tetris look", i.e. simply connected groups of squares.

## 11. Vector Race

### 11.1 The GUI

A vector race happens on a grid with a drawn racing loop.  All players start at the starting line, next to each other in a fixed order.  In every turn you move your car forward by the last motion plus/minus one step in each direction.  If you hit / exceed the track or if you hit another player, you are deccelerated to speed 0 and have to accelerate again in the next round.  The winner is who passes the goal line in a minimal number of steps.

Make it an interactive game for 2 players (input either via mouse or via keyboard).

### 11.2 DP solution

Split away a server from the game and implement some automatic player:

Find the optimal next step, such that you do not leave the track (and do not hit your opponent).  Increase the recursions such that you don't leave the track in the next 2, 3, ... steps.


## 99 More
For more Problems have a look at [Project Euler](https://projecteuler.net/archives).



# 4. Finite Automata

## 1. Finite State Machine (DFA)
Implement a generic finite state machine, i.e. a finite set $S$ of states together with an initial state $s_0\in S$, a transition function $\delta\colon C\times S\to S$ where for every $s\in S$ the set $C_s$ is finite and called the progression parameters.  A state path is a sequence $(C\times S)^*$ such that $z_0$ is the initial state, $c_k\in C_k$ and $s_{k+1}=\delta(s_k,c_k)$.

### 1.2 Can you augment to an non-Deterministic Finite Automaton (NFA)
The difference is a transition relation $\Delta\colon C\times S\to [0,1]^S$ with the transition rules $S_0=\{s_0:1\}$ and $S_{k+1} = \bigcup_{s\in S_k} \Delta(s)$ with the corresponding probabilities, i.e. the state at any moment in time consists of a probability distribution over all possible atomic states (with the constraint that the total probability adds up to 1).

## 2. [Turing machine](https://en.wikipedia.org/wiki/Turing_machine) on the computer
A Turing machine is a set $Z$ of states containing $z_0$ the initial state (and $z_f$ the final state), a set $\Sigma$ of letters, a transition function $\delta\colon Z\times\Sigma \to Z\times\Sigma\times\{ L,H,R \}$, and a two-sided growable $\mathbb{Z}$-sequence $b$ of letters from $\Sigma$. A step in computation consists of reading the letter $b_i$ (start with $i=0$) and mapping it together with the state $z_i\in Z$ to the new state $z_{i+1}$, new letter at $b_i$ and the shift operation depending on the third argument of δ moving the sequence $b$ left, not, or right (translation).

A computation by a Turing machine is the transformation of an initial band b with the initial state $z_0$ to the final state $z_f$ or into a 1-step loop and the output the final tape $b'$.


## 3. [Core War](https://en.wikipedia.org/wiki/Core_War)
A simple assembler interpreter that permits to run threads and spread in the simulated memory. The competition runs two (or more) threads interlaced and the threads try to occupy memory (by writing into it). The surviving (or more dominant) thread wins.

### possible Memory Structure:
```kotlin
  data class Word(var op :Opcode, var src :unsigned, var dst :unsigned)
```

The machine needs only an instruction pointer per thread and a list of threads per user.

### possible Assembler Instructions:

  - `mov` to from registers, memory (data, relative, indexed, indirect)
  - `jmp`, `jCond, jnCond`, `call` for jumping to another part (`call` stores the return address on the stack, Cond is a flag).
  - `add, sub, cmp` elementary operations with the register and memory
  - `fork` and `stop` for multi threading. The interpreter serializes all threads of one process and runs the processes strictly interlaced.

_Implementation:_ Kotlin, C++, Haskell or Rust, Java



# 5A Fractal Geometry
## 1. Mandelbrot
The Mandelbrot is the set of all points $c\in\mathbb{C}$ of the complex plane for which the sequence:
 $$z_0=0,\quad z_{n+1} = z_n^2+c$$
  remains bounded.

Optimization:

* It is sufficient to test (for a fixed number of iterations) whether the $z_n$ remain within a disk of radius 2. Don't use the square root.
* The iteration at which the $z_n$ leave the disk can be translated into a color.

###  You can also draw Julia sets, i.e. points
$z_0\in\mathbb{C}$ for which the sequence remains bounded. The Julia set depends on the parameter $c\in\mathbb{C}$.

## 2. Śierpinski Triangle and Carpet
### 2.1 Cantor's Mid-third Set
This is the interval $[0,1]$ except for the middle third, and then repeat the same procedure for each of the remaining intervals.

### 2.S Devil's Staircase
This is Cantor's set stretched in 2D, i.e. instead of drawing the set on a line, we use it as a description for a monotone function:  We start with the identity function on the unit interval $1\colon:[0,1]\to[0,1]$.  Then we remove the middle third piece and replace it with a constant function
 $$ f_1(x) = \begin{cases} {}^3/_2x &\text{for }0\le x<{}^1/_3, \\
   {}^1/_2 &\text{for }{}^1/_3\le x\le{}^2/_3, \\
   {}^3/_2x-{}^1/_2 &\text{for } {}^2/_3<x\le1.  \end{cases}
 $$
Then we repeat the procedure on every interval where the function is not yet constant.

The result is a function that is almost everywhere constant but has a net increment of 1 on the unit interval.

### 2.T Śierpinski Triangle
The triangle can be computed by the first $2^n$ rows of Pascal's triangle modulo 2, i.e. start from a single $1$ on top and in every following row place numbers between the columns from the previous row as the sum of the 2 neighboring values, padding with 0s on the boundaries.

You can display that in a Swing window or HTML5-Canvas by first estimating the window resolution and then taking the nearest power of 2 and computing Pascal's triangle modulo 2. (You only need to buffer 2 rows for that).

### 2.2 Śierpinski Carpet
Starting from a Square, remove the middle 1/3 square and repeat the procedure with all remaining 8, 1/3 squares.

Again you can start by filling the window with a square and then iterating down to the pixel size.


## 3. Iterated function systems

### 1. Encode an elementary class of transformations of $\mathbb{R}^d$
via parameters and pick $n$ $(f_1, \dots, f_n)$ of them (contracting or only weakly stretching).

### 2. Try to compute the nontrivial set:
  $$Z\subset \mathbb{R}^d : f_1(Z)\cup\dots\cup f_n(Z) = Z$$

You can use the following strategy to approximate the set:

#. Construct a probability measure $p$ on $\{1,2,\dots,n\}$ that picks a transformation $f_i$ with a probability proportional to the volume stretching/ shrinking.
#. Given a point $z_0\in Z$ you can construct a dense sequence of points in $Z$ by the stochastic iteration $z_{k+1} = f_{i_k}(z_k)$ where the $i_k$ are independently identically distributed random variables with the probability $p$.
#. You can find an approximation of a first point by picking $z_{-\infty}\in\mathbb{R}^d$ and iterating from there.
#. Not all IFS with weakly stretching maps are convergent, thus you should also check for the first points from $z_k$ whether they lie within *finite* volume.
#. Do it in particular for affine transformations of $\mathbb{R}^d$ for $d=2,3$.

99. $\mathbb C\supset X = \bigcup_{k\in\mathbb Z} \log_{(k)} X$ which you approximate by
  $z_{n+1} = \log_{(0)} (z_n+2\pi k_n\mathsf{i})$, $k_n \sim p_{\mathbb Z}\approx N(0,\sigma^2)$ and $p_{\mathbb Z}$ means a random distribution on integers that has a (finite) second moment when read as a continuous real random variable. $p_{\mathbb Z}\approx N(0,\sigma^2)$ means that we compute the integral random variable as the integer approximation of a continuous normal distributed random variable with expectation value 0 and variance $\sigma^2>0$.  The higher $\sigma$, the better the approximation to a uniform distribution on $\mathbb Z$, but also the slower the convergence of the pointed approximation of the figure.


## 4. Zeros of Littlewood polynomials.
A Littlewood polynomial is a polynomial $p(z) = 1 \pm z \pm z^2 \pm \dotsm \pm z^d$. I.e. there are $2^d$ Littlewood polynomials of degree $d>0$. It is easy to show that all roots of Littlewood polynomials are within $1/2\le|z|\le2$. The interesting thing is that the set $D\subset\mathbb{C}$ of zeros of all Littlewood polynomials is a fractal. For implementation:

#. Write a function that finds a complex zero of a polynomial $p\in\mathbb{C}[z]$.

   Hint: You can use a line search algorithm: $z_{n+1} = z_n +\lambda_\infty \nabla|p|(z_n)$ where you choose $\lambda_0=-1$ and improve with the Newton iteration for derivative of the function $\phi(\lambda) := p(z+\lambda\nabla|p|(z))$, i.e. $\lambda_{n+1} = \lambda_n -\phi'(\lambda_n)/\phi''(\lambda_n)$.

#. wrap with a function such that it finds all zeros within a certain circle.

   Hint: Given a complex root (with non-zero imaginary part), then also its conjugate is a root (because all coefficients are real), thus you would divide by a quadratic polynomial.

#. Write a `Qt::QWidget`/Swing `JComponent` that draws all zeros of all Littlewood polynomials up to degree $d$.

#. Make the `QWidget` interactive such that one can zoom into the set and increase/ decrease the maximum degree $d$.


## 5. Attractor of a discrete dynamical system.
Such a system is given by a map $\phi\colon X\to X$ and may depend on additional parameters. Starting from an arbitrary point, the system converges to the nearest stable minimal fixed-set unless it is exactly on an unstable fixed-set. As a particular example draw the

### A. Feigenbaum scenario
which is an analysis of the logistic map
  $$ \phi\colon[0,1]\to[0,1]: x \mapsto 4\lambda x(1-x)$$
with the range $0<\lambda<1$.

### Other functions are e.g.
  $$\phi(x) = \lambda x/(1+x)^5,\quad 0<\lambda<250, 0<x<20,$$
  $$\phi(x) = x(0.5+\lambda(1-x)),\quad 0<\lambda<4, 0<x<1.5,$$
  $$\phi(x) = x\exp(\lambda(1-x)),\quad 0<\lambda<0.1,$$
  $$\phi(x) = 16\lambda(x(1-x))^2,\quad 0.4<\lambda<0.95,$$
  $$\phi(x) = \lambda\sin(\pi x),\quad 0.32≤\lambda.$$


## 6.  Repeller of a Discrete Dynamical System
I.e. the inverse of the dynamical map makes the repeller become an attractor.
Reveal the relation to the previous tasks 3 (IFS) and 5.


## 7. Implement the Chaos Pendulum in a `QtWidget`/`SVG` (or equivalent).
The chaos pendulum is a system of two real pendula one at the bottom of the other. Write down $T$ (kinetic) and $U$ (potential) energy in terms of the two positions and determine the Euler-Lagrange equations of motion (see also [chaosPendulum](../Math/introduction/chaosPendulum.pdf) for details). Simulate the motion from a random initial position.



# 5B Euclidean Geometry
## 0.  Closest Point pair

Given $N$ different points in $\mathbb R^d$, determine the 2 points with the clostes distance.

Make sure your algorithms has time complexity $\mathcal{O}(N^2d)$.


## 1. Brillouin zones / Voronoi diagram

Given a $D$ dimensional space ($D=2,3$) with $k$ distinct points, partition the space into $k$ regions such that for each region $k$ and each point $P$ within the region distance to the center $k$ is the smallest among all distances of $P$ to any center.

*Solution:*  The Euclidean space $\mathbb E(D)$ is partitioned by the perpendicular bisectors between all pairs of the centers into $k$ convex regions that each contain one center inside it which is the closest (i.e. dominating) center.

*Gamification:*  Draw $k=3,...$ centers in $\mathbb E(2)$ and let the user spray the outlines of the Brillouin zones (the above partitions).


## 2. Construction of semi-regular (Archimedean) polyhedra
A semi-regular polyhedron consists of only 2 or 3 types of regular $n$-gons and each vortex has the same structure i.e. the same number of each type of polygon meeting.  Input should be the vertex description.  Draw the polyhedron and transform it into a graph.


## 3. Convex hull in $\mathbb{R}^d$
Given $N$ point in $\mathbb{R}^d$ find the set of subsets that form the hyperplanes of the convex full of them, i.e. for every such hyperplane all the points of the original set lie in or on one side of this hyperplane.

### 3.1 Min and Max in $\mathcal O(N)$
This is the convex hull in 1D

### 3.2 [QuickHull](https://en.wikipedia.org/wiki/Quickhull) in 2D in $O(N\log r)$

1.  Find the points $A$, $B$ with min/max x-coordinates  (if there are many of them, find those with min/max y-coordinates) and start with the line segment `AB` (=hyperplane) spanned by them.

2. Split the remaining $N-2$ points into $S_+$ above and $S_-$ below the initial line.  Treat recursively each of those sets:

3. Find the convex hull above `AB` in the set `S` by if $S=\emptyset$ then the result is `AB`, otherwise

4. find the point `P` in `S` that has the biggest distance from `AB`, then find the points above `AP` and the points above `PB`

5. The result is the join of the convex hulls above `AP` and above `PB`

6. The result of the original problem is the join of the convex hulls above `AB` and above `BA`.

$r$ is the number of points in the convex hull (which also consists of $r$ faces = hyperplanes = line segments).


### 3.3 QuickHull in $d>2$ dimensions

1. Find $d$ points that span a $d-1$ dimensional hyperplane as big as possible -- if there are no such points, then the set is degenerate (see 3.4).  These points span the faces (=hyperplanes) `fs={H_±}`.  Remove them from the unassigned points.

2. For each face `f` in `fs`: find all unassigned points `P` that are above it (=away from the center) and gather them in `f.outer`.

3. For each face `f` in `fs` with non-empty `f.outer`:  Find the point `P` with maximum distance from `f`:  Replace the face `f` by faces from the facets (=$d-2$-dimensional hyperplanes) of `f` and `P` -- namely those facets that are _visible_ from `P` where _visible_ means that the cone from the facet to `P` is completely outside `f`.

4. Compute the outer points of the new faces among the outer points of `f`.

5. The algorithm ends if there are no more outer points for any of the faces `f` in `fs`.  Then `fs` is the convex hull.

Approximate alternative:  Find the embedding cube, i.e. $$\prod_{i=1}^d \left[\min_{1\le k\le N} x^{(k)}_i, \max_{1\le k\le N} x^{(k)}_i\right]$$


### 3.4 Convex Hull of Degenerate Point sets

0. If all points lie in a $d'<d$ dimensional affine subspace, then the convex hull will be in that subspace, too.  The simplest answer is to return just that subspace.

1. In addition you could transform that subspace to $R^{d'}$ by rotation, taking the first $d'$ coordinates and then find the convex hull in this $R^{d'}$, e.g. by 3.3 (or 3.2 if you are lucky).


## 4. Bézier-Casteljau Curves

Given $N+1$ points in the plane.  Can you draw a smooth curve through them?

If we assume that all $x$-coordinates are different, then we may just be looking for a polynomial of degree $N$, $p(x)=a_0+a_1x+a_2x^2+\dots+a_Nx^N$ that has $N+1$ coefficients and try to determine these from the linear equations:

  $$\begin{array}{rl}
     p(x_0) &= y_0 \\
     \vdots \\
     p(x_N) &= y_N
  \end{array}$$

For small numbers of points this may be an easy to formulate problem and the system is numerically easy to even solve it with Gaussian eliminations.  However, if the points fluctuate higher or lie naturally on a not-to-low dimensional polynomial, then the result may be less pleasant.

Instead we may want to try to patch together something with piecewise polynomials and choose the inner $N-1$ points as the breaking points of the polynomials.  Beyond requiring the function values at these inner points, we may additionally require that the first few derivatives agree.  Therefore, we end up with $(k+1)N-k$ equations for $(k+1)N$ coefficients for polynomials of degree $k$.  Smooth splines usually require the first and second derivatives to be continuous, i.e. we choose $k=3$.  The problem is, that you either come up with additional $k$ constraints or you somehow reduce the degree of some of the polynomials.

An effective means is to use the Bézier polynomials as follows:
  $$ b^0_i(x) = \theta(x-x_i) -\theta(x-x _ {i+1}) = \begin{cases} 1 &\text{for } x_i\le x < x _ {i+1},\\ 0&\text{otherwise.} \end{cases}$$
  $$ b^k_i(x) = \omega^k_i(x) b^{k-1} _ i(x) +\left(1-\omega^k _ {i+1}(x)\right)b^{k-1} _ {i+1}(x), $$
  $$ \omega^k_i(x) = \begin{cases} \frac{x-x_i}{x _ {i+k} -x_i} &\text{for } x _ {i+k}>x_i,\\  0 &\text{otherwise.} \end{cases} $$

Note that $\sum_{i=-k}^N b^k_i(x) \equiv 1$ and the functions $b^k_i(x)$ are $k-1$-fold continuously differentiable.  They thus represent a smooth partition of 1.  In order to determine the Bézier curve, you compute
  $$ p(x) = \mathbf{b}(x)\cdot\mathbf c,\quad  \mathbf c = G^{-1}X\mathbf y$$
  $$ G = XX^T,\quad X = (\mathbf{b}(x_0),\dots,\mathbf{b}(x_N)),\quad \mathbf{b}(x) = \left(b^k _ {-k}(x),\dots,b^k_N(x)\right)^T. $$

The coefficient matrix is symmetric positive semi-definite.  This allows to use Cholesky factorization for solving the linear equation for $\mathbf c$ (once).  But in order to make the matrix positive definite, you have to eliminate $k-1$ of the $b^k_i(x)$ from the definition of $\mathbf{b}(x)$.  One possibility for $k=3$ is to remove the interpolating terms $b^3 _ {1-k}(x)$ and $b^3 _ {N-1}(x)$, because these have their peak between interpolation points.

If you want to estimate & visualize the quality of the approximation, you should either implement a function plotter and/or compute the RMSE, i.e.
  $$ \mathrm{RMSE} = \left[\tfrac1{b-a}\int_a^b \left(f(x)-p(x)\right)^2\,dx\right]^{1/2}. $$
Obviously you need more than $N+1$ points for the computation of the root mean square error.


# 6. Numerical Methods
## 1. Implement fluid dynamics on the computer.
The goal is to describe 2D and 3D eddies in an ideal fluid. Instead of solving the Euler PDE manually, you can use the form of an eddy velocity field: rotationally symmetric and of constant circulation $\Gamma$, i.e.
  $$\mathbf{v}(\mathbf{r}) = \Gamma \mathbf{k} \times (\mathbf{r-r}_0) / (4\pi|\mathbf{r-r}_0|^3)$$
  or in 2D $$\mathbf{v}(\mathbf{r}) = \Gamma \mathbf{k}\times (\mathbf{r-r}_0) / (2\pi|\mathbf{r-r}_0|^2).$$
  According to Helmholtz' laws every vortex remains a vortex (they can never collide) and moves in the velocity field of the other vortices like a point particle.


## 2. Read how to use an FFT library. Solve linear PDEs using FFT, ...
splines, points via Euler forward method, ...

e.g. the heat equation:

  $$\partial_t u +\alpha^2\Delta u = f,\quad u|\partial\Omega = u_0$$

  for given $f$, $u_0$ and $u = u(t, x,y,z)$.

### 2.1 Fourier solution

  $$ u = \sum_{n=0}^\infty \exp(-\alpha^2(\pi n/L)^2t) sin (\pi nx/L)$$

### 2.2 Euler forward

 $$u(t+\Delta t, x) = u(t, x) +\alpha^2\Delta t\cdot (\Delta_2 u/(\Delta x)^2)$$

with $\Delta_2u(x_k,y_l) = u(x_{k+1},y_l)+u(x_k,y_{l+1}) -4u(x_k,y_l)
        +u(x_{k-1},y_l)+u(x_k,y_{l-1})$

### 2.3 Splines

  $$ u(t, x) = \begin{cases}  a_{0,k}(t) +a_{1,k}(t)\cdot(x-x_0) +\tfrac12a_{2,k}(t)\cdot(x-x_0)^2 &\text{for }x_{k-1}\le x\le x_{k+1}
  \end{cases}$$
together with the continuity condition
  $$u(t,x_{k-1}-) = u(t,x_{k-1}+),\quad  u(t, x_{k+1}-)=u(t,x_{k+1}+),$$
  $$\partial_x u(t,x_{k-1}-) = \partial_x u(t,x_{k-1}+),\quad
    \partial_x u(t,x_{k+1}-) = \partial_x u(t, x_{k+1}+).$$

Check what happens when you vary $\Delta t\approx (\Delta x)^2/(2\alpha^2)$.

### 2.4 Finite Elements Methods
Consider a spatial only PDE, e.g. $$-\Delta u = f $$ for the steady-state solution of the 2D heat equation with Dirichlet boundary conditions.  The biggest possible solution space (for at least continuous solutions) is $W^1_{2,0}:=\{u\in C(\bar\Omega): u|\partial\Omega=0, \nabla u\in L_2(\Omega)\otimes\mathbb{C}^2\}\subset \mathbb{H}:=L_2(\Omega)$ with inner product
$$\langle\phi,\psi\rangle :=\iint_\Omega \bar{\phi}(x,y)\,\psi(x,y)\, \mathrm{d}^2(x,y). $$
Then a solution has to fulfill
 $$\forall\phi\in W^2_{2,0}(\Omega)\colon 0 = \langle\phi, \Delta u+f\rangle
    = -\langle\nabla\phi,\nabla u\rangle_{L_2(\Omega)\otimes\mathbb{C}^2} +\langle\phi,f\rangle $$

If we use a fundamental system $\{\phi_{ij}\}$ in $W^1_{2,0}(\Omega)$, then we can write
 $$ u = \sum_{i,j} u_{ij}\phi_{ij} $$
together with
 $$ Au = f $$ where
 $$ A_{ij,kl} = h^{-2}\langle\nabla\phi_{ij},\nabla\phi_{kl}\rangle $$
is called the stiffness matrix and
 $$ f_{ij} := \langle\phi_{ij},f\rangle $$ the inner part of the load vector.
To be more precise for $\Omega=[0,1]\times[0,1]$ we use $\phi_{ij}(x,y) = \varphi_{00}(x/h-i, y/h-j)$ and
$$ \varphi_{00}(x,y) = \begin{cases} (1+x)(1+y) &\text{for } -1\le x,y\le 0, \\
  (1-x)(1+y)  &\quad -1\le y\le0\le x\le 1, \\
  (1+x)(1-y)  &\quad -1\le x\le0\le y\le 1, \\
  (1-x)(1-y)  &\quad 0\le x,y\le 1, \\
  0  &\text{else.}  \end{cases}
$$
Their partial derivatives are
$$ \partial_x \varphi_{00}(x,y) = \begin{cases} 1+y &\text{for } -1< x,y<0, \\
  -(1+y)  &\quad -1<y<0<x<1, \\
  1-y     &\quad -1<x<0<y<1, \\
  -(1-y)  &\quad 0 < x,y <1, \\
  0       &\text{else.}  \end{cases}
$$
The matrix $A$ has only 8 non-zero diagonals, namely for the generic row $(i,j)$ the entries $(i-1,j-1)$, $(i-1,j)$, $(i-1,j+1)$, $(i,j-1)$, $(i,j+1)$, $(i+1,j-1)$, $(i+1,j)$, $(i+1,j+1)$.


## 3. Spectral Method for non-linear PDEs
Read a book about the spectral transform for solving the KdV equation, e.g. [1], and implement it.

Try to solve the Ernst equation
 $$ \mathfrak{R}(u)(u_{rr}+u_{r}/r+u_{zz}) = (u_r)^2 +(u_z)^2. $$

[1] F. Calogero and S. Degasperis: *Spectral Transform and Solitons*, **1983**.


## 4. Genetic Algorithms -- Large scale (discrete) optimization problems II
A genetic algorithm is an optimization program that finds the optimal solution by mutation, crossover, and multiplication of the fittest from a population of potential solutions (gene pool). A sample problem is:

* Find the global minimum of the function $$f(x_1,x_2)=\sum_{i=1}^N \left[i+(x_1-a_{1i})^6+(x_2-a_{2i})^6\right]^{-1}$$
* use a population size of 200 and start with 200 random `chromosome`s
* a `chromosome` encodes the parameters $x_1$ and $x_2$ in binary form
* reproduction probability is proportional to $500 - f(x_1, x_2)$.
* mutation probability is `1e-2` and the mutation is only one random bit,
* crossover probability is $0.6\le p_c\le 1$ and the crossover happens at a random position with a length of $1,2,3$.
* Iteration until either a target value is *found* (approximated sufficiently close) or the population is almost homogeneous.


## 5. [Neural Networks on the Computer](https://en.wikipedia.org/wiki/Artificial_neural_network)
Read a good book about Neuronal Networks and implement them on the computer.

_Implementation languages:_ Python (tensorflow library, pytorch), Fortran, C++, Matlab

[Literature](https://en.wikipedia.org/wiki/Artificial_neural_network), e.g. K. Gurney:  An Introduction to Neural Networks. UCL Press **(1997)**. *ISBN* 978-1857286731. *OCLC* 37875698.


### A. Strategic AI for 3 player chess -- Large-scale Discrete Optimization Problems II

Starting from the [3 player chess server & client](#implement-a-server-and-clients-for-some-of-the-3-player-chess-games) implementation (medium sized project), write program(s) that:

#. can play final moves,
#. play initial moves according to board evaluations,
#. can backtrack game trees from arbitrary positions.

As a hint for implementations as well as strategies consult [ThreeWayChess.org/Thinking strategies](http://www.threewaychess.org/Comparison of breadth, depth, and kinds of thinking required5.pdf).


## 6. Hastings-Metropolis Algorithm

Implement the HM algorithm and statistical sampling as an approximation for computing path integrals.

Verify your algorithm by approximating the Black-Scholes-Mertens formula for European Options Pricing.

Simulate American Options Pricing and Pricing of Options of Stock with Dividend payments.


# 5C. Algorithms for Topology

Read the book *Computing for Topology* and implement some of its algorithms in Kotlin/Scala/Haskell/C++.



# 5D. Manifolds on the computer

A manifold can be represented by a Čech groupoid, i.e. a collection of connected and simply connected open coordinate patches that are glued together along connected simply connected subpatches.

While in mathematics, manifolds are typically paracompat (i.e. have a locally countable cover), we will restrict to finitely generated manifolds for obvious reasons.


_Medium sized subprojects_

## 0 [Simplicial complexes](https://en.wikipedia.org/wiki/Simplicial_complex)
Implement a simplex as a set/ vector of vertices, a simplicial complex as a set of simplices. Implement the properties `vertices` and `segments`, and operations `addSimplex`, `removeSimplex`, `union`, `disjointUnion`, ...

## 1 finite [CW complexes](https://en.wikipedia.org/wiki/CW_complex)
Implement as `template <...> class CW`, parameter the dimension $d$.

1. 1 dim'l CW cpx is an undirected graph
2. Maps from a $d$-sphere to a $d$-dim'l CW cpx.
3. Operations: Construction from a $d$-1 dim'l CW cpx, a number of disks, and a bunch of maps from the corresponding spheres to the $d-1$ dim'l subspace.


## 2 [Projective varieties](https://en.wikipedia.org/wiki/Algebraic_variety#Projective_varieties_and_quasi-projective_varieties) over $\mathbb{R}$
Given the above implementation of finite CW complexes try to transform an arbitrary irreducible projective variety (connected algebraic set in a projective space) into its CW representation.  Try to implement the computation of common geometric properties:

* tangent space (assuming the CW complex is smooth)
* p-forms
* (co)-homology, e.g. de Rham cohomology or cell homology -- try to find a normal form for an associative graded commutative algebra and transform the projective variety in such a normal form

## 3 [Diffeologies](https://en.wikipedia.org/wiki/Diffeology)
*Def.* If $X$ is a topological paracompact space, a diffeology on $X$ is a family of maps, called plots, each from an open subset of $\mathbb R^d$ ($d\ge0$) to $X$ such that the following hold:

* Every constant map $\mathrm{pt}\colon\mathbb R^d\supset U\to X$ is a plot;
* For a given map $p\colon \mathbb R^d\supset U\to X$, if every point $p_0\in U$ in the domain has a neighborhood $p_0\in U_0\subset U$ such that restricting the map to this neighborhood $p|U_0\to X$ is a plot, then the map $p$ is a plot;
* If $p\colon U\to X$ is a plot, and $f\colon U'\to U$ is a smooth function from an open subset $U'\subset\mathbb R^{d'}$ of some standard space into the domain $U$ of $p$, then the composition $p\circ f\colon U'\to X$ is a plot;

*Rem.*: The domains of different plots can be subsets of $\mathbb R^d$ for different $d$s.

A topological space $X$ together with a diffeology is called a diffeological space.  By abuse of notation we will just write $X$ whenever the diffeology is clear.

A map $f\colon X\to Y$ between diffeological spaces $X$ and $Y$ is called *smooth* iff composing it with every plot of $X$ is a plot of $Y$, i.e. $p\colon \mathbb R^d\supset U_p\to X$ a plot, then $f\circ p\colon \mathbb R^d\supset U_p\to Y$ is a plot of $Y$.

A *diffeomorphism* is a smooth bijective map whose inverse is also smooth.

The diffeological spaces together with smooth maps form a category.  A diffeological space is only unique up to diffeomorphism.

Examples:

### 0. Smooth manifolds (with or without border)
are smooth diffeologies.  The charts generate all maps.  The diffeomorphisms between them are the diffeomorphisms from smooth differential geometry.


### 1. Manifolds with corners
are diffeologies.  The difference between $[0,1]\times\{0,1\}\cup\{0,1\}\times[0,1]$ and $\mathbb S^1$ is that the former has 4 corners (the $(x,y)$ with $x,y\in\{0,1\}$) while the latter is rotation invariant and thus has no corners.


### 2. Square with extending line segment
It is possible to define a diffeology on the union of an (open) square with a line segment (that overlap).  Namely, let $X, Y$ be two diffeological spaces that are topological subspaces of $X\cup Y$, then we can endow $X\cup Y$ with the union diffeology that is generated by all maps to either $X$ or $Y$.


Q: How can we capture finitely generated diffeologies, beyond finite spaces?



# 7. Diverse Problems
## 1.  Learn another programming language
e.g. [Python](https://python.org/), [Kotlin](https://kotlinlang.org/), [TypeScript](https://www.typeScriptLang.org/), [Scala](https://scala-lang.org/), [Rust](https://rust-lang.org/), [C++](https://www.cplusPlus.com/), [Haskell](https://haskell.org/), ... and implement some [elementary things](#smaller-ideas), e.g. polynomials.  Be sure to cover a common style guide as well as patterns and anti-patterns.  Note that the difference between programming languages is not the spelling of variables, functions, classes, ..., but the grammar, the (current) standard library as well as the (current) best practices.  Once you have learned 3 languages, start a comparison according to, e.g., speed, portability, availability of common libraries for common domains (e.g. web programming, algorithms, test driven development, graphics, signal processing, numerical simulations, ...), ... .


# 8. Mobile & Web Apps
## 1. Drawing Lines
Write a mobile app with which you can

1. draw lines,
2. select a line and drag, endpoint drag,
3. scale and rotate (about its center),
4. multiple selection with the operations (except endpoint drag) and persisting (app sleep/restart),
5. grouping (at least 2 elements) where drag, scale and rotate operate on the whole group together,
6. copy, cut, paste the selection (with multiple pasting, other app interoperation)

Please complete in Scrum style (not waterfall), i.e. finish each point as MVP (not too much future designed)

recommended programming languages: Kotlin (for Android platform) or Swift (for iOS platform).

Pattern recommendation: MVC, MVVM or the platform's recommended structure

_Implementation:_ Kotlin, TypeScript, Haskell, Python


## 2. Timeline Infographic

Develop a web app which documents a series of historic events. Ideally dates are separated evenly (or exponentially) and make use of infinite scrolling. This implies that scrolling back to 1980's is quick but scrolling back to the age of the dinosaurs is tedious. The interface should be engaging. The app should provide filters and make use of common gestures.

### Skills

- Gestures
- Infinite scrolling
- Animations
- Interactivity

## 3. Five wins
2 players put symbols (cross and circle) alternatingly on an infinite grid.
The winner is who first gets 5 of their symbols in a tight row/column or diagonal, e.g.
`xxxxx`, or
```
  x
   x
    x
     x
      x
```
or similar.  The row is broken if there is 1 opposite or empty symbol in between.  In that case it is not a win.

### 1. Write algorithms to check automatically if anyone has won.

### 2. What happens at 4 wins?
What happens when you play that in a finite 4x4 grid, 5x5?

### 3. Write an AI that

1. predicts the next immediate winning move
2. predicts a blocking move, i.e. one that if omitted lets the opponent win
3. predicts a winning in 2 moves
4. blocks a 2 wins for the opponent

99. Winning strategy

_Implementation:_ Kotlin, TypeScript, Haskell, Python


## 4. Crime Scene
The goal of the game is to find out who killed with what action in what kind with
which weapon out of what motive.  Given for *n* players are $n+1$ of each type,
one set is given to you as alibis and you are to formulate crime hypotheses and
interrogate the other witnesses who can disprove your hypothesis by providing one
alibi item. If nobody can (not even yourself), then you win.  To make it more
interesting, all players are inspectors and formulate hypotheses intertwined.
When another detective disproves their hypothesis, you only learn that it was
disproved not how.

Try to generate the grammar correctly.

_Implementation:_ Kotlin, TypeScript, Haskell, Python


## 5. Super Code
Given pins of 6 different colors (e.g. Red, Orange, Yellow/Brown, Green, Blue, Purple) one player is to pick 4 pins in order and thus selects a secret code.  The other player is to guess this secret code.  Whenever a guess is produced, the first player gives full points for every correct pin and half points for every correct color at the wrong place.

### 2. Write a playing strategy that can find the correct code with a minimum number of guesses

_Implementation:_ Kotlin, TypeScript, Haskell, Python


## 6. Groups Game
Given cards with 4 features, e.g. repetition, shape, shading, color a *Group* are three cards that cannot be divided into 2 parts according to any single feature, i.e. in each feature the cards have either all the same value or all different values.  For example the cards "one red striped diamond", "two red solid diamonds" and "three red empty diamonds" form a *Group* while the three cards "one red striped diamond", "two red solid spades", "three red empty diamonds" do not form a *Group* (they split into 2 groups w.r.t. shape).

Your goals are to

1. implement a *server* that shuffles a deck and pops up cards one by one.
2. The players are allowed to call "Group" at every moment.
3. If one player does so, they need to point out a *Group*.

  a) If they can, they earn a point and the corresponding cards are removed,
  b) if they cannot they loose a point.

4. The game is over if either 12 cards are open or the full stack was popped up.  The player with most points wins.

Warm up:  Given 4 features with 4 values each, e.g.

i. repetition: 1, 2, 3, 5
ii. shape: diamond, club, spade, heart
iii. color: red, orange, green, blue,
iv. shade: solid, striped, dotted, empty

1) How many different cards exist?
2) How many cards can you make such that every 2 cards have exactly 1 other card (in the deck) that makes them a *Group*?
3) How can you shuffle them efficiently?

_Implementation:_ Kotlin, TypeScript, Haskell, Python

## 7. Ping pong
Write a browser game that simulates tennis playing i.e. a ball (small circle) moves linear until it hits either a boundary or a racket where it is reflected accoding to physics laws for totally elastic reflection.  You control your own racket with the mouse (moving up and down).

### Vary the speed of the ball

### Introduce barriers

### vary racket size

## 8. Grazing maze

### 1. Implement pacman in the browser
A Maze in which there are dots along the walking ways and from time to time fruits (giving extra points).  The player is to graze the maze in limited time and collect as many points as possible (but with limited speed).

### 2. Add automated evil bots

Whenever the player collides with an evil bot, they die.  There may be an undestructible mode for 20s after picking a particular fruit in which you can kill evil bots by touching and collect points for them.

### 3. Allow Multiplayer games

Either let the player run one after the other each on an individual board.

Or allow 2 competing players to play on a board.

### 4. Define an API to allow for AI agents to play

### 5. Allow for (parameterized) random generation of the Maze

The parameter is the game level, i.e. the complexity of the maze, e.g. the non-straightness of the paths.

### 6. Allow for hexagonal grids

The cell centers lie on a triangular grid and valid moves are l-r, u-d, ur-dl.



# 8B. MDA (Multiple Devices App)
Write an app that can be used in browser (desktop, laptop, tablet, phone) as well as via a mobile app.

The app should thus have 4 components:

0. backend server
1. common part (e.g. minimal data structures)
2. mobile app part
3. web app

You may consider the above "LineSketch" app as an idea.  The backend could host a collection of sample drawings.

## 2. Implement servers for multi-player modes of the previous games

### 1. Five wins
### 2. Crime Scene
### 3. Groups Game


## 3. Implement a server and clients for some of the [3 player chess games](http://en.wikipedia.org/wiki/Three-player_chess)

Start with writing a simple application that permits to play at one computer and checks for all rules during moves. Extend by protocollizing the moves, saving, and restoring the game. First implementation can be in C++/Qt, but the internet version should be in TypeScript (or Kotlin/Java for the server).


## 4. Hnefatafl
Implement the [Tafl games](https://en.wikipedia.org/wiki/Tafl_games) in the browser (with server backend).

Add a computer strategy (s.th. one player can also play).


## 5. Power Grid
The board game consists of a map with a couple of cities and some of their
connections.  The players are to start in one city and build power connections
to some other cities while also buying power plants and fossils to fire those
power plants.  New fossils are discovered at the end of every turn, but fossils
are limited and become scarce over time.  The player with the most powered cities
in the end wins.



# 8C. Architecture Katas
Once you have run (or ruined) your first larger project.  It is time to do project management in a more systematic way.  That is what [Architecture Katas](archKatas.html) are good for.



# 9. Compiler
Goal is to build a working compiler.  You may want to read about more specific ideas in e.g.

[1] D. Grune, K.v.Reeuwijk, H.E. Bal, C.J.H. Jacobs, K. Langendoen: *Modern Compiler Design*, **2012**.

## 0. Find an easy programming language
0. Better than brainfuck or white code

1. not too complicated (What is the advantage of Context Free Grammars, LL1-Grammars?)

2. Turing complete (addition, peek, poke, loop&exit, conditional execution)

If you have no idea yet, read about the [Quorum](https://quorumlanguage.com/) project, i.e. evidence-based design of a programming language.

3. write at least 2 simple programs in it


## 1. Frontend: Determine a normal form and write a parser for it
producing an (abstract) annotated syntax tree (AST) out of a syntactically correct program.

If you use a parser generator (yacc or similar) make sure that you understand
what the generated code does and how to modify it if additional behavior is needed.


## 2. Backend: Write a compiler/transpiler to LLVM, node.js or JVM

Compile the above written sample programs and check that they work correctly

extract the specifications of a minimal standard library, e.g. `printf`, `main`


## 3. Write some optimizer

1. Rewrite the above sample programs in the native language and compare the output of your compiler with the manually written code.  See if you can find optimizations.

2. Make sure the optimizations leave the interpretation invariant in all (testable) cases

3. Test by time or memory measurements with appropriate samples that the optimizations indeed give an advantage


------

This page is maintained by [M.Gr.](http://melli79.github.io/).
