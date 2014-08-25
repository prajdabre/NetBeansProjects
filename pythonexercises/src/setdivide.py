#! /usr/bin/python

# To change this template, choose Tools | Templates
# and open the template in the editor.

c=[13, 59, 115, 175, 180, 214, 251, 317, 372, 416]

s1=[]
s2=[]
c.sort(None, None, True)

for i in c:
    if sum(s1)==sum(s2):
        s1.append(i)
    elif (sum(s1)-sum(s2))>=i:
        s2.append(i)
    else:
        s1.append(i)

print s1
print s2
print abs(sum(s1)-sum(s2))


c.sort()
a=[]
b=[]
a.append(c.pop())
while c:
        if len(a) > len(b):
            b.append(c.pop())
        elif len(b) > len(a):
            a.append(c.pop())
        elif sum(a) > sum(b):
            b.append(c.pop())
        else:
            a.append(c.pop())
print a, b
print sum(a), sum(b)
print len(a), len(b)