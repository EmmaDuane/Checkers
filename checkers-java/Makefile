JAR_PKG = Main.jar
ENTRY_POINT = Main


# set your source file name
SOURCE_FILE = \
Main.java \
GameLogic.java \
Move.java \
Board.java \
Checker.java\
AI.java \
StudentAI.java \
ManualAI.java \
InvalidMoveError.java

TARGET = ./
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
	@echo "make new: build the target directory."
	@echo "make build: compile the source file."
	@echo "make clean: clear classes generated."
	@echo "make rebuild: rebuild project."
	@echo "make run: run your application at the entry point."
	@echo "make jar: jar the classes."

build: $(SOURCE_FILE:.java=.class)
%.class: %.java
	$(JAVAC) $(JFLAGS) $<

rebuild: clean build

.PHONY: new clean run jar

new:
	mkdir -pv $(TARGET)

clean:
	rm -frv $(TARGET)/*.class

run:
	java $(CLASSPATH) $(ENTRY_POINT)

jar:
	jar cvfe $(JAR_PKG) $(ENTRY_POINT) -C $(TARGET) .
