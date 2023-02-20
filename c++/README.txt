------------------------------------------------------
### MOORE MACHINE (OR, DFA) LEARNING EXERCISE DETAILS: 
------------------------------------------------------
#compiling moore.cpp 

g++ moore.cpp -o moore_exec -O3

# Expected input alphabet 

A = {0, 1} 

#executing moore_exec

./moore_exec 0011
./moore_exec 00000

# Output of moore_exec 

For a given binary string (i.e., containing only 
0/1 and nothing else) as command line argument, 
moore_exec either prints "Yes" or "No" (without quotes)
followed by a new line. "Yes" signifies the input 
string is a member of the language whereas "No" 
suggests that the string is not a member of the language.  

If the second command line argument 
contains a string which includes any 
character other than '0' or '1' (without quotes), 
it will terminate the program with an assertion 
violation.





------------------------------------------------
### MEALY MACHINE LEARNING EXERCISE DETAILS 
------------------------------------------------ 

#compiling mealy.cpp

g++ mealy.cpp -o mealy_exec -O3

#expected input and output alphabet 

A = {0, 1}
O = {0, 1, 2, 3, 4} 

#executing mealy_exec 

./mealy_exec 00000 (prints '0' in the standard output)
./mealy_exec 000001 (printf '1' in the standard output)

#output of moore_exec 

For a given binary string containing nothing but 0/1, it 
outputs an integer in the following set {0, 1, 2, 3, 4} 
followed by a new line. 

If it is given a string that contains any other character 
except 0/1, it will throw a runtime exception due to assertion 
violation.  
 
