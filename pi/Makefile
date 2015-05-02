SRC = ./src
BIN = ./bin
JC = javac
JFLAGS = -d $(BIN)
	
#Add source files here. Make sure to prepend with $(SRC)
CLASSES = \
	$(SRC)/Driver.java \
	$(SRC)/BugDetector.java \
	$(SRC)/Bug.java \
	$(SRC)/Pair.java \
	$(SRC)/Scope.java \
	$(SRC)/Parser.java \
	$(SRC)/Invariants.java

CLASS_FILES=$(CLASSES:$(SRC)/%.java=$(BIN)/%.class)
	
all: classes

classes:
	mkdir -p $(BIN)
	$(JC) $(JFLAGS) $(CLASSES) 
	
clean:
	$(RM) $(BIN)/*.class
	
run:
	./pipair
