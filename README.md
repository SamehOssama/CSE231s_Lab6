# CSE231s: Advanced Computer Programming
## Exception handling assignment
Write a program that reads an ARXML file containing a list of containers, each with a unique ID, and reorders the containers alphabetically by their name sub- container "SHORT-NAME".

The program should write the reordered containers to a new ARXML file.


### Tasks:
1) The arxml file name should be a argument which is passed through the command line.
2) If the file doesn't have `.arxml` extension, trigger a handled exception `NotVaildAutosarFileException`.
3) If the file is empty, trigger an unhandled exception `EmptyAutosarFileException”`.
4) Name the output file from `example.arxml` to `example_mod.arxml`.

### Deliverables:
- Link to the github repository.
- Bash file containing 3 test cases (correct, wrong file extension, empty file).
- Readme.md file with proper documentation.

---

## Code walkthrough:
There are 2 approaches to solve this assignment, The first approach is to parse the file as an XML document and the second approach is to interprit the file as strings.
### File structure:
The code consists of 10 files:
|File Name|Description|
| -----------|-----------|
|[ArxmlParser.java](#arxmlparserjava)|Contains the solution for the XML document approach.|
|[ArxmlSolver.java](#arxmlsolverjava)|Contains the solution for the string approach.|
|NotVaildAutosarFileException.java|Checked exception for wrong extension files.|
|EmptyAutosarFileException.java|Unchecked exception for empty files|
|[start.bat](#startbat)|Contains the batch code that runs the program in its 3 test cases.|
|[test.arxml](#test_in)|Contains the test input for the program.|
|[test_mod.arxml](#test_out)|The file that gets created with the sorted data.|
|wrong.aml|The test file with the wrong extension.|
|empty.arxml|The empty test file.|
|xsl.xsl|The output template for the ArxmlParser file.|

### ArxmlParser.java
The first approach is the `DOM` parser library from `w3c`.
The code does the following:
1) Check if the file is of a worng extension, then throw `NotVaildAutosarFileException`. If it is empty, then throw `EmptyAutosarFileException`.
2) Load the `.arxml` file in memory as a document object which contains `container` nodes.
3) Get the `container` nodes inside an array and sort them based on their `SHORT-NAME` tag content.
4) Create a new document object and append to it the root `AUTOSAR` node then the sorted container nodes get appended to it.
5) Create a new `.arxml` file with the new document object.

### ArxmlSolver.java
The second approach is to get the input as string: 
The code does the following:
1) Check if the file is of a worng extension, then throw `NotVaildAutosarFileException`. If it is empty, then throw `EmptyAutosarFileException`.
2) Convert the file to a string.
3) Get the container attributes in a 2D array where each container is the row and the attribute values as an array `["SHORT-NAME", "UUID", "LONG-NAME"]`.
4) Sort the container array by the `"SHORT-NAME"` attribute.
5) Replace the old values in the file string and output the string to the file.

### start.bat
The batch file which compiles and runs the program. It performs 3 test cases:
1) [test case:](#correct_test) (correct test file)
```bat
java ArxmlParser test.arxml 
```
2) [test case:](#wrong_test) (wrong file extension)
```batch
java ArxmlParser wrong.aml 
```
3) [test case:](#empty_test) (empty arxml file)
```batch
java ApArxmlParserp empty.arxml 
```

---

## Example test cases:

### <a name="correct_test"></a>Test case 1 (correct file):

#### <a name="test_in"></a>test.arxml (**Input**)
```xml
<?xml version="1.0" encoding="UTF-8"?>
<AUTOSAR>
    <CONTAINER UUID="198ae269-8478-44bd-92b5-14982c4ff68a">
        <SHORT-NAME>ContainerB</SHORT-NAME>
        <LONG-NAME>AA</LONG-NAME>
    </CONTAINER>
    <CONTAINER UUID="198ae269-8478-44bd-92b5-14982c4ff68b">
        <SHORT-NAME>ContainerA</SHORT-NAME>
        <LONG-NAME>BB</LONG-NAME>
    </CONTAINER>
    <CONTAINER UUID="198ae269-8478-44bd-92b5-14982c4ff68c">
        <SHORT-NAME>ContainerC</SHORT-NAME>
        <LONG-NAME>CC</LONG-NAME>
    </CONTAINER>
</AUTOSAR>
```

#### <a name="test_out"></a>test_mod.arxml (**Output**)
The bash output:

```
Starting correct test file (test.arxml)
Done creating the ARXML File
```
The modified file's content:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<AUTOSAR>
    <CONTAINER UUID="198ae269-8478-44bd-92b5-14982c4ff68b">
        <SHORT-NAME>ContainerA</SHORT-NAME>
        <LONG-NAME>BB</LONG-NAME>
    </CONTAINER>
    <CONTAINER UUID="198ae269-8478-44bd-92b5-14982c4ff68a">
        <SHORT-NAME>ContainerB</SHORT-NAME>
        <LONG-NAME>AA</LONG-NAME>
    </CONTAINER>
    <CONTAINER UUID="198ae269-8478-44bd-92b5-14982c4ff68c">
        <SHORT-NAME>ContainerC</SHORT-NAME>
        <LONG-NAME>CC</LONG-NAME>
    </CONTAINER>
</AUTOSAR>
```

### <a name="wrong_test"></a>Test case 2 (wrong extension):

#### <a name="wrong"></a>wrong.aml (**Input**)
This file has the same content of [test.arxml](test_in).

#### CMD output:
```bash
Starting wrong file extension test (wrong.aml)
NotVaildAutosarFileException
        at ArxmlParser.main(ArxmlParser.java:33)
```

### <a name="empty_test"></a>Test case 3 (empty file):

#### <a name="empty"></a>empty.arxml (**Input**)
This file is empty.

#### CMD output:
```bash
Starting empty file test (empty.arxml)
Exception in thread "main" EmptyAutosarFileException
        at ArxmlParser.main(ArxmlParser.java:37)
```
