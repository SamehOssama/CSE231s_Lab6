@echo OFF
echo Welcome to my lab 6 "Error Handling" solution
echo:
set appc=ArxmlParser.java
set app=ArxmlParser
set correctFile=test.arxml
set wrongFile=wrong.aml
set emptyFile=empty.arxml

javac %appc%
echo Starting correct test file (%correctFile%)
java %app% %correctFile%   # Correct test file
echo:
echo Starting wrong file extension test (%wrongFile%)
java %app% %wrongFile%     # Wrong file extension
echo:
echo Starting empty file test (%emptyFile%)
java %app% %emptyFile%     # Empty file
del *.class
pause