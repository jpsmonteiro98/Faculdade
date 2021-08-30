import Data.Char


triangulo :: Int -> Int -> Int -> Bool
triangulo a b c | a+b>c && a+c>b && b+c>a = True
                | otherwise = False
                
aTri :: Float->Float->Float->Float
aTri a b c = sqrt(s*(s-a)*(s-b)*(s-c))
  where s = (a+b+c)/2

-- Numeros primos

divisores :: Int->[Int]
divisores n = [x | x<-[1..n],mod n x==0]

primo :: Int -> Bool
primo n = divisores n==[1,n]

primos :: Int->[Int]
primos n= [x | x<-[1..n],primo x]

--Finish

--Ex 6
binom :: Int->Int->Float
binom n k = (fromIntegral (product [1..n])) / (fromIntegral (product [1..k]*product [1..y]))
  where y = n-k

--Ex 7a
maxi x y = if x>=y then x else y
mini x y = if x<=y then x else y

max3 a b c = max (maxi a b) c
min3 a b c = min (mini a b) c 

--Ex 8
--orderTriple a b c = (min3 a b c, min (max(a,b) c), max3 a b c)
                    --incompleto
--Ex 9
classifica a | a<9 = "Reprovado"
             | a>=10 && a<=12 = "Sufeciente"
             | a>=13 && a<=15 = "Bom"
             | a>=16 && a<=18 = "Muito Bom"
             | a>=19 && a<=20 = "Muito Bom e Com Distinção"
--Ex 10
ouExcl :: Bool->Bool->Bool
ouExcl a b= (not a && b) || (a && not b)

--Ex 11
--safeTail ::  [a]->[a]
--safeTail [x:xs] = if []==[x] then [x]
  --            else if []==[] then []
  --               else xs

--Ex 12

---------------------------------------------------------------------------------------------

--Ex 22
somaQuadrados :: [Int]->Int
somaQuadrados xs = sum[x^2 |x<-xs]


somaQuadrados2 = sum[x^2 | x<-[1..100]]

--Ex 23
aproxA :: Int -> Double
aproxA n = 4 * sum [((-1)^x)/(fromIntegral(2*x + 1)) | x <- [0..n]]

--Ex 24
divdrop :: Int -> [Int]
divdrop n = [x | x <-[1..(n-1)], mod n x==0]

--Ex 30
factorial :: Int->Int
factorial 1 = 1
factorial n = n*factorial (n-1)

--Ex 30b
rangeProduct :: Int -> Int -> Int
rangeProduct n m | m /= n = m * rangeProduct n (m-1)
                 | m == n = n
--Ex 31
mulsoma :: Int->Int->Int
mulsoma 1 m = m
mulsoma n m = m + mulsoma(n-1) m

--Ex 34
fun34 :: (Float->Float)->Float->Bool
fun34 f 0 = (f 0)==0
fun34 f n = (f n)==0 || f(n-1)==0

--Ex 35
fun35 :: (Int->Int)->Int->Int
fun35 f 0 = f 0
fun35 f n = f n + fun35 f (n-1)

--Ex37
expoente :: Int->Int
expoente 0 = 1
expoente n = 2^n

concat3 :: [[a]]->[a]
concat3 []=[]
concat3 (x:xs) = x ++ (concat xs)

replicate3 :: Int->a->[a]
replicate3 0 m = []
replicate3 n m = [m] ++ replicate3 (n-1) m

--Ex 42
intersperse :: a->[a]->[a]
intersperse m [] = []
intersperse m [x] = [x]
intersperse m (x:xs)= x : m : intersperse m xs

--Ex 43
insert :: Ord a => a->[a]->[a]
insert a [] = [a]
insert a (x:xs) | a<x = a : (x:xs)
                | otherwise = x : (insert a xs) 
--Ex 43b
isort :: Ord a =>[a]->[a]
isort [] = []
isort [x] = [x]
isort (x:xs) = insert x (isort xs)

--Ex 44
minimum2 :: Ord a =>[a]->a
minimum2 [x] = x
minimum2 (x:xs) | x<head(xs) = minimum2 (x : tail(xs))
                | otherwise = minimum2 xs
--Ex 44b
delete2 :: Eq a=> a->[a]->[a]
delete2 a [] = []
delete2 a (x:xs) | a==x = xs
                 | otherwise = x : delete2 a xs
--Ex 44c
ssort :: Ord a=>[a]->[a]
ssort []=[]
ssort x = minimum2 x : ssort(delete2(minimum2 x)x)

--Ex 45
merge :: Ord a=> [a]->[a]->[a]
merge [][]=[]
merge (x:xs) (y:ys) | x>y = y: x: merge (xs)(ys)
                    | otherwise = x: y: merge(xs)(ys)

--Ex 49b
concat4 :: [[a]] -> [a]
concat4 x = foldr (++) [] x

reverse49 :: [a]->[a]
reverse49 x = foldr (\x y->y ++ [x]) [] x 

reverse50 :: [a]->[a]
reverse50 x = foldl (\x y-> [y] ++ x) [] x

elem48 :: Eq a => a->[a]->Bool
elem48 n m = any (n==) m 

dec2int :: [Int]->Int
dec2int x = foldr (\x y-> y*10 +x) 0 x 
