# fasm

## Introduction

**fasm** is an assembler for the
[Fonzie](https://github.com/20centaurifux/Fonzie/) virtual machine.
You can use it to build binaries in the *Delvecchio* format. These binaries
can be loaded and executed by the machine.

## File format

The syntax is similar to most x86 assemblers. You can define two sections
in your source code:

| Opcode | Description                                   |
| :----- | :-------------------------------------------- |
| .data  | *DWORDs* stored in the memory of the machine  |
| .code  | instructions                                  |

### .data section

Variables are defined in the *.data* section. The maximum length of a
variable name is 16, the minimum length is one character. Letters, numbers and
underscores are allowed in a variable name, but it cannot start with a number.

A valid *.data* section may look the following way:

```
.data
	f00_=900  ; first value
	_bar=2000 ; second value
```

As you can see each instruction can end with a comment. Comments are introduced
by a semicolon.

The *.data* section is optional.

### .code section

**fasm** uses
[Intel syntax](https://en.wikipedia.org/wiki/X86_assembly_language#Syntax).
The *mnemonics* below are supported:

* mov
* inc
* dec
* sub
* add
* mul
* div
* and
* or
* rnd
* ret
* cmp
* je
* jne
* jge
* jg
* jle
* jl
* call
* ret
* pop
* push
* movs

You find a description of all available instructions in
**Fonize's** [README](https://github.com/20centaurifux/Fonzie/blob/master/README.md)
file.

This example shows how a complete source file can look like:

```
.data
	foo=900

.code
	mov a0, [foo] ; copy 900 to register a0
	mov a1, 100   ; copy 100 to register a1
	add a0, a1    ; add register a0 and a1, result is stored in r
	mov a0, r     ; copy r to a0
	cmp a0, a1    ; compare a1 to a0
	jl foobar     ; jump to subroutine if a1 is less than a1
	ret           ; without a return the subroutine foobar would be executed

	foobar:
	mov a0, a1    ; copy a1 to a0
	ret
```

Label names follow the same rules as variable names.

## Compiling source files

To compile a source file you have to specify two options: the source and the destination
filename:

```
$ fasm.exe --in=mysource.s --out=mybinary.bin
```

It's also possible to use a separate file for the data segment:

```
$ fasm.exe --in=mysource.s --datafile=mydata.bin --out=mybinary.bin
```

## Building fasm

**fasm** is written in C#. The easiest way to build the executable is to
compile it with [MonoDevelop](http://www.monodevelop.com/) or
[Microsoft Visual Studio](https://www.visualstudio.com/).

If you want to build **fasm** with the Mono compiler you can also use
the [Makefile](https://github.com/20centaurifux/Fonzie/blob/master/Makefile).
