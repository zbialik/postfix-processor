# Data Structures Lab 1

Assume you have a Machine with a single register and six instructions       (Problem 2.3.10 from the LAT text.)

```
"LD  A"  places the operand A in the register 
"ST  A"  places the contents of the register into the variable A
"AD  A"  adds the contents of the variable A to the register
"SB  A"  subtracts the contents of the variable A from the register
"ML  A"  multiples the contents of the register by the variable A
"DV  A"  divides the contents of the register by the variable A
```

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

# Pre-Reqs
* Java 11
* Maven v3.6.x

# Build Code

You can compile the source code by executing the following:

```
git clone git@github.com:zbialik/postfix-processor.git
cd postfix-processor
mvn clean
mvn compile
mvn package
```

The compiled jar file will be located in `target/postfix-processor-0.0.1-SNAPSHOT.jar`


# Run Compiled Code

The compiled code requires 2 input arguments:
1. `inputFile` (relative or absolute path)
2. `outputFile` (relative or absolute path)

I have provided a test `inputFile` in `src/test/resources/postfix-inputs.txt`.

You can run the compiled code like so:

```
java -cp target/postfix-processor-0.0.1-SNAPSHOT.jar datastructures_lab01.postfix_processor.PostfixProcessor src/test/resources/postfix-inputs.txt my-output.txt
```

The output file will look similar to the following:

```
---------------------------------------------
ABC*+DE-/ as machine lang sequence:
	LD	B
	ML	C
	ST	TEMP1
	LD	A
	AD	TEMP1
	ST	TEMP2
	LD	D
	SB	E
	ST	TEMP3
	LD	TEMP2
	DV	TEMP3
	ST	TEMP4

---------------------------------------------
AB+C- as machine lang sequence:
	LD	A
	AD	B
	ST	TEMP1
	LD	TEMP1
	SB	C
	ST	TEMP2

---------------------------------------------
ABC+/CBA*+ is an INVALID postfix expression
```

NOTE: The output when processing `src/test/resources/postfix-inputs.txt` will be different than the above.
