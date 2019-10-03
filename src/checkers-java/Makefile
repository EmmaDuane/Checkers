#This makefile was originally Connect-K, but the commands are taken from Wumpus World makefile thanks to its simplicity

JAR_PKG = Main.jar
ENTRY_POINT = Main


# set your source file name
SOURCE_FILE = \
Main.java \
GameLogic.java \
Move.java \
Direction.java \
Position.java \
Board.java \
Checker.java\
AI.java \
StudentAI.java \
ManualAI.java \
InvalidMoveError.java \
InvalidParameterError.java \

TARGET = .
SOURCE = ../checker-java
LIB = ../checker-java

JAVAC = javac

ENCODING = -encoding UTF-8
CLASSPATH = -classpath $(LIB)
SOURCEPATH = -sourcepath $(TARGET)
D = -d $(TARGET)
JFLAGS = $D $(CLASSPATH) $(SOURCEPATH) $(ENCODING)

vpath %.class $(TARGET)
vpath %.java $(SOURCE)

Default:
# Remove class files to create new ones
	@rm -fr $(TARGET)/*.class
# Compile and create class files
	@$(JAVAC) $(JFLAGS) *.java
# Remove jar file to create a new one
	@rm -f $(TARGET)/Main.jar
# Create a jar file
	@jar -cfe $(JAR_PKG) $(ENTRY_POINT) -C $(TARGET) .
# Finally, remove all the class files
	@rm -f $(TARGET)/*.class
	@echo Done.
