#jScrabble3D
```
 * Original webpge http://www.jscrabble.wz.cz/
```
#Dictionaries
```
If the dictionaries dont have sense, then your platform have different encoding then is "default".
Thast my wrong, but for me hard to fix.

CZ and English dictioneries were fixed:
enca  -L czech cz-Sk2.txt
cz-Sk2.txt: MS-Windows code page 1250
iconv -f "windows-1250" -t "UTF-8" cz-Sk2.txt-nwt -o cz-Sk2.txt-nw
rm cz-Sk2.txt
mv cz-Sk2.txt-nw cz-Sk2.txt

Other may need similar
```
#Depndencies
```
(whole) lwjgl and jl1
```
