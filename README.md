# Power Libraries
Power Libraries is a small project to collect some repeatedly needed or otherwise useful Java 8 classes in a collection of tiny libraries.

## IO Power
IO Power is the first and really tiny library of the Power Libraries. It contains some simple helper method for opening Input- and 
Outputstreams. The main purpose of IO Power is to make opening streams, readers and writers shorter and more understandable.

For example if you need to write a UTF-8 file you would normally write:
```java
try(BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileWriter(new File("test.txt")), StandardCharsets.UTF_8))) {
	out.write("Hello World");
}
```

With IO Power you can simply write;
```java
try(BufferedWriter out = Write.file(new File("test.txt"), StandardCharsets.UTF_8))) {
	out.write("Hello World");
}
```


If you want to read a compressed resource instead of
```java
try(BufferedReader in = new BufferedReader(new InputStreamReader(new GZipInputStream(SomeClass.class.getResourceAsStream("test.gz")), StandardCharsets.UTF_8))) {
	String firstLine = in.readLine();
}
```

you could write
```java
try(BufferedReader in = Read.compressedResource(SomeClass.class.getResource("test.gz"), StandardCharsets.UTF_8))) {
	String firstLine = in.readLine();
}
```
