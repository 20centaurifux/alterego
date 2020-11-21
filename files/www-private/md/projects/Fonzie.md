# Fonzie

## Introduction

**Fonzie** is a tiny virtual machine. It's a fun project created to
test auto-generated algorithms in a virtual environment. **Fonzie**
is a hack, don't use it in production :)

The **Fonzie** machine has 8 registers, a stack and two segments:
one for data and another one for code.

The default stack size is 256 bytes. The data segment can have 256 bytes
and the code segment 2048 bytes.

Calling a subroutine **Fonzie** pushes the return address onto the stack.
Please keep this in mind before returning from a subroutine.


## Registers

**Fonzie** has the following 8 registers:

| Register(s) | Description                                          |
| :----- | :-------------------------------------------------------- |
| A0..A3 | used in mathematical instructions                         |
| R      | stores the result of mathematical instructions            |
| IP     | points to the location of the current executing statement |
| SP     | points to the top of the stack                            |
| FL     | stores various flags                                      |
| EX     | stores exceptions                                        |

The only supported data type is *DWORD*. **Fonzie** stores bytes in *big endian*
format.

## Opcodes / Instructions

Results of mathematical instructions are stored in the *R* register.

Comparing two *DWORDs* you find the result of the operation in the *FL* register.

The following list contains all available *Opcodes*:

| Opcode                         | Description                                                                      |
| :----------------------------- | :------------------------------------------------------------------------------- |
| 01 (*MOV\_REG\_REG*)           | copy *DWORD* in second register to first one                                     |
| 02 (*MOV\_REG\_ADDR*)          | copy *DWORD* in memory to register                                               |
| 03 (*MOV\_ADDR\_REG*)          | copy *DWORD* in register to memory                                               |
| 04 (*MOV\_REG\_DWORD*)         | copy *DWORD* to register                                                         |
| 05 (*MOV\_REG\_ADDR\_IN\_REG*) | copy *DWORD* in memory to register, the address is taken from the given register |
| 06 (*INC*)                     | increment *DWORD* in register                                                    |
| 07 (*DEC*)                     | decrement *DWORD* in register                                                    |
| 08 (*SUB\_REG\_REG*)           | subtract value in second register from value in first one                        |
| 09 (*SUB\_REG\_ADDR*)          | subtract value in memory from value in register                                  |
| 10 (*SUB\_REG\_DWORD*)         | subtract *DWORD* from value in register                                          |
| 11 (*ADD\_REG\_REG*)           | add values from two registers                                                    |
| 12 (*ADD\_REG\_ADDR*)          | add value in register and value in memory                                        |
| 13 (*ADD\_REG\_DWORD*)         | add value in register and *DWORD*                                                |
| 14 (*MUL\_REG\_REG*)           | multiply values from two registers                                               |
| 15 (*MUL\_REG\_ADDR*)          | multiply value in register and value in memory                                   |
| 16 (*MUL\_REG\_DWORD*)         | multiply value in register and *DWORD*                                           |
| 17 (*DIV\_REG\_REG*)           | divide value in second register from value in first register                     |
| 18 (*DIV\_REG\_ADDR*)          | divide value in memory from value in register                                    |
| 19 (*DIV\_REG\_DWORD*)         | divide *DWORD* from value in register                                            |
| 20 (*AND\_REG\_REG*)           | bitwise AND on value in first and second register                                |
| 21 (*AND\_REG\_ADDR*)          | bitwise AND on value in memory and register                                      |
| 22 (*AND\_REG\_DWORD*)         | bitwise AND on *DWORD* and value in register                                     |
| 20 (*OR\_REG\_REG*)            | bitwise OR on value in first and second register                                 |
| 21 (*OR\_REG\_ADDR*)           | bitwise OR on value in memory and register                                       |
| 22 (*OR\_REG\_DWORD*)          | bitwise OR on *DWORD* and value in register                                      |
| 26 (*MOD\_REG\_REG*)           | divide value in second register from value in first register                     |
| 27 (*MOD\_REG\_ADDR*)          | divide value in memory from value in register                                    |
| 28 (*MOD\_REG\_DWORD*)         | divide *DWORD* from value in register                                            |
| 29 (*RND*)                     | store random number in *R*                                                       |
| 30 (*RET*)                     | return from (sub)routine                                                         |
| 31 (*CMP\_REG\_ADDR*)          | compare value in memory to value in register                                     |
| 32 (*CMP\_REG\_REG*)           | compare value in second register to value in first one                           |
| 33 (*JE*)                      | jump if compared values are equal                                                |
| 34 (*JNE*)                     | jump if compared values aren't equal                                             |
| 35 (*JGE*)                     | jump if second value is greater than or equal to first one                       |
| 36 (*JG*)                      | jump if second value is greater than first one                                   |
| 37 (*JLE*)                     | jump if second value is less than or equal to first one                          |
| 38 (*JL*)                      | jump if second value is less than first one                                      |
| 39 (*CALL*)                    | call subroutine                                                                  |
| 40 (*PUSH*)                    | push *DWORD* stored in register to stack                                         |
| 41 (*POP*)                     | pop *DWORD* from stack to register                                               |
| 42 (*MOVS_REG_STACK*)          | copy *DWORD* from register to stack address                                      |
| 43 (*MOVS_STACK_REG*)          | copy *DWORD* from stack address to register                                      |

The exceptions below may occur during operation:

| Opcode | Description                     |
| :----- | :------------------------------ |
| 01     | a specified address is invalid  |
| 02     | a specified register is invalid |
| 03     | stack overflow                  |
| 04     | carry over                      |
| 05     | division by zero                |
| 06     | a given *Opcode* is invalid     |

Exceptions are stored in the *EX* register.

Returning from the main routine the virtual machine halts.

## Building Fonzie / Usage

**Fonzie** uses [GNU Make](https://www.gnu.org/software/make/) as
build system. To compile the program run the following command:

```
$ make
```

Having a little endian system the additional option below is necessary:

```
-DLITTLE_ENDIAN 
```

If you have a x86 compatible CPU the following option turns on some
optimizations:

```
-DARCH_X86
```

You can use the executable to run binaries in the *Delvecchio* format. To
build such binaries please have a look at the
[fasm](https://github.com/20centaurifux/fasm/) project.
