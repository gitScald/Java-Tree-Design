 * Author:		Benjamin VIAL
 * ID:			29590765
 * Course:		COMP 352
 * Instructor:		Stuart THIEL
 * Due date:		Friday, June 16th, 2017

 Huffman coding tree & AVL/Splay tree design
 ===========================================

Archive contents:
	- atree.java:		Source file for the AVL/Splay tree implementation
	- huffman.java:		Source file for the Huffman coding tree implementation
	- Jabberwock.txt:	Encoding source file for the Huffman coding tree
	- Operations.txt:	Operations file used to build the AVL/Splay tree
	- RandomString.txt:	Student ID/string lookup file to generate Huffman code from a specific string
	- README.txt:		This document
	- com:			Contains source files for supporting classes
	- text:			Contains write-up (.docx and .pdf) for the textual responses, as well as the explanations of design choices
	- uml:			Contains high-resolution (.png) images of UML class diagrams, as well as the complete .xml file

 Huffman coding tree
 -------------------

Usage:
	java huffman <encoding source> <string source> <student ID>
where:
	<encoding source> 	the file to build the Huffman coding tree from (e.g., Jabberwock.txt)
	<string source> 	(Optional) the file to fetch the string to encode from (e.g., RandomStrings.txt)
	<student ID> 		(Optional) the student ID to look for in fetching said string (e.g., 29590765)

 AVL/Splay tree
 --------------

Usage:
	java atree <operations source>
where:
	<operations source>	the file to build the AVL/Splay tree from (e.g., Operations.txt)