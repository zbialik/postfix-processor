# Data Structures Lab 1

Assume you have a Machine with a single register and six instructions       (Problem 2.3.10 from the LAT text.)

LD  A  places the operand A in the register 
ST  A  places the contents of the register into the variable A
AD  A  adds the contents of the variable A to the register
SB  A  subtracts the contents of the variable A from the register
ML  A  multiples the contents of the register by the variable A
DV  A  divides the contents of the register by the variable A

Write a program that accepts a postfix expression containing single letter operands and the operators +, -, \*, and / and prints a sequence of instructions to evaluate the expression and leaves the result in the register. Use variables of the form TEMPn as temporary variables. For example, using the postfix expression ABC*+DE-/ should print the following:

```
# ABC*+DE-/ converts to machine lang sequence:

LD  B
ML  C
ST  TEMP1
LD  A
AD  TEMP1
ST  TEMP2
LD  D
SB  E
ST  TEMP3
LD  TEMP2
DV  TEMP3
ST  TEMP4
```

# Solution

TBD
