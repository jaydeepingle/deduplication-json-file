JFLAGS = -g
JC = javac -classpath "jars/*:."
JV = java -classpath "jars/*:."
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
        Lead.java \
        JSONProcessor.java \
	Solution.java

default: classes

classes: $(CLASSES:.java=.class)

run:
	$(JV) Solution leads.json
clean:
	$(RM) *.class
	$(RM) output.json
	$(RM) logs.json
